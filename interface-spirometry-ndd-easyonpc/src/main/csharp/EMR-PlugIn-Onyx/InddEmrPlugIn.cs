using System;
namespace EMR.PlugIn.Onyx
{
    public delegate string OnEmrSendMessageDelegate(string strMessage);

    /// <summary>
    /// Interface which identifies EMR-PlugIn. Every PlugIn must implement the the elements of this interface.</summary>
    /// <remarks>required: elements are identified by their names. Don't rename it!</remarks>
    public interface InddEmrPlugIn
    {
        //-- static Info/Configuration --
        //static string Name { get; }                                                         //module name
        //static Control ConfigurationControl                                                 //required when Plug in is configurable

        //-- Start/Stop --
        //Constructor()                                                                       //without parameter
        bool Initialize(System.Collections.Generic.Dictionary<string, string> configValues);  //start EMR module
        void Close();                                                                         //stop EMR module

        //- receiving data -
        string ReceiveXmlMessage(string strXmlMessage);                                            //handle EasyWarePro messages: xml message (assumption starting with '<' -> xml string, else path to xml file)

        //- sending data -
        OnEmrSendMessageDelegate SendXmlCallback { set; }                           //callback to return data as xml string or file
    }
}
