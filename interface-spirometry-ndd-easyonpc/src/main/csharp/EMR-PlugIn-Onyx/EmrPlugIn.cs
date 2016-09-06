using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Text;
using System.Windows.Forms;
using ndd.EMR.SharedConst;

namespace EMR.PlugIn.Onyx
{

  public class EmrPlugIn : InddEmrPlugIn
  {

    #region - PlugIn Information - static -

    /// <summary>
    /// Name shown in the EMR selection drop down list.</summary>
    /// <remarks>required</remarks>
    public static string Name
    {
      get { return Properties.Resources.PlugInName; }
    }
    #endregion

    #region - PlugIn Configuration - static -
    /// <summary>
    /// Control containing GUI elements to configure plugIn settings.</summary>
    /// <remarks>required when PlugIn is configurable</remarks>
    public static Control ConfigurationControl
    {
      get { return new UserControlEmrConfig(); }
    }

    #endregion


    #region -- Configuration Values --
    protected string _FileExchangeFolder = System.IO.Path.GetTempPath();
    private string m_XmlExchangeFile = "ExchangeFile_PlugIn-EasyWarePro.xml";


    public string XmlExchangeFile
    {
      get { return System.IO.Path.Combine(_FileExchangeFolder, m_XmlExchangeFile); }
    }

    public bool _UseCmdFile = false;

    public bool UseCmdFile
    {
      get
      {
        return _UseCmdFile && !String.IsNullOrEmpty(_CmdFileIn) && !String.IsNullOrEmpty(_CmdFileOut);
      }
      set
      {
        _UseCmdFile = value;
      }
    }

    private string _CmdFileIn;

    public string CmdFileIn
    {
      get { return _CmdFileIn; }
      set { _CmdFileIn = value; }
    }

    private string _CmdFileOut;

    public string CmdFileOut
    {
      get { return _CmdFileOut; }
      set { _CmdFileOut = value; }
    }

    private FileSystemWatcher _fileWatcher;

    private string _CfgAttachReport_String;
    private string _CfgAttachmentFormat;
    private string _CfgAttachmentFileName;

    private bool _CloseAfterTest;

    public bool CloseAfterTest
    {
      get { return _CloseAfterTest; }
      set { _CloseAfterTest = value; }
    }

    private bool _IncludeCurveData;

    public bool IncludeCurveData
    {
      get { return _IncludeCurveData; }
      set { _IncludeCurveData = value; }
    }

    private bool _IncludeTrialValues;

    public bool IncludeTrialValues
    {
      get { return _IncludeTrialValues; }
      set { _IncludeTrialValues = value; }
    }

    #endregion


    #region - Constructor, Initialization, Close -

    /// <summary>
    /// Constructor, parameterless</summary>
    ///<remarks>required: parametereless</remarks>
    public EmrPlugIn()
    {
    }

    /// <summary>
    /// Initialization and starting EMR Module</summary>
    /// <param name="configValues">saved configuration values, <see cref="UserControlEmrConfig"/></param>
    /// <returns>true if successfull</returns>
    /// <remarks>required</remarks>
    public virtual bool Initialize(Dictionary<string, string> configValues)
    {
      //load saved configuration values
      LoadingConfigValues(configValues);

      //example how you could use even use the command line
      UsingCommandLineParameters();

      if (UseCmdFile)
      {
        _fileWatcher = new FileSystemWatcher(_FileExchangeFolder);
        _fileWatcher.NotifyFilter = NotifyFilters.FileName;
        _fileWatcher.Filter = _CmdFileIn;
        _fileWatcher.IncludeSubdirectories = false;
        _fileWatcher.Created += new FileSystemEventHandler(fileWatcher_Created);
        //_fileWatcher.Changed += new FileSystemEventHandler(_fileWatcher_Changed);
        _fileWatcher.EnableRaisingEvents = true;

        string strFileIn = Path.Combine(_FileExchangeFolder, CmdFileIn);
        if (File.Exists(strFileIn))
        {
          SendMessage(strFileIn);
        }
      }

      return true;
    }


