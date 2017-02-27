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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.obiba.onyx.jade.instrument.ExternalAppLauncherHelper;
import org.obiba.onyx.jade.instrument.service.InstrumentExecutionService;
import org.obiba.onyx.util.FileUtil;
import org.obiba.onyx.util.data.Data;
import org.obiba.onyx.util.data.DataBuilder;

public class FRAXInstrumentRunnerTest {

  private ExternalAppLauncherHelper externalAppHelper;

  private FRAXInstrumentRunner fraxInstrumentRunner;

  private InstrumentExecutionService instrumentExecutionServiceMock;

  @Before
  public void setUp() throws IOException, URISyntaxException {

    fraxInstrumentRunner = new FRAXInstrumentRunner();

    // create a test directory to simulate blackbox.exe software installation path
    String fraxSimulatedPath = new File("target", "test-frax").getPath();
    (new File(fraxSimulatedPath)).mkdir();

    fraxInstrumentRunner.setFraxPath(fraxSimulatedPath);
    fraxInstrumentRunner.setInFileName("input.txt");
    fraxInstrumentRunner.setOutFileName("output.txt");
    fraxInstrumentRunner.setTypeCode("t");
    fraxInstrumentRunner.setCountryCode(19);

    // Cannot mock ExternalAppLauncherHelper (without EasyMock extension!),
    // so for now, use the class itself with the launch method overridden to
    // do nothing.
    externalAppHelper = new ExternalAppLauncherHelper() {
      public void launch() {
        // do nothing
      }

      @Override
      public boolean isSotfwareAlreadyStarted(String lockname) {
        return false;
      }
    };

    fraxInstrumentRunner.setExternalAppHelper(externalAppHelper);

    // Create a mock instrumentExecutionService for testing.
    instrumentExecutionServiceMock = createMock(InstrumentExecutionService.class);
    fraxInstrumentRunner.setInstrumentExecutionService(instrumentExecutionServiceMock);
  }

