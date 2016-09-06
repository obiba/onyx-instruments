#region -- File Info --
//==============================================================================
// Copyright (c) by ndd Medizintechnik Zuerich, Switzerland.All rights reserved.
// -----------------------------------------------------------------------------
// Project: EasyWarePro EMR Plug in
// Content: shared definitions, constant declaration
// -----------------------------------------------------------------------------
// RCS Version Information:
// $HeadURL: svn://nddsrv02/software/Projects/PC/EasyWarePro/trunk/Software/EMR/EMR-PlugIn-Basic/SharedConst.cs $
// $LastChangedBy: mzinniker $
// $Date: 2010-12-02 04:05:50 -0500 (Do, 02 Dez 2010) $
// $Revision: 39011 $
//==============================================================================
#endregion

using System;
using System.Collections.Generic;
using System.Text;

//*********************************************************************
//* 
//* This definitions are shared with 3rd party EMR providers! 
//* Avoid changes and consider additions instead.
//*
//*********************************************************************
namespace ndd.EMR.SharedConst
{

  public static class PlugInNaming
  {
    public static string EmrInterfaceName                    = "InddEmrPlugIn";

    //static properties
    public static string EmrInterface_Name                   = "Name";
    public static string EmrInterface_CfgCtrl                = "ConfigurationControl";

    //methods
    public static string EmrInterface_SendXmlCallback        = "SendXmlCallback";
    public static string EmrInterface_ReceiveXmlMessage      = "ReceiveXmlMessage";
    public static string EmrInterface_Initialize             = "Initialize";
    public static string EmrInterface_Close                  = "Close";

    public static string EmrCfgCtrlInterface_SetGuiValues    = "SetGuiValues";
    public static string EmrCfgCtrlInterface_ValidateEntries = "ValidateEntries";
    public static string EmrCfgCtrlInterface_GetGuiValues    = "GetGuiValues";
  }

  /// <summary>
  /// Defines the commands ndd supports.</summary>
  public static class Commands
  {
    public static class GetConfiguration
    {
      public const string Command = "GetConfiguration";
    }

    public class Configuration
    {
      public const string Command = "Configuration";
      public const string FileExchangeFolder = "FileExchangeFolder";
      public const string CloseAfterTest   = "CloseAfterTest";
      public const string IncludeCurveData = "IncludeCurveData";
      public const string IncludeTrialValues = "IncludeTrialValues";
      public const string AttachReport     = "AttachReport";
      public const string AttachmentFormat = "AttachmentFormat";
      public const string AttachmentFileName = "AttachmentFileName";

      public const string AttachmentFileName_PlaceHolder_PatientID = "%PatientID%";
      public const string AttachmentFileName_PlaceHolder_Firstname = "%Firstname%";
      public const string AttachmentFileName_PlaceHolder_Lastname  = "%Lastname%";
      public const string AttachmentFileName_PlaceHolder_OrderID   = "%OrderID%";
      
    }

    public static class GetSupportedFeatures
    {
      public const string Command = "GetSupportedFeatures";
    }

    public class SupportedFeatures
    {
      public const string Command        = "SupportedFeatures";
      public const string Worklist       = "Worklist";
      public const string SearchPatients = "SearchPatients";
    }

    public static class SyncPatient
    {
      public const string Command = "SyncPatient";
    }

    public static class AddToWorklist
    {
      public const string Command = "AddToWorklist";
      public const string OrderID = "OrderID";
    }

    public static class PerformTest
    {
      public const string Command = "PerformTest";
      public const string OrderID = "OrderID";
      public const string TestType = "TestType";
    }

    public static class ShowTest
    {
      public const string Command = "ReviewTest";
      public const string OrderID = "OrderID";
      public const string DateTime = "DateTime";
    }

    public static class SearchPatients
    {
      public const string Command   = "SearchPatients";
      public const string FirstName = "FirstName";
      public const string LastName  = "LastName";
      public const string PatientID = "PatientID";
    }

    public static class SearchPatientsResult
    {
      public const string Command = "SearchPatientsResult";
    }

    #region -- not supported --
    ///// <summary>
    ///// Requests test results.</summary>
    //public static class GetTestResults
    //  {
    //  public const string Command = "GetTestResults";
    //  /// <summary>
    //  /// Unique patient identification -> returns all test of that patient. 
    //  /// Additional filter can be set with DateBegin-DateEnd.</summary>
    //  public const string PatientID = "PatientID";
    //  public const string OrderID = "OrderID";
    //  /// <summary>
    //  /// EasyWarePro specific test identification. e.g. “SN:DT 200076:633404223171210000”</summary>
    //  public const string TestIdRef = "TestIdRef";
    //  public const string DateBegin = "DateBegin";
    //  /// <summary>
    //  /// Returns all test which are performed between DateBegin and DateEnd.</summary>
    //  public const string DateEnd = "DateEnd";
    //}
    #endregion

    /// <summary>
    /// Returns test results (either when requested through GetTestResults or when Test has been performed).</summary>
    public static class TestResult
    {
      public const string Command = "TestResult";
      public const string Attachment = "Attachment";
    }

    /// <summary>
    /// Indicates that error occurred.</summary>
    public static class Error
    {
      public const string Command = "Error";
      /// <summary>
      /// Error message</summary>
      public const string Message = "Message";
      /// <summary>
      /// More detailed error message (e.g. technical)</summary>
      public const string Details = "Details";
    }
  }
}
