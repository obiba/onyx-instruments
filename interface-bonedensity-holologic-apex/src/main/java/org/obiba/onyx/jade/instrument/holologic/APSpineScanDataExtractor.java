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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.tool.dcmrcv.DicomServer;
import org.obiba.onyx.jade.instrument.holologic.APEXInstrumentRunner.Side;
import org.obiba.onyx.util.data.Data;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *  AP Lumbar spine data are extracted from Spine PatScan db table.
 */
public class APSpineScanDataExtractor extends APEXScanDataExtractor {

  /**
   * @param patScanDb
   * @param refCurveDb
   * @param participantData
   * @param server
   * @param apexReceiver
   */
  protected APSpineScanDataExtractor(JdbcTemplate patScanDb, JdbcTemplate refCurveDb, Map<String, String> participantData, DicomServer server, ApexReceiver apexReceiver) {
    super(patScanDb, refCurveDb, participantData, server, apexReceiver);
    ApexDicomData dicom1 = new ApexDicomData();
    dicom1.validator.put(Tag.Modality, new TagEntry(true,true,"OT"));
    dicom1.validator.put(Tag.BodyPartExamined, new TagEntry(true,true,"LSPINE"));
    dicom1.validator.put(Tag.ImageAndFluoroscopyAreaDoseProduct, new TagEntry(true,false,""));
    dicom1.validator.put(Tag.PatientOrientation, new TagEntry(true,false,""));
    dicom1.validator.put(Tag.BitsAllocated, new TagEntry(true,true,"8"));
    dicom1.validator.put(Tag.PhotometricInterpretation, new TagEntry(true,true,"RGB"));
    dicom1.validator.put(Tag.PixelSpacing, new TagEntry(true,true,null));
    dicom1.validator.put(Tag.SamplesPerPixel, new TagEntry(true,true,"3"));
    dicom1.validator.put(Tag.MediaStorageSOPClassUID, new TagEntry(true,true,UID.SecondaryCaptureImageStorage));
    dicom1.name = getResultPrefix() + "_DICOM";
    apexDicomList.add(dicom1);
  }

  @Override
  public String getName() {
    return "SP";
  }

  // NOTE: the dicom BodyPartExamined tag for both IVA lateral and AP lumbar spine is "LSPINE"
  // Here we return "SPINE" to distinguish between the two types of scan.  See
  // APEXScanDataExtractor for further implementation details.
  @Override
  public String getBodyPartName() {
    return "SPINE";
  }

  @Override
  protected void extractDataImpl(Map<String, Data> data) {
    extractScanData("Spine", data, new SpineResultSetExtractor(data));
  }

  @Override
  protected long getScanType() {
    return 1l;
  }

  @Override
  public String getRefType() {
    return "S";
  }

  @Override
  public String getRefSource() {
    return "Hologic";
  }

  private final class SpineResultSetExtractor extends ResultSetDataExtractor {

    public SpineResultSetExtractor(Map<String, Data> data) {
      super(data);
    }

    @Override
    protected void putData() throws SQLException, DataAccessException {
      putLong("NO_REGIONS");
      putLong("STARTING_REGION");

      for(int i = 1; i <= 4; i++) {
        putBoolean("L" + i + "_INCLUDED");
        putDouble("L" + i + "_AREA");
        putDouble("L" + i + "_BMC");
        putDouble("L" + i + "_BMD");
      }

      putDouble("TOT_AREA");
      putDouble("TOT_BMC");
      putDouble("TOT_BMD");
      putDouble("STD_TOT_BMD");
      putLong("ROI_TYPE");
      putDouble("ROI_WIDTH");
      putDouble("ROI_HEIGHT");
      putString("PHYSICIAN_COMMENT");
    }
  }

  @Override
  public Side getSide() {
    return null;
  }

}