  @After
  public void tearDown() {
    fraxInstrumentRunner.shutdown();
    String fraxSimulatedPath = new File("target", "test-frax").getPath();
    File fraxSimulated = new File(fraxSimulatedPath);
    if (fraxSimulated.exists()) {
      try {
        FileUtil.delete(fraxSimulated);
      } catch(IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  public void testInitialize() throws FileNotFoundException, IOException, URISyntaxException {

    simulatePreExistingFiles();

    // Set arbitrary inputs for testing.
    Map<String, Data> inputData = new HashMap<String, Data>();
    inputData.put("INPUT_PARTICIPANT_SEX", DataBuilder.buildInteger(0));
    inputData.put("INPUT_PARTICIPANT_BMI", DataBuilder.buildDecimal(25.7));
    inputData.put("INPUT_PARTICIPANT_PREVIOUS_FX", DataBuilder.buildInteger(0));
    inputData.put("INPUT_PARTICIPANT_PARENT_FX", DataBuilder.buildInteger(0));
    inputData.put("INPUT_PARTICIPANT_SMOKER", DataBuilder.buildInteger(0));
    inputData.put("INPUT_PARTICIPANT_GLUCO", DataBuilder.buildInteger(0));
    inputData.put("INPUT_PARTICIPANT_ARTHRITIS", DataBuilder.buildInteger(0));
    inputData.put("INPUT_PARTICIPANT_OSTEO", DataBuilder.buildInteger(0));
    inputData.put("INPUT_PARTICIPANT_ALCOHOL", DataBuilder.buildInteger(0));
    inputData.put("INPUT_PARTICIPANT_TSCORE", DataBuilder.buildDecimal(-2.1));
    inputData.put("INPUT_PARTICIPANT_BIRTH_DATE", DataBuilder.buildDate(getSimulatedDate(1936,8,26)));
    inputData.put("INPUT_PARTICIPANT_INTERVIEW_DATE", DataBuilder.buildDate(getSimulatedDate(2012,8,13)));

    expect(instrumentExecutionServiceMock.getInputParametersValue(
      "INPUT_PARTICIPANT_SEX",
      "INPUT_PARTICIPANT_BMI",
      "INPUT_PARTICIPANT_PREVIOUS_FX",
      "INPUT_PARTICIPANT_PARENT_FX",
      "INPUT_PARTICIPANT_SMOKER",
      "INPUT_PARTICIPANT_GLUCO",
      "INPUT_PARTICIPANT_ARTHRITIS",
      "INPUT_PARTICIPANT_OSTEO",
      "INPUT_PARTICIPANT_ALCOHOL",
      "INPUT_PARTICIPANT_TSCORE",
      "INPUT_PARTICIPANT_BIRTH_DATE",
      "INPUT_PARTICIPANT_INTERVIEW_DATE")).andReturn(inputData);

    replay(instrumentExecutionServiceMock);

    fraxInstrumentRunner.initialize();

    verify(instrumentExecutionServiceMock);

    verifyInitialization();
  }

  // note that month is zero-based
  private java.util.Date getSimulatedDate(int year, int month, int day) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, day);

    return c.getTime();
  }

  @Test
  public void testShutdown() throws FileNotFoundException, IOException, URISyntaxException {

    simulatePreExistingFiles();

    fraxInstrumentRunner.shutdown();

    verifyCleanup();
  }

  @Test
  public void testRun() throws FileNotFoundException, IOException, URISyntaxException {

    externalAppHelper.launch();

    simulateResults();

    Map<String, Data> results = fraxInstrumentRunner.retrieveDeviceData();

    // Compare the values read with the ones from the result file.
    Assert.assertEquals(6.89, Double.parseDouble(results.get("OSTEO_FX").getValueAsString()), 0);
    Assert.assertEquals(2.42, Double.parseDouble(results.get("HIP_FX").getValueAsString()), 0);
    Assert.assertEquals(9.17, Double.parseDouble(results.get("OSTEO_BMD_FX").getValueAsString()), 0);
    Assert.assertEquals(3.58, Double.parseDouble(results.get("HIP_BMD_FX").getValueAsString()), 0);

    instrumentExecutionServiceMock.addOutputParameterValues((Map<String, Data>)anyObject());
    replay(instrumentExecutionServiceMock);

    // Make sure that the results are sent to the server.
    fraxInstrumentRunner.sendDataToServer(results);

    verify(instrumentExecutionServiceMock);

    verifyCleanup();
  }

  private void simulateResults() throws FileNotFoundException, IOException, URISyntaxException {

    // Copy the results file to the test directory.
    FileUtil.copyFile(new File(getClass().getResource("/output.txt").toURI()),
      new File(fraxInstrumentRunner.getFraxPath(), fraxInstrumentRunner.getOutFileName()) );
  }

  private void simulatePreExistingFiles() throws IOException {

    // Create dummy pre-existing files
    FileWriter dummyInput = new FileWriter(new File(fraxInstrumentRunner.getFraxPath(), fraxInstrumentRunner.getInFileName()));
    dummyInput.write("test input");
    dummyInput.close();
    FileWriter dummyOutput = new FileWriter(new File(fraxInstrumentRunner.getFraxPath(), fraxInstrumentRunner.getOutFileName()));
    dummyOutput.write("test output");
    dummyOutput.close();
  }

  private void verifyInitialization() {

    // Make sure the backup files have been created.
    Assert.assertTrue(new File(fraxInstrumentRunner.getFraxPath(), fraxInstrumentRunner.getInFileName() + ".orig").exists());
    Assert.assertTrue(new File(fraxInstrumentRunner.getFraxPath(), fraxInstrumentRunner.getOutFileName() + ".orig").exists());
  }

  private void verifyCleanup() {

    // Make sure the backup files have been deleted.
    Assert.assertFalse(new File(fraxInstrumentRunner.getFraxPath(), fraxInstrumentRunner.getInFileName() + ".orig").exists());
    Assert.assertFalse(new File(fraxInstrumentRunner.getFraxPath(), fraxInstrumentRunner.getOutFileName() + ".orig").exists());
  }
}
