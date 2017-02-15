/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.jade.instrument.sheffielduniversity;

import javax.swing.*;
import java.awt.*;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FilenameFilter;
import java.io.StringWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.obiba.onyx.jade.client.JnlpClient;
import org.obiba.onyx.jade.instrument.ExternalAppLauncherHelper;
import org.obiba.onyx.jade.instrument.InstrumentRunner;
import org.obiba.onyx.jade.instrument.service.InstrumentExecutionService;
import org.obiba.onyx.util.FileUtil;
import org.obiba.onyx.util.UnicodeReader;
import org.obiba.onyx.util.data.Data;
import org.obiba.onyx.util.data.DataBuilder;
import org.obiba.onyx.util.data.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Specified instrument runner for the FRAX calculator
 *
 * @author inglisd
 */

public class FRAXInstrumentRunner implements InstrumentRunner/*, InitializingBean*/ {

  private static final Logger log = LoggerFactory.getLogger(JnlpClient.class);

  // Injected by spring.
  protected InstrumentExecutionService instrumentExecutionService;

  protected ExternalAppLauncherHelper externalAppHelper;

  private String inFileName;

  private String outFileName;

  private String typeCode;

  private String countryCode;

  private String fraxPath;

  public InstrumentExecutionService getInstrumentExecutionService() {
    return instrumentExecutionService;
  }

  public void setInstrumentExecutionService(InstrumentExecutionService instrumentExecutionService) {
    this.instrumentExecutionService = instrumentExecutionService;
  }

  public ExternalAppLauncherHelper getExternalAppHelper() {
    return externalAppHelper;
  }

  public void setExternalAppHelper(ExternalAppLauncherHelper externalAppHelper) {
    this.externalAppHelper = externalAppHelper;
  }

  public String getFraxPath() {
    return fraxPath;
  }

  public void setFraxPath(String fraxPath) {
    this.fraxPath = fraxPath;
  }

  private File getInFile() {
    return new File( getFraxPath(), inFileName );
  }

  private File getOutFile() {
    return new File( getFraxPath(), outFileName );
  }

  public void setInFileName(String inFileName) {
    this.inFileName = inFileName;
  }

  public void setOutFileName(String outFileName) {
    this.outFileName = outFileName;
  }

  public String getInFileName() {
    return inFileName;
  }

  public String getOutFileName() {
    return outFileName;
  }

  /**
   * Implements parent method initialize from InstrumentRunner
   */
  public void initialize() {
    if(externalAppHelper.isSotfwareAlreadyStarted()) {
      JOptionPane.showMessageDialog(null, externalAppHelper
          .getExecutable() + " already locked for execution.  Please make sure that another instance is not running.",
          "Cannot start application!", JOptionPane.ERROR_MESSAGE);
      throw new RuntimeException("already locked for execution");
    }
    createBackupFiles();
    initParticipantData();
  }

  /**
   * Implements parent method run from InstrumentRunner
   */
  public void run() {
    externalAppHelper.launch();

    try {
      Thread.sleep(2000);
    } catch(InterruptedException e) {
    }

    Map<String, Data> data = retrieveDeviceData();
    sendDataToServer(data);
  }

  private void sendDataToServer(Map<String, Data> data) {
    instrumentExecutionService.addOutputParameterValues(data);
  }

  /**
   *  parse the frax output.txt file
   */
  private Map<String, Data> retrieveDeviceData() {

    Map<String, Data> outputData = new HashMap<String, Data>();
    File resultFile = getOutFile();
    InputStream resultFileStrm = null;
    UnicodeReader resultReader = null;
    BufferedReader fileReader = null;
    String delim = ",";

    try {
      if(resultFile.exists()) {
        outputData.put("RESULT_FILE", DataBuilder.buildBinary(resultFile));
        resultFileStrm = new FileInputStream(resultFile);
        resultReader = new UnicodeReader(resultFileStrm);
        fileReader = new BufferedReader(resultReader);
        String line;
        List<String> output = new ArrayList<String>();
        int expectedCount = 17;
        while(null != (line = fileReader.readLine())) {
          if(false == line.isEmpty()) {
            output.addAll(Arrays.asList(line.split(delim)));
          }
        }
        if(expectedCount == output.size()) {
          outputData.put("OSTEO_FX", DataBuilder.buildDecimal(Double.valueOf(output.get(13).trim())));
          outputData.put("HIP_FX", DataBuilder.buildDecimal(Double.valueOf(output.get(14).trim())));
          outputData.put("OSTEO_BMD_FX", DataBuilder.buildDecimal(Double.valueOf(output.get(15).trim())));
          outputData.put("HIP_BMD_FX", DataBuilder.buildDecimal(Double.valueOf(output.get(16).trim())));
        }

        resultFileStrm.close();
        fileReader.close();
        resultReader.close();
      }
    } catch(FileNotFoundException fnfEx) {
      log.warn("Frax output file not found");
    } catch(IOException ioEx) {
      throw new RuntimeException("Error: retrieve frax data IOException", ioEx);
    } catch(Exception ex) {
      throw new RuntimeException("Error: retrieve frax data", ex);
    }

    return outputData;
  }

