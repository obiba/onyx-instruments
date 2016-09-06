#region -- File Info --
//==============================================================================
// Copyright (c) by ndd Medizintechnik Zuerich, Switzerland.All rights reserved.
// -----------------------------------------------------------------------------
// Project: EasyWarePro EMR Plug in
// Content: shared definitions used to access the configuration form
// -----------------------------------------------------------------------------
// RCS Version Information:
// $HeadURL: svn://nddsrv02/software/Projects/PC/EasyWarePro/trunk/Software/EMR/EMR-BasicPlugIn/SharedConst.cs $
// $LastChangedBy: mzinniker $
// $Date: 2010-11-04 13:40:25 +0100 (Thu, 04 Nov 2010) $
// $Revision: 38533 $
//==============================================================================
#endregion

using System;
using System.Collections.Generic;

namespace EMR.PlugIn.Onyx
{
  /// <summary>
  /// A control handling the configuration entry has to implement the following methods.</summary>
  interface IEmrConfigControl
  {
    /// <summary>
    /// Load previous stored values onto control.</summary>
    /// <param name="configValues">list of key-value parameters stored in EasyWarePro</param>
    void SetGuiValues(System.Collections.Generic.Dictionary<string, string> configValues);

    /// <summary>
    /// Validates entries on the form.</summary>
    /// <returns><code>true</code> = values are valid</returns>
    bool ValidateEntries();

    /// <summary>
    /// Reads the values on the configuration form.</summary>
    /// <returns>list of key-value parameters</returns>
    Dictionary<string, string> GetGuiValues();
  }
}
