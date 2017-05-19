/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.jade.instrument.holologic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ApexReceiver extends JFrame {

  private static final String OK_PENDING = "Ok, click refresh in onyx";

  private static final String HOLOGIC_APEX_RECEIVER = "Hologic Apex Receiver";

  private static final String APEX_DATABASE_VARIABLES = "Apex database variables:";

  private static final String APEX_DICOM_FILES = "Apex DICOM files:";

  private static final String CAPTURE_WAIT = "Waiting capture...";

  private static final String MISSING_DATA = "Missing database variables...";

  private static final String OK = "OK";

  private static final String P_AND_R_NOT_INCLUDED = "P and R not included: Configure Apex, close this window and restart";

  private static final String INCORRECT_DICOM_SENT = "Incorrect participant image(s) sent from Apex";

  private static final String MISSING_DICOM = "Missing DICOM files...";

  private static final long serialVersionUID = 1L;

  private final CountDownLatch exitLatch = new CountDownLatch(1);

  private JButton captureButton;

  private JPanel variableStatusPanel;

  private JPanel dicomStatusPanel;

  private JLabel participantIDLabel;

  private JLabel waitingCaptureVariableLabel;

  private JLabel waitingCaptureDicomLabel;

  private JButton saveButton;

  private boolean validParticipantDicom = true;

  private boolean validPandRDicom = true;

  public ApexReceiver() {
    init();
    initPlus();
  }

  public void init() {
    setTitle(HOLOGIC_APEX_RECEIVER);
    getContentPane().setLayout(new BorderLayout(0, 0));

    // add a panel to the center of the parent frame with a grid of 3 rows x 2 columns
    JPanel panel = new JPanel();
    panel.setBackground(Color.LIGHT_GRAY);
    getContentPane().add(panel, BorderLayout.CENTER);
    panel.setLayout(new GridLayout(3, 2, 0, 5));

    // add a child panel to the parent panel (row 1 col 1)
    JPanel participantIDPanel = new JPanel();
    panel.add(participantIDPanel);

    // child panel contains participant ID label
    participantIDLabel = new JLabel();
    participantIDPanel.add(participantIDLabel);

    // add a child panel to the parent to hold the capture button (row 1 col 2)
    JPanel capturePanel = new JPanel();
    panel.add(capturePanel);

    // add button to child panel
    captureButton = new JButton("Capture");
    capturePanel.add(captureButton);

    // child panel for variables label (row 2 col 1)
    JPanel variableLabelPanel = new JPanel();
    FlowLayout flowLayout = (FlowLayout) variableLabelPanel.getLayout();
    flowLayout.setAlignment(FlowLayout.RIGHT);
    panel.add(variableLabelPanel);

    // static variables label
    JLabel databaseLabel = new JLabel(APEX_DATABASE_VARIABLES);
    variableLabelPanel.add(databaseLabel);

    // child panel for variables status label
    variableStatusPanel = new JPanel();
    flowLayout = (FlowLayout) variableStatusPanel.getLayout();
    flowLayout.setAlignment(FlowLayout.LEFT);
    panel.add(variableStatusPanel);

    // dynamic variables status label
    waitingCaptureVariableLabel = new JLabel(CAPTURE_WAIT);
    variableStatusPanel.add(waitingCaptureVariableLabel);

    // child panel for variables label (row 3 col 1)
    JPanel dicomLabelPanel = new JPanel();
    flowLayout = (FlowLayout) dicomLabelPanel.getLayout();
    flowLayout.setAlignment(FlowLayout.RIGHT);
    panel.add(dicomLabelPanel);

    // static dicom data label
    JLabel labelRawDataIn = new JLabel(APEX_DICOM_FILES);
    dicomLabelPanel.add(labelRawDataIn);

    // child panel for dicom data status label (row 3 col 2)
    dicomStatusPanel = new JPanel();
    flowLayout = (FlowLayout) dicomStatusPanel.getLayout();
    flowLayout.setAlignment(FlowLayout.LEFT);
    panel.add(dicomStatusPanel);

    // dynamic dicom data status label
    waitingCaptureDicomLabel = new JLabel(CAPTURE_WAIT);
    dicomStatusPanel.add(waitingCaptureDicomLabel);

    // child panel for save button
    JPanel savePanel = new JPanel();
    getContentPane().add(savePanel, BorderLayout.SOUTH);

    // save button
    saveButton = new JButton(OK);
    savePanel.add(saveButton);
    setSaveDisable();
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ApexReceiver.this.dispose();
      }
    });
  }

  public void initPlus() {
    setBounds(1, 1, 570, 185);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation((screenSize.width - getWidth()) / 2, screenSize.height - getHeight() - 70);
    setResizable(false);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        exitLatch.countDown();
      }
    });
  }

  public void setCaptureActionListener(ActionListener actionListener) {
    captureButton.addActionListener(actionListener);
  }

  public void setVariableStatusOK() {
    waitingCaptureVariableLabel.setText(OK);
    variableStatusPanel.setBackground(Color.GREEN);
  }

  public void setVariableStatusNotOK() {
    waitingCaptureVariableLabel.setText(MISSING_DATA);
    variableStatusPanel.setBackground(Color.RED);
    setSaveDisable();
  }

  public void setVariableStatusOKPending() {
    waitingCaptureVariableLabel.setText(OK_PENDING);
    variableStatusPanel.setBackground(Color.GREEN);
  }

  public void setDicomStatusOK() {
    waitingCaptureDicomLabel.setText(OK);
    dicomStatusPanel.setBackground(Color.GREEN);
    initializeDicomFileState();
  }

  public void setDicomStatusNotOK() {
    StringBuilder result = new StringBuilder();
    if(false == isValidPandRDicomFile()) {
      result.append(P_AND_R_NOT_INCLUDED);
    }
    if(false == isValidParticipantDicomFile()) {
      if(result.toString().isEmpty()) result.append(System.lineSeparator());
      result.append(INCORRECT_DICOM_SENT);
    }
    waitingCaptureDicomLabel.setText(result.toString());
    dicomStatusPanel.setBackground(Color.RED);
    initializeDicomFileState();
    setSaveDisable();
  }

  public void setDicomStatusNotReady() {
    waitingCaptureDicomLabel.setText(MISSING_DICOM);
    dicomStatusPanel.setBackground(Color.RED);
    initializeDicomFileState();
    setSaveDisable();
  }

  private void setSaveDisable() {
    saveButton.setEnabled(false);
  }

  public void setSaveEnable() {
    saveButton.setEnabled(true);
  }

  public void setParticipantID(String id) {
    participantIDLabel.setText("Participant ID: " + id);
  }

  public void waitForExit() {
    try {
      exitLatch.await();
    } catch(InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void updatePandRDicomFileState(boolean state) {
    validPandRDicom &= state;
  }

  public boolean isValidPandRDicomFile() {
    return validPandRDicom;
  }

  public void updateParticipantDicomFileState(boolean state) {
    validParticipantDicom &= state;
  }

  public boolean isValidParticipantDicomFile() {
    return validParticipantDicom;
  }

  public void initializeDicomFileState() {
    validPandRDicom = true;
    validParticipantDicom = true;
  }

}