  /**
   * Implements parent method shutdown from InstrumentRunner Delete results from current measurement
   */
  public void shutdown() {
    restoreFiles();
  }

  /**
   *  back up existing frax files (input.txt, output.txt)
   */
  private void createBackupFiles() {
    File inFile = getInFile();
    File backupInFile = new File( inFile.getName() + ".orig" );
    try {
      if(inFile.exists()) {
        FileUtil.copyFile(inFile,backupInFile);
      }
    } catch(Exception e) {
      throw new RuntimeException("Error backing up FRAX " + inFile.getName() + " file", e);
    }
    File outFile = getOutFile();
    File backupOutFile = new File( outFile.getName() + ".orig" );
    try {
      if(outFile.exists()) {
        FileUtil.copyFile(outFile,backupOutFile);
        outFile.delete();
      }
    } catch(Exception e) {
      throw new RuntimeException("Error backing up FRAX " + outFile.getName() + " file", e);
    }
  }

  /**
   *  restore any backed up frax files (input.txt, output.txt)
   */
  private void restoreFiles() {
    File inFile = getInFile();
    File backupInFile = new File( inFile.getName() + ".orig" );
    try {
      if(backupInFile.exists()) {
        FileUtil.copyFile(backupInFile,inFile);
      }
    } catch(Exception e) {
      throw new RuntimeException("Error restoring FRAX " + inFile.getName() + " file", e);
    }
    File outFile = getOutFile();
    File backupOutFile = new File( outFile.getName() + ".orig" );
    try {
      if(backupOutFile.exists()) {
        FileUtil.copyFile(backupOutFile,outFile);
      }
    } catch(Exception e) {
      throw new RuntimeException("Error restoring FRAX " + outFile.getName() + " file", e);
    }
  }

  /**
   *  Initialize the frax input.txt file
   */
  private void initParticipantData() {
    File inFile = getInFile();
    try {
      StringWriter writer = new StringWriter();

      writer.write(typeCode);
      writer.write(",");
      writer.write(countryCode);
      writer.write(",");

      Double age = null;
      if(instrumentExecutionService.hasInputParameter("INPUT_PARTICIPANT_BIRTH_DATE") &&
         instrumentExecutionService.hasInputParameter("INPUT_PARTICIPANT_INTERVIEW_DATE")) {
        String dob = instrumentExecutionService.getInputParameterValue("INPUT_PARTICIPANT_BIRTH_DATE").getValueAsString();
        String today = instrumentExecutionService.getInputParameterValue("INPUT_PARTICIPANT_INTERVIEW_DATE").getValueAsString();
        try {
          age = computeYearsDifference(today, dob);
        } catch(ParseException e) {
        }
      }
      writer.write((null != age ? age.toString() : "_"));
      writer.write(",");

      String[] parameterArray = {
        "INPUT_PARTICIPANT_SEX",
        "INPUT_PARTICIPANT_BMI",
        "INPUT_PARTICIPANT_PREVIOUS_FX",
        "INPUT_PARTICIPANT_PARENT_FX",
        "INPUT_PARTICIPANT_SMOKER",
        "INPUT_PARTICIPANT_GLUCO",
        "INPUT_PARTICIPANT_ARTHRITIS",
        "INPUT_PARTICIPANT_OSTEO",
        "INPUT_PARTICIPANT_ALCOHOL",
        "INPUT_PARTICIPANT_TSCORE"
      };
      int size = parameterArray.length - 1;
      for(int i = 0; i < size; i++) {
        writeParameter(writer, parameterArray[i], false);
      }
      writeParameter(writer, parameterArray[size], true);
      FileWriter fileWriter = new FileWriter(inFile);
      fileWriter.write(writer.toString());
      fileWriter.close();

    } catch(Exception e) {
      log.error("Unable to write participant data: " + inFile.getAbsolutePath(), e);
      instrumentExecutionService.instrumentRunnerError(e);
    }
  }

  private void writeParameter(StringWriter writer, String name, boolean isLast ) {
    if(instrumentExecutionService.hasInputParameter(name)) {
      Data data = instrumentExecutionService.getInputParameterValue(name);
      String value = null != data.getValue() ? data.getValueAsString() : "_";
      writer.write(value);
    } else {
      writer.write("_");
    }
    if(!isLast) {
      writer.write(",");
    }
  }

  /**
   *
   * @param s1
   * @param s2
   * @return
   * @throws ParseException
   */
  public static Double computeYearsDifference(String s1, String s2) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Date d1;
    try {
      d1 = format.parse(s1);
    } catch(ParseException e) {
      throw e;
    }
    Date d2;
    try {
      d2 = format.parse(s2);
    } catch(ParseException e) {
      throw e;
    }

    Calendar c1 = Calendar.getInstance();
    c1.setTime(d1);
    Calendar c2 = Calendar.getInstance();
    c2.setTime(d2);

    Double diff = (c1.getTimeInMillis() - c2.getTimeInMillis()) / (1000. * 60. * 60. * 24. * 365.25);
    if(diff < 0.) diff *= -1.;

    return diff;
  }

}
