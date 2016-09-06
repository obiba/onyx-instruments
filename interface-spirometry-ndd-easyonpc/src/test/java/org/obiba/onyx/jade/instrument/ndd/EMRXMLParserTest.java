/*******************************************************************************
 * Copyright (c) 2011 OBiBa. All rights reserved.
 *  
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.jade.instrument.ndd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.junit.Test;
import org.obiba.onyx.jade.instrument.ndd.FVCDataExtractor.FVCData;
import org.obiba.onyx.jade.instrument.ndd.FVCDataExtractor.FVCTrialData;
import org.xml.sax.SAXException;

/**
 *
 */
public class EMRXMLParserTest {

  private static final SimpleDateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  @Test
  public void testParse() throws URISyntaxException, XPathExpressionException, FileNotFoundException, IOException, ParserConfigurationException, SAXException {
    EMRXMLParser<FVCData> parser = new EMRXMLParser<FVCData>();

    File file = new File(getClass().getResource("/export.xml").toURI());

    parser.parse(new FileInputStream(file), new FVCDataExtractor());


    CommandData commandData = parser.getCommandData();
    Assert.assertEquals("TestResult", commandData.getType());
    Assert.assertArrayEquals(new String[] {"c:\\tmp\\output.pdf"}, commandData.getParameters().values().toArray());
    Assert.assertArrayEquals(new String[] {"Attachment"}, commandData.getParameters().keySet().toArray());

    ParticipantData pData = parser.getParticipantData();
    Assert.assertNotNull(pData);
    Assert.assertEquals("CLSA-2", pData.getIdentifier());
    Assert.assertEquals("Patate", pData.getLastName());
    Assert.assertEquals("Madame", pData.getFirstName());
    Assert.assertEquals(new Double(1.7), pData.getHeight());
    Assert.assertEquals(new Double(70), pData.getWeight());
    Assert.assertEquals("Caucasian", pData.getEthnicity());
    Assert.assertEquals("", pData.getAsthma());
    Assert.assertEquals("", pData.getSmoker());
    Assert.assertEquals("", pData.getCopd());
    Assert.assertEquals("Female", pData.getGender());
    Assert.assertEquals("1972-01-27", pData.getDateOfBirth());

    FVCData tData = parser.getTestData();
    Assert.assertEquals("FVC", tData.getType());
    Assert.assertEquals("2011-07-27T11:14:55.281", ISO_8601.format(tData.getDate()));
    Assert.assertEquals("D1", tData.getQualityGrade());

    Map<String, Number> results = tData.getBestResults();
    Assert.assertEquals(65, results.size());
    Assert.assertTrue(results.containsKey("FEF10"));
    Assert.assertTrue(results.containsKey("FEF2575_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV_75"));
    Assert.assertTrue(results.containsKey("FEF40"));
    Assert.assertTrue(results.containsKey("FEF2575"));
    Assert.assertTrue(results.containsKey("FET2575"));
    Assert.assertTrue(results.containsKey("MMEF_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV3_VCmax"));
    Assert.assertTrue(results.containsKey("FEF2575_6"));
    Assert.assertTrue(results.containsKey("FEV1_VCmax"));
    Assert.assertTrue(results.containsKey("FET"));
    Assert.assertTrue(results.containsKey("MEF25"));
    Assert.assertTrue(results.containsKey("FEV1_FEV6_PRED"));
    Assert.assertTrue(results.containsKey("PEF_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV_75_FEV6"));
    Assert.assertTrue(results.containsKey("MEF20"));
    Assert.assertTrue(results.containsKey("FEV1_FVC"));
    Assert.assertTrue(results.containsKey("MTC1"));
    Assert.assertTrue(results.containsKey("MTC3"));
    Assert.assertTrue(results.containsKey("MTC2"));
    Assert.assertTrue(results.containsKey("MEF60"));
    Assert.assertTrue(results.containsKey("FEV1_FEV6_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV_25"));
    Assert.assertTrue(results.containsKey("PEF_PRED"));
    Assert.assertTrue(results.containsKey("VCmax"));
    Assert.assertTrue(results.containsKey("EOTV"));
    Assert.assertTrue(results.containsKey("FEF75"));
    Assert.assertTrue(results.containsKey("FVC_PRED"));
    Assert.assertTrue(results.containsKey("MEF90"));
    Assert.assertTrue(results.containsKey("FEF25"));
    Assert.assertTrue(results.containsKey("FEV_75_VCmax"));
    Assert.assertTrue(results.containsKey("FEF80"));
    Assert.assertTrue(results.containsKey("FEF50_VCmax"));
    Assert.assertTrue(results.containsKey("FEF2575_FVC"));
    Assert.assertTrue(results.containsKey("MEF50"));
    Assert.assertTrue(results.containsKey("FVC_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV6_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV_5"));
    Assert.assertTrue(results.containsKey("FEF60"));
    Assert.assertTrue(results.containsKey("PEF"));
    Assert.assertTrue(results.containsKey("MMEF_PRED"));
    Assert.assertTrue(results.containsKey("FEV_5_FVC"));
    Assert.assertTrue(results.containsKey("FEV_75_FVC"));
    Assert.assertTrue(results.containsKey("FEV1_LLNORMAL"));
    Assert.assertTrue(results.containsKey("MMEF"));
    Assert.assertTrue(results.containsKey("FEV3_FVC"));
    Assert.assertTrue(results.containsKey("PEF_L_Min_PRED"));
    Assert.assertTrue(results.containsKey("FVC"));
    Assert.assertTrue(results.containsKey("MEF40"));
    Assert.assertTrue(results.containsKey("FEF50"));
    Assert.assertTrue(results.containsKey("PEF_L_Min"));
    Assert.assertTrue(results.containsKey("FEF50_FVC"));
    Assert.assertTrue(results.containsKey("MEF75"));
    Assert.assertTrue(results.containsKey("FEV6_PRED"));
    Assert.assertTrue(results.containsKey("PEF_L_Min_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV1_PRED"));
    Assert.assertTrue(results.containsKey("FEV3"));
    Assert.assertTrue(results.containsKey("FEV6"));
    Assert.assertTrue(results.containsKey("FEV1"));
    Assert.assertTrue(results.containsKey("FEV1_FVC_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEF2575_PRED"));
    Assert.assertTrue(results.containsKey("FEF7585"));
    Assert.assertTrue(results.containsKey("FEV1_FVC_PRED"));
    Assert.assertTrue(results.containsKey("FEV1_FEV6"));
    Assert.assertTrue(results.containsKey("MTCR"));

    Assert.assertEquals(2, tData.getTrials().size());
    Assert.assertEquals("2011-07-27T11:15:14.937", ISO_8601.format(tData.getTrials().get(0).getDate()));
    Assert.assertEquals(new Long(1), tData.getTrials().get(0).getRank());

    results = tData.getTrials().get(0).getResults();
    Assert.assertEquals(76, results.size());
    Assert.assertTrue(results.containsKey("FEF10"));
    Assert.assertTrue(results.containsKey("FEF2575_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV_75"));
    Assert.assertTrue(results.containsKey("FEF40"));
    Assert.assertTrue(results.containsKey("FEF2575"));
    Assert.assertTrue(results.containsKey("FET2575"));
    Assert.assertTrue(results.containsKey("BTPSex"));
    Assert.assertTrue(results.containsKey("MsgNo"));
    Assert.assertTrue(results.containsKey("AmbTemp"));
    Assert.assertTrue(results.containsKey("MMEF_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV3_VCmax"));
    Assert.assertTrue(results.containsKey("FEV1_VCmax"));
    Assert.assertTrue(results.containsKey("FEF2575_6"));
    Assert.assertTrue(results.containsKey("FET"));
    Assert.assertTrue(results.containsKey("AmbHumidity"));
    Assert.assertTrue(results.containsKey("MEF25"));
    Assert.assertTrue(results.containsKey("FEV1_FEV6_PRED"));
    Assert.assertTrue(results.containsKey("FEV_75_FEV6"));
    Assert.assertTrue(results.containsKey("PEF_LLNORMAL"));
    Assert.assertTrue(results.containsKey("AmbTemp_Fahr"));
    Assert.assertTrue(results.containsKey("MEF20"));
    Assert.assertTrue(results.containsKey("FEV1_FVC"));
    Assert.assertTrue(results.containsKey("MTC1"));
    Assert.assertTrue(results.containsKey("MTC3"));
    Assert.assertTrue(results.containsKey("MTC2"));
    Assert.assertTrue(results.containsKey("MEF60"));
    Assert.assertTrue(results.containsKey("FEV1_FEV6_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV_25"));
    Assert.assertTrue(results.containsKey("PEF_PRED"));
    Assert.assertTrue(results.containsKey("VCmax"));
    Assert.assertTrue(results.containsKey("EOTV"));
    Assert.assertTrue(results.containsKey("FEF75"));
    Assert.assertTrue(results.containsKey("FVC_PRED"));
    Assert.assertTrue(results.containsKey("MEF90"));
    Assert.assertTrue(results.containsKey("FEF25"));
    Assert.assertTrue(results.containsKey("FEV_75_VCmax"));
    Assert.assertTrue(results.containsKey("FEF80"));
    Assert.assertTrue(results.containsKey("FEF50_VCmax"));
    Assert.assertTrue(results.containsKey("FEF2575_FVC"));
    Assert.assertTrue(results.containsKey("MEF50"));
    Assert.assertTrue(results.containsKey("PEFT"));
    Assert.assertTrue(results.containsKey("FVC_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV6_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV_5"));
    Assert.assertTrue(results.containsKey("FEF60"));
    Assert.assertTrue(results.containsKey("PEF"));
    Assert.assertTrue(results.containsKey("MMEF_PRED"));
    Assert.assertTrue(results.containsKey("FEV_5_FVC"));
    Assert.assertTrue(results.containsKey("FEV_75_FVC"));
    Assert.assertTrue(results.containsKey("FEV1_LLNORMAL"));
    Assert.assertTrue(results.containsKey("MMEF"));
    Assert.assertTrue(results.containsKey("FEV3_FVC"));
    Assert.assertTrue(results.containsKey("PEF_L_Min_PRED"));
    Assert.assertTrue(results.containsKey("FVC"));
    Assert.assertTrue(results.containsKey("MEF40"));
    Assert.assertTrue(results.containsKey("ATI"));
    Assert.assertTrue(results.containsKey("FEF50"));
    Assert.assertTrue(results.containsKey("PEF_L_Min"));
    Assert.assertTrue(results.containsKey("BEV"));
    Assert.assertTrue(results.containsKey("FEF50_FVC"));
    Assert.assertTrue(results.containsKey("MEF75"));
    Assert.assertTrue(results.containsKey("FEV6_PRED"));
    Assert.assertTrue(results.containsKey("PEF_L_Min_LLNORMAL"));
    Assert.assertTrue(results.containsKey("T0"));
    Assert.assertTrue(results.containsKey("FEV1_PRED"));
    Assert.assertTrue(results.containsKey("FEV3"));
    Assert.assertTrue(results.containsKey("FEV6"));
    Assert.assertTrue(results.containsKey("BTPSin"));
    Assert.assertTrue(results.containsKey("FEV1_FVC_LLNORMAL"));
    Assert.assertTrue(results.containsKey("FEV1"));
    Assert.assertTrue(results.containsKey("FEF2575_PRED"));
    Assert.assertTrue(results.containsKey("FEF7585"));
    Assert.assertTrue(results.containsKey("FEV1_FVC_PRED"));
    Assert.assertTrue(results.containsKey("FEV1_FEV6"));
    Assert.assertTrue(results.containsKey("AmbPressure"));
    Assert.assertTrue(results.containsKey("MTCR"));
    Assert.assertEquals(results.get("FEF10"), new Double(5.382199605305989));
    Assert.assertEquals(results.get("FEF2575_LLNORMAL"), new Double(1.9849999556317925));
    Assert.assertEquals(results.get("FEV_75"), new Double(2.821692571043968));
    Assert.assertEquals(results.get("FEF40"), new Double(3.9711998303731284));
    Assert.assertEquals(results.get("FEF2575"), new Double(3.1108149834749033));
    Assert.assertEquals(results.get("FET2575"), new Double(0.6441120517187892));
    Assert.assertEquals(results.get("BTPSex"), new Double(1.0199999809265137));
    Assert.assertEquals(results.get("MsgNo"), new Double(0.0));
    Assert.assertEquals(results.get("AmbTemp"), new Double(20.0));
    Assert.assertEquals(results.get("MMEF_LLNORMAL"), new Double(1.9849999556317925));
    Assert.assertEquals(results.get("FEV3_VCmax"), new Double(1.0));
    Assert.assertEquals(results.get("FEV1_VCmax"), new Double(0.819819908659104));
    Assert.assertEquals(results.get("FEF2575_6"), new Double(3.1108149834749033));
    Assert.assertEquals(results.get("FET"), new Double(2.5882336659566123));
    Assert.assertEquals(results.get("AmbHumidity"), new Double(50.0));
    Assert.assertEquals(results.get("MEF25"), new Double(1.9413999319076538));
    Assert.assertEquals(results.get("FEV1_FEV6_PRED"), new Double(0.8400000398978591));
    Assert.assertEquals(results.get("FEV_75_FEV6"), new Double(0.7041158033713293));
    Assert.assertEquals(results.get("PEF_LLNORMAL"), new Double(5.5712498754728585));
    Assert.assertEquals(results.get("AmbTemp_Fahr"), new Double(68.0));
    Assert.assertEquals(results.get("MEF20"), new Double(1.6387999455134075));
    Assert.assertEquals(results.get("FEV1_FVC"), new Double(0.819819908659104));
    Assert.assertEquals(results.get("MTC1"), new Double(-1.337117326872295));
    Assert.assertEquals(results.get("MTC3"), new Double(4.727422558410455));
    Assert.assertEquals(results.get("MTC2"), new Double(-1.4525032374471443));
    Assert.assertEquals(results.get("MEF60"), new Double(3.9711998303731284));
    Assert.assertEquals(results.get("FEV1_FEV6_LLNORMAL"), new Double(0.7520000357180834));
    Assert.assertEquals(results.get("FEV_25"), new Double(1.265164002776146));
    Assert.assertEquals(results.get("PEF_PRED"), new Double(7.442499833647162));
    Assert.assertEquals(results.get("VCmax"), new Double(4.007426843047142));
    Assert.assertEquals(results.get("EOTV"), new Double(0.05033707618713379));
    Assert.assertEquals(results.get("FEF75"), new Double(1.9413999319076538));
    Assert.assertEquals(results.get("FVC_PRED"), new Double(4.073749908944592));
    Assert.assertEquals(results.get("MEF90"), new Double(5.382199605305989));
    Assert.assertEquals(results.get("FEF25"), new Double(4.736200014750163));
    Assert.assertEquals(results.get("FEV_75_VCmax"), new Double(0.7041158033713293));
    Assert.assertEquals(results.get("FEF80"), new Double(1.6387999455134075));
    Assert.assertEquals(results.get("FEF50_VCmax"), new Double(0.8187298302408277));
    Assert.assertEquals(results.get("FEF2575_FVC"), new Double(0.7762624510219434));
    Assert.assertEquals(results.get("MEF50"), new Double(3.2809998989105225));
    Assert.assertEquals(results.get("PEFT"), new Double(0.07323366595661242));
    Assert.assertEquals(results.get("FVC_LLNORMAL"), new Double(3.3174999258480966));
    Assert.assertEquals(results.get("FEV6_LLNORMAL"), new Double(3.2587499271612614));
    Assert.assertEquals(results.get("FEV_5"), new Double(2.18519888818264));
    Assert.assertEquals(results.get("FEF60"), new Double(2.6553998788197837));
    Assert.assertEquals(results.get("PEF"), new Double(5.39579963684082));
    Assert.assertEquals(results.get("MMEF_PRED"), new Double(3.3374999254010618));
    Assert.assertEquals(results.get("FEV_5_FVC"), new Double(0.5452872812822386));
    Assert.assertEquals(results.get("FEV_75_FVC"), new Double(0.7041158033713293));
    Assert.assertEquals(results.get("FEV1_LLNORMAL"), new Double(2.679999940097332));
    Assert.assertEquals(results.get("MMEF"), new Double(3.1108149834749033));
    Assert.assertEquals(results.get("FEV3_FVC"), new Double(1.0));
    Assert.assertEquals(results.get("PEF_L_Min_PRED"), new Double(446.5499900188297));
    Assert.assertEquals(results.get("FVC"), new Double(4.007426843047142));
    Assert.assertEquals(results.get("MEF40"), new Double(2.6553998788197837));
    Assert.assertEquals(results.get("ATI"), new Double(0.0));
    Assert.assertEquals(results.get("FEF50"), new Double(3.2809998989105225));
    Assert.assertEquals(results.get("PEF_L_Min"), new Double(323.7479782104492));
    Assert.assertEquals(results.get("BEV"), new Double(0.08262976258993149));
    Assert.assertEquals(results.get("FEF50_FVC"), new Double(0.8187298302408277));
    Assert.assertEquals(results.get("MEF75"), new Double(4.736200014750163));
    Assert.assertEquals(results.get("FEV6_PRED"), new Double(4.000624910579063));
    Assert.assertEquals(results.get("PEF_L_Min_LLNORMAL"), new Double(334.2749925283715));
    Assert.assertEquals(results.get("T0"), new Double(0.1117663340433876));
    Assert.assertEquals(results.get("FEV1_PRED"), new Double(3.319374925806187));
    Assert.assertEquals(results.get("FEV3"), new Double(4.007426843047142));
    Assert.assertEquals(results.get("FEV6"), new Double(4.007426843047142));
    Assert.assertEquals(results.get("BTPSin"), new Double(1.1184662580490112));
    Assert.assertEquals(results.get("FEV1_FVC_LLNORMAL"), new Double(0.7270000345306471));
    Assert.assertEquals(results.get("FEV1"), new Double(3.2853683084249496));
    Assert.assertEquals(results.get("FEF2575_PRED"), new Double(3.3374999254010618));
    Assert.assertEquals(results.get("FEF7585"), new Double(1.641889894752948));
    Assert.assertEquals(results.get("FEV1_FVC_PRED"), new Double(0.8250000391853973));
    Assert.assertEquals(results.get("FEV1_FEV6"), new Double(0.819819908659104));
    Assert.assertEquals(results.get("AmbPressure"), new Double(965.8563232421875));
    Assert.assertEquals(results.get("MTCR"), new Double(-0.28284277750747255));

    results = tData.getTrials().get(1).getResults();
    Assert.assertEquals(new Long(3), tData.getTrials().get(1).getRank());
    Assert.assertEquals(76, results.size());

    FVCTrialData trialData = tData.getTrials().get(0);
    Assert.assertEquals(new Double(0.01), trialData.getFlowInterval());
    Assert.assertEquals("-0.011184662580490112 0.061199996620416641 0.12239999324083328 0.19379998743534088 0.26519998908042908 0.34679999947547913 0.44879999756813049 0.60179996490478516 0.826200008392334 1.121999979019165 1.4585999250411987 1.8563998937606812 2.3153998851776123 2.8457999229431152 3.4271998405456543 4.0289998054504395 4.5797996520996094 5.028599739074707 5.3039999008178711 5.39579963684082 5.3651995658874512 5.3141999244689941 5.293799877166748 5.293799877166748 5.2427997589111328 5.2019996643066406 5.2019996643066406 5.2121996879577637 5.1815996170043945 5.0693998336791992 4.8959994316101074 4.7328000068664551 4.6205997467041016 4.5389995574951172 4.4777998924255371 4.4267997741699219 4.3757996559143066 4.3350000381469727 4.2941999435424805 4.2737998962402344 4.2533998489379883 4.2329998016357422 4.202399730682373 4.1513996124267578 4.0697999000549316 3.9677999019622803 3.886199951171875 3.8249998092651367 3.7637996673583984 3.6821997165679932 3.5699999332427979 3.4577999114990234 3.3863999843597412 3.335399866104126 3.315000057220459 3.3048000335693359 3.28439998626709 3.2435996532440186 3.2027997970581055 3.1823997497558594 3.1721997261047363 3.1415998935699463 3.09060001373291 3.0293998718261719 2.9987998008728027 2.9885997772216797 2.9579997062683105 2.8763999938964844 2.76419997215271 2.6723997592926025 2.6417999267578125 2.6315999031066895 2.6009998321533203 2.5499999523162842 2.4785997867584229 2.4173998832702637 2.3765997886657715 2.3561999797821045 2.3561999797821045 2.3664000034332275 2.3459999561309814 2.3153998851776123 2.2746000289916992 2.2541999816894531 2.24399995803833 2.223599910736084 2.2133998870849609 2.2031998634338379 2.2031998634338379 2.2133998870849609 2.223599910736084 2.2133998870849609 2.1827998161315918 2.1113998889923096 2.0399999618530273 1.9685999155044556 1.9175999164581299 1.8665999174118042 1.8359999656677246 1.8053998947143555 1.795199990272522 1.7747999429702759 1.7441998720169067 1.7135999202728272 1.6931999921798706 1.6931999921798706 1.6829999685287476 1.6421999931335449 1.5911998748779297 1.5401999950408936 1.509600043296814 1.509600043296814 1.5299999713897705 1.5503998994827271 1.56059992313385 1.56059992313385 1.5401999950408936 1.5197999477386475 1.4891998767852783 1.4483999013900757 1.4075999259948731 1.3769999742507935 1.3463999032974243 1.3055999279022217 1.264799952507019 1.23419988155365 1.2137999534606934 1.1933999061584473 1.1627999544143677 1.121999979019165 1.0811998844146729 1.0607999563217163 1.0403999090194702 1.0097999572753906 0.968999981880188 0.92819994688034058 0.88739997148513794 0.86699992418289185 0.85679996013641357 0.86699992418289185 0.87719994783401489 0.87719994783401489 0.85679996013641357 0.83639997243881226 0.81599992513656616 0.79559993743896484 0.78539997339248657 0.77519994974136353 0.77519994974136353 0.76499998569488525 0.73439997434616089 0.71399998664855957 0.69359999895095825 0.67319995164871216 0.66299998760223389 0.65279996395111084 0.64259999990463257 0.63239997625350952 0.63239997625350952 0.65279996395111084 0.65279996395111084 0.65279996395111084 0.65279996395111084 0.65279996395111084 0.64259999990463257 0.61199992895126343 0.60179996490478516 0.59160000085830688 0.58139997720718384 0.56099998950958252 0.53039997816085815 0.49979996681213379 0.46919995546340942 0.44879999756813049 0.41819998621940613 0.38759997487068176 0.34679999947547913 0.31619998812675476 0.29580000042915344 0.27539998292922974 0.25499999523162842 0.24479998648166657 0.23459997773170471 0.22439999878406525 0.22439999878406525 0.22439999878406525 0.21419999003410339 0.20399998128414154 0.19379998743534088 0.19379998743534088 0.19379998743534088 0.19379998743534088 0.19379998743534088 0.19379998743534088 0.20399998128414154 0.20399998128414154 0.20399998128414154 0.19379998743534088 0.18359999358654022 0.18359999358654022 0.18359999358654022 0.18359999358654022 0.17339999973773956 0.16319999098777771 0.17339999973773956 0.19379998743534088 0.20399998128414154 0.21419999003410339 0.21419999003410339 0.22439999878406525 0.22439999878406525 0.23459997773170471 0.23459997773170471 0.23459997773170471 0.23459997773170471 0.22439999878406525 0.21419999003410339 0.20399998128414154 0.18359999358654022 0.15299998223781586 0.12239999324083328 0.091799996793270111 0.0714000016450882 0.040799997746944427 0.020399998873472214 0.010199999436736107 0.010199999436736107 0.030599998310208321 0.050999995321035385 0.081599995493888855 0.11219999939203262 0.14280000329017639 0.16319999098777771 0.17339999973773956 0.18359999358654022 0.18359999358654022 0.18359999358654022 0.19379998743534088 0.19379998743534088 0.19379998743534088 0.17339999973773956 0.14280000329017639 0.11219999939203262 0.091799996793270111 0.081599995493888855 0.081599995493888855 0.091799996793270111 0.091799996793270111 0.091799996793270111 0.10199999064207077 0.11219999939203262 0.11219999939203262 0.11219999939203262 0.10199999064207077 0.10199999064207077 0.10199999064207077 0.091799996793270111 0.091799996793270111 0.091799996793270111 0.10199999064207077 0.11219999939203262 0.11219999939203262 0.11219999939203262 0.11219999939203262 0.10199999064207077 0.081599995493888855 0.0714000016450882 0.050999995321035385 0.040799997746944427 0.030599998310208321 0.010199999436736107 0 -0.022369325160980225 -0.033553987741470337 -0.044738650321960449 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.067107975482940674 -0.0894773006439209 -0.11184661835432053 -0.13421595096588135 -0.14540061354637146 -0.15658527612686157 -0.16776992380619049 -0.15658527612686157 -0.14540061354637146 -0.14540061354637146 -0.13421595096588135 -0.12303128838539124 -0.12303128838539124 -0.11184661835432053 -0.11184661835432053 -0.11184661835432053 -0.11184661835432053 -0.11184661835432053 -0.11184661835432053 -0.11184661835432053 -0.10066195577383041 -0.10066195577383041 -0.10066195577383041 -0.0894773006439209 -0.0894773006439209 -0.078292638063430786 -0.078292638063430786 -0.078292638063430786 -0.078292638063430786 -0.067107975482940674 -0.067107975482940674 -0.044738650321960449 -0.044738650321960449 -0.033553987741470337 -0.044738650321960449 -0.044738650321960449 -0.044738650321960449 -0.044738650321960449 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.022369325160980225 -0.022369325160980225 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.044738650321960449 -0.044738650321960449 -0.044738650321960449 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.044738650321960449 -0.044738650321960449 -0.044738650321960449 -0.044738650321960449 -0.044738650321960449 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.055923309177160263 -0.044738650321960449 -0.044738650321960449 -0.044738650321960449 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.044738650321960449 -0.044738650321960449 -0.044738650321960449 -0.044738650321960449 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.022369325160980225 -0.022369325160980225 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.033553987741470337 -0.022369325160980225 -0.022369325160980225 -0.022369325160980225 -0.022369325160980225 -0.022369325160980225 -0.022369325160980225 -0.022369325160980225 -0.033553987741470337 -0.033553987741470337 -0.022369325160980225 -0.022369325160980225 -0.022369325160980225 -0.022369325160980225 -0.022369325160980225 -0.022369325160980225", new String(trialData.getFlowValues()));
    Assert.assertEquals(new Double(0.01), trialData.getVolumeInterval());
    Assert.assertEquals("-0.00010199844837188721 0.00030599534511566162 0.0013770014047622681 0.003161996603012085 0.0056099891662597656 0.0088739991188049316 0.013107001781463623 0.018716990947723389 0.02636699378490448 0.036821991205215454 0.050540991127491 0.068084992468357086 0.0900149941444397 0.11709599196910858 0.14988899230957031 0.18869999051094055 0.2331719845533371 0.28243798017501831 0.33491697907447815 0.38877296447753906 0.44252696633338928 0.49577099084854126 0.548708975315094 0.60164695978164673 0.65422797203063965 0.70634996891021729 0.75831902027130127 0.81044101715087891 0.86235892772674561 0.91335892677307129 0.96272695064544678 1.0104119777679443 1.0568729639053345 1.102415919303894 1.1472959518432617 1.1917169094085693 1.2356278896331787 1.2790799140930176 1.3221238851547241 1.3649128675460815 1.4074978828430176 1.4498789310455322 1.4920048713684082 1.5336718559265137 1.5745738744735718 1.6145069599151611 1.6535729169845581 1.6919759511947632 1.729766845703125 1.766792893409729 1.8027478456497192 1.8375809192657471 1.8715978860855103 1.9050538539886475 1.9382548332214356 1.9713538885116577 2.0042488574981689 2.0367870330810547 2.0689170360565186 2.1007919311523438 2.1325139999389648 2.164031982421875 2.1950910091400146 2.2255380153656006 2.2555770874023438 2.2854630947113037 2.3151450157165527 2.3441638946533203 2.3721120357513428 2.3990399837493896 2.425508975982666 2.4518249034881592 2.4779369831085205 2.5035898685455322 2.5285799503326416 2.5529069900512695 2.576775074005127 2.6003880500793457 2.6239500045776367 2.6475629806518555 2.6710739135742188 2.6943299770355225 2.7171778678894043 2.7397708892822266 2.7622618675231934 2.7845489978790283 2.80668306350708 2.8287150859832764 2.8507468700408936 2.8728809356689453 2.8951170444488525 2.9173018932342529 2.9392318725585938 2.96055006980896 2.9811539649963379 3.0010440349578857 3.0203218460083008 3.0390899181365967 3.0575010776519775 3.0756058692932129 3.0935580730438232 3.1113569736480713 3.128849983215332 3.1460368633270264 3.1630198955535889 3.1799519062042236 3.1967818737030029 3.2133059501647949 3.2293200492858887 3.2448239326477051 3.2599709033966064 3.2750160694122314 3.2902140617370605 3.3056669235229492 3.3212728500366211 3.3368790149688721 3.3523318767547607 3.3675808906555176 3.3825750350952148 3.3971610069274902 3.4113390445709229 3.4252109527587891 3.4387259483337402 3.4518840312957764 3.4646339416503906 3.4770779609680176 3.4892668724060059 3.5012519359588623 3.51298189163208 3.5243039131164551 3.5352180004119873 3.5458769798278809 3.5563318729400635 3.5664808750152588 3.5762729644775391 3.5856568813323975 3.5946328639984131 3.6033539772033691 3.6119730472564697 3.6206429004669189 3.629364013671875 3.6381359100341797 3.6467549800872803 3.65516996383667 3.6633808612823486 3.6713879108428955 3.6792418956756592 3.6869938373565674 3.6947460174560547 3.7024469375610352 3.7098929882049561 3.7170839309692383 3.7240710258483887 3.7308540344238281 3.7374839782714844 3.7440629005432129 3.7504889965057373 3.7568130493164062 3.7631368637084961 3.7696139812469482 3.776141881942749 3.7826700210571289 3.7891979217529297 3.7957258224487305 3.8022029399871826 3.8084249496459961 3.8144938945770264 3.8204100131988525 3.8262238502502441 3.8318848609924316 3.8372399806976318 3.8422889709472656 3.847031831741333 3.8515708446502686 3.8558039665222168 3.8597819805145264 3.8633518218994141 3.8665649890899658 3.8695738315582275 3.8723788261413574 3.8749799728393555 3.8774278163909912 3.8797738552093506 3.8820688724517822 3.8843128681182861 3.88655686378479 3.8887498378753662 3.890841007232666 3.8928298950195312 3.8947679996490479 3.8967058658599854 3.898643970489502 3.9005818367004395 3.9025199413299561 3.9045088291168213 3.9065489768981934 3.9085888862609863 3.9105269908905029 3.912362813949585 3.9141988754272461 3.9160349369049072 3.9178709983825684 3.9196560382843018 3.9213390350341797 3.9230220317840576 3.9249088764190674 3.9268980026245117 3.92903995513916 3.9311819076538086 3.9334259033203125 3.9356698989868164 3.9380159378051758 3.9403619766235352 3.9427080154418945 3.9450538158416748 3.9472978115081787 3.949491024017334 3.9515819549560547 3.9534690380096436 3.9551010131835938 3.9564268589019775 3.957446813583374 3.9582118988037109 3.9586708545684814 3.95892596244812 3.9590280055999756 3.959129810333252 3.9593849182128906 3.9598438739776611 3.960608959197998 3.9616799354553223 3.9630570411682129 3.9646379947662354 3.966372013092041 3.968207836151123 3.9700438976287842 3.9718799591064453 3.9738178253173828 3.9757559299468994 3.977694034576416 3.9794788360595703 3.9810090065002441 3.9821820259094238 3.9831509590148926 3.9839668273925781 3.9847829341888428 3.9857008457183838 3.9866189956665039 3.9875369071960449 3.9885568618774414 3.9896788597106934 3.9908008575439453 3.9919228553771973 3.9929938316345215 3.9940140247344971 3.9950339794158936 3.9959518909454346 3.9968700408935547 3.9977879524230957 3.9987568855285645 3.9998278617858887 4.0009498596191406 4.0020718574523926 4.0031938552856445 4.004213809967041 4.0050806999206543 4.0057950019836426 4.0063557624816895 4.0068149566650391 4.0071206092834473 4.0072736740112305 4.0073246955871582 4.0071568489074707 4.0068774223327637 4.0064859390258789 4.0059266090393066 4.0053672790527344 4.00480842590332 4.004249095916748 4.003577709197998 4.0027389526367188 4.0016765594482422 4.0003905296325684 3.9989924430847168 3.9974265098571777 3.9958047866821289 3.99418306350708 3.9926729202270508 3.9912190437316895 3.9898767471313477 3.9885907173156738 3.9873602390289307 3.9862418174743652 3.9851233959198 3.9840049743652344 3.9828865528106689 3.9817678928375244 3.980649471282959 3.9795310497283936 3.9785244464874268 3.97751784324646 3.9765112400054932 3.9755604267120361 3.974665641784668 3.9738826751708984 3.973099946975708 3.9723169803619385 3.9715340137481689 3.970862865447998 3.9701919555664062 3.9696884155273438 3.9692411422729492 3.9689056873321533 3.9684581756591797 3.9680109024047852 3.9675633907318115 3.967116117477417 3.9667246341705322 3.9663889408111572 3.9660534858703613 3.9658298492431641 3.9656062126159668 3.9653265476226807 3.9649910926818848 3.9646553993225098 3.9643199443817139 3.9639842510223389 3.963648796081543 3.9632573127746582 3.9628100395202637 3.96236252784729 3.9620270729064941 3.9616916179656982 3.9613559246063232 3.9610204696655273 3.9606847763061523 3.9603493213653564 3.9600138664245605 3.9596781730651855 3.959230899810791 3.9587833881378174 3.9583361148834229 3.9578886032104492 3.9574413299560547 3.9569380283355713 3.956378698348999 3.9558196067810059 3.9552602767944336 3.9547011852264404 3.9541418552398682 3.9535825252532959 3.9530234336853027 3.9524641036987305 3.9519050121307373 3.951345682144165 3.9508984088897705 3.9504508972167969 3.9500036239624023 3.9496121406555176 3.9492764472961426 3.9489409923553467 3.9486055374145508 3.9482698440551758 3.94793438911438 3.947598934173584 3.947263240814209 3.9469277858734131 3.9465920925140381 3.9461448192596436 3.945697546005249 3.9452500343322754 3.9448027610778809 3.9444670677185059 3.94413161277771 3.9437961578369141 3.9435164928436279 3.9432926177978516 3.9430131912231445 3.9426774978637695 3.9423420429229736 3.9420065879821777 3.9416708946228027 3.9413354396820068 3.9409999847412109 3.9407761096954346 3.9405524730682373 3.94032883644104 3.9401051998138428 3.9398813247680664 3.9396576881408691 3.9394340515136719 3.9391543865203857 3.93881893157959 3.9385392665863037 3.9383156299591064 3.93809175491333 3.9378681182861328 3.9376444816589355 3.9374208450317383", new String(trialData.getVolumeValues()));
  }
}