    private void UsingCommandLineParameters()
    {

      string[] args = Environment.GetCommandLineArgs();

      for (int i = 0; i < args.Length; i++)
      {
        switch (args[i])
        {
          case "/ShowTest":
            if (_SendXmlCallback != null)
            {
              //example command, of course Patient Data needs to be replaced,  e.g. as well transfered with command lines
              string strPatientID = "Patient X";
              _SendXmlCallback(@"
<ndd>
<Command Type=""ShowTest"">
        <Parameter Name=""PatientID"">" + strPatientID + @"</Parameter>
        <Parameter Name=""TestDate"">15.11.2010</Parameter>
    </Command>
<Patients>
    <Patient ID=""PSM-11213"">
      <LastName>Smith</LastName>
      <FirstName>Peter</FirstName></Patient>
</Patients>
</ndd>");
            }
            break;
          default:
            break;
        }
      }
    }

    protected virtual void LoadingConfigValues(Dictionary<string, string> configValues)
    {

      if (configValues.ContainsKey(UserConfig_Defs.ParamName_UseCmdFile))
      {
        bool bValue = false;
        if (Boolean.TryParse(configValues[UserConfig_Defs.ParamName_UseCmdFile], out bValue))
          _UseCmdFile = bValue;
      }

      if (configValues.ContainsKey(UserConfig_Defs.ParamName_CmdFileIn))
      {
        _CmdFileIn = configValues[UserConfig_Defs.ParamName_CmdFileIn];
      }

      if (configValues.ContainsKey(UserConfig_Defs.ParamName_CmdFileOut))
      {
        _CmdFileOut = configValues[UserConfig_Defs.ParamName_CmdFileOut];
      }

      if (configValues.ContainsKey(Commands.Configuration.FileExchangeFolder))
        _FileExchangeFolder = configValues[Commands.Configuration.FileExchangeFolder];

      if (configValues.ContainsKey(Commands.Configuration.IncludeCurveData))
      {
        bool bValue = false;
        if (Boolean.TryParse(configValues[Commands.Configuration.IncludeCurveData], out bValue))
          _IncludeCurveData = bValue;
      }

      if (configValues.ContainsKey(Commands.Configuration.IncludeTrialValues))
      {
        bool bValue = false;
        if (Boolean.TryParse(configValues[Commands.Configuration.IncludeTrialValues], out bValue))
          _IncludeTrialValues = bValue;
      }

      if (configValues.ContainsKey(Commands.Configuration.CloseAfterTest))
      {
        bool bValue = false;
        if (Boolean.TryParse(configValues[Commands.Configuration.CloseAfterTest], out bValue))
          _CloseAfterTest = bValue;
      }


      #region - Attachments -
      if (configValues.ContainsKey(Commands.Configuration.AttachReport))
      {
        _CfgAttachReport_String = configValues[Commands.Configuration.AttachReport];
      }

      if (configValues.ContainsKey(Commands.Configuration.AttachmentFormat))
      {
        _CfgAttachmentFormat = configValues[Commands.Configuration.AttachmentFormat];
      }

      if (configValues.ContainsKey(Commands.Configuration.AttachmentFileName))
      {
        _CfgAttachmentFileName = configValues[Commands.Configuration.AttachmentFileName];
      }
      #endregion
    }

    /// <summary>
    /// Stops the EMR module. You could call dispose() in case you have resources to free.</summary>
    /// <remarks>required</remarks>
    public virtual void Close()
    {
    }
    #endregion

    #region - handling sending data -


    public virtual string SendMessage(string strMessage)
    {
      if (_SendXmlCallback != null)
      {
        return _SendXmlCallback(strMessage);
      }
      return "";
    }


    /// <summary>
    /// This is an example how you could watch a folder and forward a command file to EasyWarePro when it is created.</summary>
    void fileWatcher_Created(object sender, FileSystemEventArgs e)
    {
      if ((e == null) || (e.FullPath == null))
        return;

      if (e.ChangeType == WatcherChangeTypes.Created)
      {
        //make sure you send only command files which are addressed for EasyWarePro
        //e.g. the name defined here could be an optional setting
        //..  if (e.Name == _CmdFileIn)

        _fileWatcher.EnableRaisingEvents = false;
         
        //wait other application to close the file
        System.Threading.Thread.Sleep(300);
        //check file still exists
        FileInfo objFileInfo = new FileInfo(e.FullPath);
        if (!objFileInfo.Exists) 
          return;    // ignore the file open
        
        SendMessage(e.FullPath);
        
        _fileWatcher.EnableRaisingEvents = true;
      }
    }

    #endregion


    #region - handling receiving data -

    /// <summary>
    /// Read XML file send by EasyWarePro.</summary>
    /// <param name="strPath">full path to the xml file</param>
    /// <returns><c>true</c>: successfull</returns>
    /// <remarks>required</remarks>
    protected string ReceiveXmlFile(string strPath)
    {
      // Create the reader.
      using (System.Xml.XmlReader reader = new System.Xml.XmlTextReader(strPath))
      {
        try
        {
          //Example of data processing handled within this PlugIn
          string strReturnMsg = ReceiveXmlMessage(reader);
          return strReturnMsg;
        }
        finally
        {
          reader.Close();

          if (UseCmdFile)
          {
            //example of data processing handled outside of this plugIn
            try
            {
              FileInfo fi = new FileInfo(strPath);

              if (fi.Exists)
              {
                fi.MoveTo(Path.Combine(_FileExchangeFolder, _CmdFileOut));
              }
            }
            catch { }
          }
          else
          {
            //someone has to delete the file
            //delete file
            try
            {
              if (File.Exists(strPath))
                File.Delete(strPath);
            }
            catch { }
          }
        }
      }
    }

    /// <summary>
    /// Read XML string send by EasyWarePro.</summary>
    /// <param name="strXmlMessage">message in xml format</param>
    /// <returns><c>true</c>: successfull</returns>
    /// <remarks>required</remarks>
    public string ReceiveXmlText(string strXmlMessage)
    {
      // Create the reader.
      System.Xml.XmlReader reader = System.Xml.XmlReader.Create(new System.IO.StringReader(strXmlMessage));
      string strResponse = ReceiveXmlMessage(reader);

      if (UseCmdFile) //if messages are handle outside, the above ReceiveXmlMessage() needs to be adapted
      {
        //write XML message to file
        using (StreamWriter sw = new StreamWriter(Path.Combine(_FileExchangeFolder, CmdFileOut), false, Encoding.Unicode))
        {
            sw.Write(strXmlMessage);
        }
      }

      return strResponse;
    }

    public virtual string ReceiveXmlMessage(string strMessage)
    {
      if (strMessage.IndexOf('<') >= 0)
        return ReceiveXmlText(strMessage);
      else
        return ReceiveXmlFile(strMessage);
    }

    protected string ReceiveXmlMessage(System.Xml.XmlReader reader)
    {
      string strCommand = "";

      if (reader.ReadToFollowing("Command"))
      //if (reader.Name == "Command")
      {
        strCommand = reader.GetAttribute("Type");
        Dictionary<string, string> parameterList = new Dictionary<string, string>();

        if (reader.ReadToDescendant("Parameter"))
        {
          do
          {
            string strParameterName = reader.GetAttribute("Name");
            string strParameterValue = reader.ReadElementString();

            parameterList.Add(strParameterName, strParameterValue);
          } while (reader.ReadToNextSibling("Parameter"));
        }

        switch (strCommand)
        {
          case Commands.Configuration.Command:
            //load saved configuration values
            LoadingConfigValues(parameterList);
            return "";

          case Commands.GetConfiguration.Command:
            return ReturnConfiguration();
          //break;

          case Commands.GetSupportedFeatures.Command: // "GetSupportedFeatures":
            //e.g. Worklist, CurveData, Attachment_Type, Attachment_Path
            return ReturnSupportedFeatures();
          //break;
          case Commands.SearchPatients.Command: // "SearchPatient":
            //list of patients
            return ReturnSearchPatientResult(parameterList);

          case Commands.TestResult.Command: // "TestResult":
            //Do nothing. Results are read from output file.
            return "";
          default:
            return @"
              <ndd>
                <Command Type=""Error"">
                  <Parameter Name=""Message"">Command not supported</Parameter>
                  <Parameter Name=""Details"">" + strCommand + @"</Parameter>
                </Command>
              </ndd>";
        }
      }

      while (reader.Read())
      {
        if (reader.Name == "Patients")
        {
          System.Xml.XmlReader patientReader = reader.ReadSubtree();
          while (patientReader.ReadToFollowing("Patient"))
          {
            if (patientReader.ReadAttributeValue() && patientReader.AttributeCount > 0)
            {
              string strPatientID = patientReader.GetAttribute("ID");
              string strPatientNone = patientReader.GetAttribute("NotAvailable");
            }

            while (patientReader.Read())
            {
              if (patientReader.Name == "FirstName")
              {
                string strName = patientReader.ReadString();
              }
            }

          }
          if (reader.ReadToFollowing("Patient"))
          {

          }
        }
      }

      Console.Beep();

      return "";
    }

    #endregion

    #region - returning data -

    protected OnEmrSendMessageDelegate _SendXmlCallback;

    public OnEmrSendMessageDelegate SendXmlCallback
    {
      set { _SendXmlCallback = value; }
    }

    protected virtual string ReturnConfiguration()
    {
      StringBuilder sb = new System.Text.StringBuilder();

      using (System.Xml.XmlTextWriter xmlWriter = new System.Xml.XmlTextWriter(new System.IO.StringWriter(sb, CultureInfo.InvariantCulture)))
      {
        xmlWriter.WriteStartDocument();
        xmlWriter.WriteStartElement("ndd");
        xmlWriter.WriteStartElement("Command");
        xmlWriter.WriteAttributeString("Type", Commands.Configuration.Command);

        //write configuration values (they could be fix)
        WriteConfiguration(xmlWriter);

        xmlWriter.WriteEndElement();
        xmlWriter.WriteEndElement();
        xmlWriter.WriteEndDocument();

        xmlWriter.Flush();
        xmlWriter.Close();

        return sb.ToString();
      }
    }

    protected virtual void WriteConfiguration(System.Xml.XmlTextWriter xmlWriter)
    {
      WriteParameter(xmlWriter, Commands.Configuration.CloseAfterTest, _CloseAfterTest.ToString(CultureInfo.InvariantCulture));
      WriteParameter(xmlWriter, Commands.Configuration.IncludeCurveData, _IncludeCurveData.ToString(CultureInfo.InvariantCulture));
      WriteParameter(xmlWriter, Commands.Configuration.IncludeTrialValues, _IncludeTrialValues.ToString(CultureInfo.InvariantCulture));
      WriteParameter(xmlWriter, Commands.Configuration.FileExchangeFolder, _FileExchangeFolder.ToString(CultureInfo.InvariantCulture));

      WriteParameter(xmlWriter, Commands.Configuration.AttachReport, _CfgAttachReport_String);
      WriteParameter(xmlWriter, Commands.Configuration.AttachmentFileName, _CfgAttachmentFileName);
      WriteParameter(xmlWriter, Commands.Configuration.AttachmentFormat, _CfgAttachmentFormat);
    }

    protected static void WriteParameter(System.Xml.XmlTextWriter xmlWriter, string strFeature, string strValue)
    {
      xmlWriter.WriteStartElement("Parameter");
      xmlWriter.WriteAttributeString("Name", strFeature);
      xmlWriter.WriteValue(strValue);
      xmlWriter.WriteEndElement();//parameter
    }

    protected virtual List<String> GetSupportedFeatures()
    {
      //add your supported features here
      return new List<string>();
    }

    //e.g. Worklist, CurveData, Attachment_Type, Attachment_Path
    protected virtual string ReturnSupportedFeatures()
    {
      //<?xml version="1.0" encoding="utf-16"?>
      //<ndd>
      //    <Command Type="SupportedFeatures">
      //        <Parameter Name="SearchPatients"></Parameter>
      //    </Command>
      //</ndd>

      StringBuilder sb = new System.Text.StringBuilder();

      using (System.Xml.XmlTextWriter xmlWriter = new System.Xml.XmlTextWriter(new System.IO.StringWriter(sb, CultureInfo.InvariantCulture)))
      {
        xmlWriter.WriteStartDocument();
        xmlWriter.WriteStartElement("ndd");
        xmlWriter.WriteStartElement("Command");
        xmlWriter.WriteAttributeString("Type", Commands.SupportedFeatures.Command);

        foreach (string strFeature in GetSupportedFeatures())
        {
          xmlWriter.WriteStartElement("Parameter");
          xmlWriter.WriteAttributeString("Name", strFeature);
          xmlWriter.WriteValue("True");
          xmlWriter.WriteEndElement();//parameter
        }

        xmlWriter.WriteEndElement();
        xmlWriter.WriteEndElement();
        xmlWriter.WriteEndDocument();
        //                xmlWriter.WriteString(@"
        //<ndd>
        //<command>Test xml data</command><Patients>
        //    <Patient ID=""PSM-11213"">
        //      <LastName>Smith</LastName>
        //      <FirstName>Peter</FirstName></Patient>
        //</Patients>
        //</ndd>");
        xmlWriter.Flush();
        xmlWriter.Close();
        return sb.ToString();
      }

    }


    //e.g. Worklist, CurveData, Attachment_Type, Attachment_Path
    protected virtual string ReturnSearchPatientResult(Dictionary<string, string> parameters)
    {
      using (System.Xml.XmlWriter xmlWriter = new System.Xml.XmlTextWriter(XmlExchangeFile, Encoding.UTF8))
      {
        try
        {
          xmlWriter.WriteStartDocument();
          xmlWriter.WriteStartElement("ndd");
          xmlWriter.WriteStartElement("Command");
          xmlWriter.WriteAttributeString("Type", Commands.SearchPatientsResult.Command);
          xmlWriter.WriteEndElement();//command
          xmlWriter.WriteStartElement("Patients");

          xmlWriter.WriteStartElement("Patient");
          xmlWriter.WriteAttributeString("ID", "SearchPatient-ID");
          xmlWriter.WriteElementString("LastName", "Search Last");
          xmlWriter.WriteElementString("FirstName", "Search First");

          xmlWriter.WriteStartElement("PatientDataAtPresent");

          xmlWriter.WriteElementString("DateOfBirth", "1956-07-28");
          xmlWriter.WriteElementString("Gender", "Female");
          xmlWriter.WriteElementString("Height", "1.82");
          xmlWriter.WriteElementString("Weight", "64");
          xmlWriter.WriteElementString("Ethnicity", "Caucasian");
          xmlWriter.WriteEndElement();//PatientDataAtPresent

          xmlWriter.WriteEndElement();//Patient
          xmlWriter.WriteEndElement();//Patients
          xmlWriter.WriteEndElement();
          xmlWriter.WriteEndDocument();
          //                xmlWriter.WriteString(@"
          //<ndd>
          //<command>Test xml data</command><Patients>
          //    <Patient ID=""PSM-11213"">
          //      <LastName>Smith</LastName>
          //      <FirstName>Peter</FirstName></Patient>
          //</Patients>
          //</ndd>");
          xmlWriter.Flush();
          xmlWriter.Close();
          return XmlExchangeFile;
        }
        finally
        {
          xmlWriter.Close();
        }
      }
    }
    #endregion
  }
}
