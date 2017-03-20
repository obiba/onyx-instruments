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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

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

/**
 * Specified instrument runner for the FRAX calculator
 *
 * @author inglisd
 */

public class FRAXInstrumentRunner implements InstrumentRunner {

  private static final Logger log = LoggerFactory.getLogger(FRAXInstrumentRunner.class);

  // Injected by spring.
  protected InstrumentExecutionService instrumentExecutionService;

  protected ExternalAppLauncherHelper externalAppHelper;

  // by default always input.txt
  private String inFileName;

  // by default always output.txt
  private String outFileName;

  // "t" for T-Score or "z" for Z-Score
  private String typeCode;

  // FRAX code for country and ethnicity
  private Integer countryCode;

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

  public String getTypeCode() {
    return typeCode;
  }

  public void setTypeCode(String code) {
    this.typeCode = code.toLowerCase().equals("t") ? "t" : "z";
  }

  public Integer getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(Integer code) {
    this.countryCode = code;
  }

  private File getInFile() {
    return new File(externalAppHelper.getWorkDir(), inFileName);
  }

  private File getOutFile() {
    return new File(externalAppHelper.getWorkDir(), outFileName);
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
  @Override
  public void initialize() {
    log.info("Initializing Frax");
    createBackupFiles();
    initParticipantData();
  }

  /**
   * Implements parent method run from InstrumentRunner
   */
  @Override
  public void run() {
    log.info("Launching Frax application");
    externalAppHelper.launch();

    try {
      log.info("Closing application at {}", new Date().toString());
      Thread.sleep(2000);
    } catch(InterruptedException e) {
    }

    log.info("Retrieving measurements");
    Map<String, Data> data = retrieveDeviceData();

    log.info("Sending data to server");
    sendDataToServer(data);
  }

  public void sendDataToServer(Map<String, Data> data) {
    instrumentExecutionService.addOutputParameterValues(data);
  }

  /**
   *  parse the frax output.txt file
   */
  public Map<String, Data> retrieveDeviceData() {

    Map<String, Data> outputData = new HashMap<String, Data>();
    File resultFile = getOutFile();
    InputStream resultFileStrm = null;
    UnicodeReader resultReader = null;
    BufferedReader fileReader = null;
    String delim = ",";

    try {
      if(resultFile.exists() && !resultFile.isDirectory()) {
        log.info("Frax result file found ... parsing");

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
        resultFileStrm.close();
        fileReader.close();
        resultReader.close();

        output.removeAll(Arrays.asList("",null));
        String parseResult = "";
        for(String s : output) {
          parseResult += s + ",";
        }
        log.info("Parse results: " + parseResult);

        if(expectedCount == output.size()) {
          outputData.put("OSTEO_FX", DataBuilder.buildDecimal(Double.valueOf(output.get(13).trim())));
          outputData.put("HIP_FX", DataBuilder.buildDecimal(Double.valueOf(output.get(14).trim())));
          outputData.put("OSTEO_BMD_FX", DataBuilder.buildDecimal(Double.valueOf(output.get(15).trim())));
          outputData.put("HIP_BMD_FX", DataBuilder.buildDecimal(Double.valueOf(output.get(16).trim())));
          log.info("Expected results size achieved");
        }

        outputData.put("RESULT_FILE", DataBuilder.buildBinary(resultFile));
      }  else {
        log.info("Frax result file missing " + resultFile.getAbsoluteFile());
      }
    } catch(FileNotFoundException fnfEx) {
      log.warn("Frax output file not found");
      throw new RuntimeException("Error: retrieve frax data FileNotfoundException", fnfEx);

    } catch(IOException ioEx) {
      throw new RuntimeException("Error: retrieve frax data IOException", ioEx);

    } catch(Exception ex) {
      throw new RuntimeException("Error: retrieve frax data Exception", ex);

    }

    return outputData;
  }

  /**
   * Implements parent method shutdown from InstrumentRunner Delete results from current measurement
   */
  @Override
  public void shutdown() {
    log.info("Shut down");
    restoreFiles();
  }

  /**
   *  back up existing frax files (input.txt, output.txt)
   */
  private void createBackupFiles() {
    File inFile = getInFile();
    File backupInFile = new File( inFile.getAbsoluteFile() + ".orig" );
    try {
      if(inFile.exists() && !inFile.isDirectory()) {
        log.info("backing up existing input file");
        FileUtil.copyFile(inFile,backupInFile);
        inFile.delete();
      }
    } catch(Exception e) {
      throw new RuntimeException("Error backing up FRAX " + inFile.getAbsoluteFile() + " file", e);
    }
    File outFile = getOutFile();
    File backupOutFile = new File( outFile.getAbsoluteFile() + ".orig" );
    try {
      if(outFile.exists() && !outFile.isDirectory()) {
        log.info("backing up existing output file");
        FileUtil.copyFile(outFile,backupOutFile);
        outFile.delete();
      }
    } catch(Exception e) {
      throw new RuntimeException("Error backing up FRAX " + outFile.getAbsoluteFile() + " file", e);
    }
  }

  /**
   *  restore any backed up frax files (input.txt, output.txt)
   */
  private void restoreFiles() {
    File inFile = getInFile();
    File backupInFile = new File( inFile.getAbsoluteFile() + ".orig" );
    try {
      if(backupInFile.exists()) {
        log.info("restoring pre-existing input file {} => {}",
          backupInFile.getAbsoluteFile(), inFile.getAbsoluteFile());
        FileUtil.copyFile(backupInFile,inFile);
        backupInFile.delete();
      } else {
        inFile.delete();
      }
    } catch(Exception e) {
      throw new RuntimeException("Error restoring FRAX " + inFile.getAbsoluteFile() + " file", e);
    }
    File outFile = getOutFile();
    File backupOutFile = new File( outFile.getAbsoluteFile() + ".orig" );
    try {
      if(backupOutFile.exists()) {
        log.info("restoring pre-existing output file {} => {}",
          backupOutFile.getAbsoluteFile(), outFile.getAbsoluteFile());
        FileUtil.copyFile(backupOutFile,outFile);
        backupOutFile.delete();
      } else {
        outFile.delete();
      }
    } catch(Exception e) {
      throw new RuntimeException("Error restoring FRAX " + outFile.getAbsoluteFile() + " file", e);
    }
  }

  /**
   *  Initialize the frax input.txt file
   */
  private void initParticipantData() {
    File inFile = getInFile();
    try {
      StringWriter writer = new StringWriter();
      String[] inputParameterCodes = new String[] {
        "INPUT_PARTICIPANT_SEX",
        "INPUT_PARTICIPANT_BMI",
        "INPUT_PARTICIPANT_PREVIOUS_FX",
        "INPUT_PARTICIPANT_PARENT_FX",
        "INPUT_PARTICIPANT_SMOKER",
        "INPUT_PARTICIPANT_GLUCO",
        "INPUT_PARTICIPANT_ARTHRITIS",
        "INPUT_PARTICIPANT_OSTEO",
        "INPUT_PARTICIPANT_ALCOHOL",
        (typeCode.equalsIgnoreCase("t") ?
         "INPUT_PARTICIPANT_TSCORE" : "INPUT_PARTICIPANT_ZSCORE"),
         "INPUT_PARTICIPANT_BIRTH_DATE",
         "INPUT_PARTICIPANT_INTERVIEW_DATE"
      };

      Map<String, Data> inputData = instrumentExecutionService.getInputParametersValue(inputParameterCodes);

      writer.write(typeCode);
      writer.write(",");
      writer.write(Integer.toString(countryCode));
      writer.write(",");

      Double age = null;
      if(inputData.containsKey("INPUT_PARTICIPANT_BIRTH_DATE") &&
         inputData.containsKey("INPUT_PARTICIPANT_INTERVIEW_DATE")) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dob = dateFormat.format(inputData.get("INPUT_PARTICIPANT_BIRTH_DATE").getValue());
        String today = dateFormat.format(inputData.get("INPUT_PARTICIPANT_INTERVIEW_DATE").getValue());
        try {
          age = computeYearsDifference(today, dob);
          DecimalFormat decFormat = new DecimalFormat("0.##");
          age = Double.valueOf(decFormat.format(age));
        } catch(ParseException e) {
        }
      }
      writer.write((null != age ? age.toString() : "_"));
      writer.write(",");

      int size = inputParameterCodes.length - 2;
      for(int i = 0; i < size; i++) {
        String code = inputParameterCodes[i];
        if(inputData.containsKey(code)) {
          Data data = inputData.get(code);
          String value = null != data.getValue() ? data.getValueAsString() : "_";
          writer.write(value);
        } else {
          writer.write("_");
        }
        if(size - 1 != i) {
          writer.write(",");
        }
      }

      FileWriter fileWriter = new FileWriter(inFile);
      fileWriter.write(writer.toString());
      fileWriter.close();
      log.info("Wrote Frax input: " + writer.toString());
      log.info("to file: " + inFile.getAbsolutePath());

    } catch(Exception e) {
      log.error("Unable to write participant data: " + inFile.getAbsolutePath(), e);
      instrumentExecutionService.instrumentRunnerError(e);
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
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

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
