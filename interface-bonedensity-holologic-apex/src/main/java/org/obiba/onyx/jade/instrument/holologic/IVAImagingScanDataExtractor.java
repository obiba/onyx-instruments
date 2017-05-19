/*******************************************************************************
 * Copyright (c) 2011 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.jade.instrument.holologic;

import java.util.HashMap;
import java.util.Map;

import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.tool.dcmrcv.ApexTag;
import org.dcm4che2.tool.dcmrcv.DicomServer;
import org.obiba.onyx.jade.instrument.holologic.APEXInstrumentRunner.Side;
import org.obiba.onyx.util.data.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * IVA Lateral spine images are captured: no PatScan db tables are accessed.
 */
public class IVAImagingScanDataExtractor extends APEXScanDataExtractor {

  private static final Logger log = LoggerFactory.getLogger(IVAImagingScanDataExtractor.class);

  private Energy energy;

  /**
   * @param patScanDb
   * @param refCurveDb
   * @param participantData
   * @param energy
   * @param server
   * @param apexReceiver
   */
  protected IVAImagingScanDataExtractor(JdbcTemplate patScanDb, JdbcTemplate refCurveDb, Map<String, String> participantData, Energy energy, DicomServer server, ApexReceiver apexReceiver) {
    super(patScanDb, refCurveDb, participantData, server, apexReceiver);
    this.energy = energy;

    ApexDicomData dicom1 = new ApexDicomData();
    dicom1.validator.put(Tag.Modality, new TagEntry(true,true,"OT"));
    dicom1.validator.put(Tag.BodyPartExamined, new TagEntry(true,true,"LSPINE"));
    dicom1.validator.put(Tag.ImageAndFluoroscopyAreaDoseProduct, new TagEntry(true,false,""));
    dicom1.validator.put(Tag.PatientOrientation, new TagEntry(true,false,""));
    dicom1.validator.put(Tag.BitsAllocated, new TagEntry(true,true,"16"));
    dicom1.validator.put(Tag.PhotometricInterpretation, new TagEntry(true,true,"MONOCHROME2"));
    dicom1.validator.put(Tag.PixelSpacing, new TagEntry(true,false,""));
    dicom1.validator.put(Tag.SamplesPerPixel, new TagEntry(true,true,"1"));
    dicom1.validator.put(Tag.MediaStorageSOPClassUID, new TagEntry(true,true,UID.SecondaryCaptureImageStorage));
    dicom1.name = getResultPrefix() + "_DICOM_MEASURE";
    apexDicomList.add(dicom1);

    ApexDicomData dicom2 = new ApexDicomData();
    dicom2.validator.put(Tag.Modality, new TagEntry(true,true,"PR"));
    dicom2.validator.put(Tag.MediaStorageSOPClassUID, new TagEntry(true,true,UID.GrayscaleSoftcopyPresentationStateStorageSOPClass));
    dicom2.name = getResultPrefix() + "_DICOM_PR";
    apexDicomList.add(dicom2);

    ApexDicomData dicom3 = new ApexDicomData();
    dicom3.validator.put(Tag.Modality, new TagEntry(true,true,"OT"));
    dicom3.validator.put(Tag.BitsAllocated, new TagEntry(true,true,"8"));
    dicom3.validator.put(ApexTag.IVARESULTSFLAG.getValue(), new TagEntry(true,true,"IVA Results"));
    dicom3.validator.put(Tag.PhotometricInterpretation, new TagEntry(true,true,"MONOCHROME2"));
    dicom3.validator.put(Tag.SamplesPerPixel, new TagEntry(true,true,"1"));
    dicom3.validator.put(Tag.MediaStorageSOPClassUID, new TagEntry(true,true,UID.SecondaryCaptureImageStorage));
    dicom3.name = getResultPrefix() + "_DICOM_OT";
    apexDicomList.add(dicom3);
  }

  @Override
  public String getBodyPartName() {
    return "LSPINE";
  }

  @Override
  protected long getScanType() {
    switch(energy) {
    case CLSA_DXA:
      return 29l; // QDR4500 single energy supine lateral image, used for VFA, ref_type = L
    case SINGLE_AP:
      return 35l; // single energy AP image, ref_type = S
    case SINGLE_LATERAL:
      return 36l; // single energy left/right lateral image, ref_type = L
    default:
      return 37l; // dual energy left/right lateral image, ref_type = L
    }
  }

  @Override
  public String getName() {
    switch(energy) {
    case DUAL_LATERAL:
      return "DEL";
    default:
      return "SEL";
    }
  }

  @Override
  public String getRefType() {
    switch(energy) {
    case SINGLE_AP:
      return "S"; // single energy AP image, ref_type = S
    default:
      return "L"; // dual energy left/right lateral image, ref_type = L
    }
  }

  @Override
  public String getRefSource() {
    return "NHANES";
  }

  @Override
  protected void extractDataImpl(Map<String, Data> data) {
    log.warn("no additional data can be extracted for this scan");
  }

  public enum Energy {
    SINGLE_LATERAL, DUAL_LATERAL, SINGLE_AP,
    // clsa c-arm (different of simulation mode)
    CLSA_DXA
  }

  @Override
  public Side getSide() {
    return null;
  }

}
