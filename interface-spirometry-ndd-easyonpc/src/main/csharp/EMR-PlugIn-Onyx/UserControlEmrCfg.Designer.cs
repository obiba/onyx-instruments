namespace EMR.PlugIn.Onyx
{
    partial class UserControlEmrConfig
    {
        /// <summary> 
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.tabControlConfig = new System.Windows.Forms.TabControl();
            this.tabPageExchange = new System.Windows.Forms.TabPage();
            this.checkBoxIncludeValues = new System.Windows.Forms.CheckBox();
            this.panelUseExternalCmdFile = new System.Windows.Forms.Panel();
            this.textBoxCmdFileIn = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.textBoxCmdFileOut = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.checkBoxUseCmdFile = new System.Windows.Forms.CheckBox();
            this.checkBoxIncludeCurve = new System.Windows.Forms.CheckBox();
            this.label3 = new System.Windows.Forms.Label();
            this.textBoxExchangeFolder = new System.Windows.Forms.TextBox();
            this.tabPageWorkflow = new System.Windows.Forms.TabPage();
            this.checkBoxCloseAfterTest = new System.Windows.Forms.CheckBox();
            this.errorProviderValidation = new System.Windows.Forms.ErrorProvider(this.components);
            this.tabAttachment = new System.Windows.Forms.TabPage();
            this.checkBoxAttachPDF = new System.Windows.Forms.CheckBox();
            this.label15 = new System.Windows.Forms.Label();
            this.comboBoxFormat = new System.Windows.Forms.ComboBox();
            this.tabControlConfig.SuspendLayout();
            this.tabPageExchange.SuspendLayout();
            this.panelUseExternalCmdFile.SuspendLayout();
            this.tabPageWorkflow.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.errorProviderValidation)).BeginInit();
            this.tabAttachment.SuspendLayout();
            this.SuspendLayout();
            // 
            // tabControlConfig
            // 
            this.tabControlConfig.Controls.Add(this.tabPageExchange);
            this.tabControlConfig.Controls.Add(this.tabPageWorkflow);
            this.tabControlConfig.Controls.Add(this.tabAttachment);
            this.tabControlConfig.Location = new System.Drawing.Point(0, 0);
            this.tabControlConfig.Name = "tabControlConfig";
            this.tabControlConfig.SelectedIndex = 0;
            this.tabControlConfig.Size = new System.Drawing.Size(314, 262);
            this.tabControlConfig.TabIndex = 3;
            // 
            // tabPageExchange
            // 
            this.tabPageExchange.Controls.Add(this.checkBoxIncludeValues);
            this.tabPageExchange.Controls.Add(this.panelUseExternalCmdFile);
            this.tabPageExchange.Controls.Add(this.checkBoxUseCmdFile);
            this.tabPageExchange.Controls.Add(this.checkBoxIncludeCurve);
            this.tabPageExchange.Controls.Add(this.label3);
            this.tabPageExchange.Controls.Add(this.textBoxExchangeFolder);
            this.tabPageExchange.Location = new System.Drawing.Point(4, 22);
            this.tabPageExchange.Name = "tabPageExchange";
            this.tabPageExchange.Padding = new System.Windows.Forms.Padding(3);
            this.tabPageExchange.Size = new System.Drawing.Size(306, 236);
            this.tabPageExchange.TabIndex = 2;
            this.tabPageExchange.Text = "Exchange";
            this.tabPageExchange.UseVisualStyleBackColor = true;
            // 
            // checkBoxIncludeValues
            // 
            this.checkBoxIncludeValues.AutoSize = true;
            this.checkBoxIncludeValues.Checked = true;
            this.checkBoxIncludeValues.CheckState = System.Windows.Forms.CheckState.Checked;
            this.checkBoxIncludeValues.Location = new System.Drawing.Point(17, 210);
            this.checkBoxIncludeValues.Name = "checkBoxIncludeValues";
            this.checkBoxIncludeValues.Size = new System.Drawing.Size(149, 17);
            this.checkBoxIncludeValues.TabIndex = 9;
            this.checkBoxIncludeValues.Text = "include trial values in XML";
            this.checkBoxIncludeValues.UseVisualStyleBackColor = true;
            // 
            // panelUseExternalCmdFile
            // 
            this.panelUseExternalCmdFile.BackColor = System.Drawing.Color.WhiteSmoke;
            this.panelUseExternalCmdFile.Controls.Add(this.textBoxCmdFileIn);
            this.panelUseExternalCmdFile.Controls.Add(this.label1);
            this.panelUseExternalCmdFile.Controls.Add(this.textBoxCmdFileOut);
            this.panelUseExternalCmdFile.Controls.Add(this.label2);
            this.panelUseExternalCmdFile.Location = new System.Drawing.Point(17, 78);
            this.panelUseExternalCmdFile.Name = "panelUseExternalCmdFile";
            this.panelUseExternalCmdFile.Size = new System.Drawing.Size(268, 103);
            this.panelUseExternalCmdFile.TabIndex = 8;
            // 
            // textBoxCmdFileIn
            // 
            this.textBoxCmdFileIn.Location = new System.Drawing.Point(16, 32);
            this.textBoxCmdFileIn.Name = "textBoxCmdFileIn";
            this.textBoxCmdFileIn.Size = new System.Drawing.Size(226, 20);
            this.textBoxCmdFileIn.TabIndex = 4;
            this.textBoxCmdFileIn.Text = "OnyxIn.xml";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(13, 16);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(61, 13);
            this.label1.TabIndex = 6;
            this.label1.Text = "Filename In";
            // 
            // textBoxCmdFileOut
            // 
            this.textBoxCmdFileOut.Location = new System.Drawing.Point(16, 71);
            this.textBoxCmdFileOut.Name = "textBoxCmdFileOut";
            this.textBoxCmdFileOut.Size = new System.Drawing.Size(226, 20);
            this.textBoxCmdFileOut.TabIndex = 5;
            this.textBoxCmdFileOut.Text = "OnyxOut.xml";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(13, 55);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(69, 13);
            this.label2.TabIndex = 7;
            this.label2.Text = "Filename Out";
            // 
            // checkBoxUseCmdFile
            // 
            this.checkBoxUseCmdFile.AutoSize = true;
            this.checkBoxUseCmdFile.Checked = true;
            this.checkBoxUseCmdFile.CheckState = System.Windows.Forms.CheckState.Checked;
            this.checkBoxUseCmdFile.Location = new System.Drawing.Point(17, 55);
            this.checkBoxUseCmdFile.Name = "checkBoxUseCmdFile";
            this.checkBoxUseCmdFile.Size = new System.Drawing.Size(126, 17);
            this.checkBoxUseCmdFile.TabIndex = 3;
            this.checkBoxUseCmdFile.Text = "use external Cmd File";
            this.checkBoxUseCmdFile.UseVisualStyleBackColor = true;
            this.checkBoxUseCmdFile.CheckedChanged += new System.EventHandler(this.checkBoxUseCmdFile_CheckedChanged);
            // 
            // checkBoxIncludeCurve
            // 
            this.checkBoxIncludeCurve.AutoSize = true;
            this.checkBoxIncludeCurve.Checked = true;
            this.checkBoxIncludeCurve.CheckState = System.Windows.Forms.CheckState.Checked;
            this.checkBoxIncludeCurve.Location = new System.Drawing.Point(17, 187);
            this.checkBoxIncludeCurve.Name = "checkBoxIncludeCurve";
            this.checkBoxIncludeCurve.Size = new System.Drawing.Size(150, 17);
            this.checkBoxIncludeCurve.TabIndex = 3;
            this.checkBoxIncludeCurve.Text = "include curve data in XML";
            this.checkBoxIncludeCurve.UseVisualStyleBackColor = true;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(14, 10);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(109, 13);
            this.label3.TabIndex = 2;
            this.label3.Text = "Data exchange folder";
            // 
            // textBoxExchangeFolder
            // 
            this.textBoxExchangeFolder.Location = new System.Drawing.Point(17, 29);
            this.textBoxExchangeFolder.Name = "textBoxExchangeFolder";
            this.textBoxExchangeFolder.Size = new System.Drawing.Size(245, 20);
            this.textBoxExchangeFolder.TabIndex = 1;
            this.textBoxExchangeFolder.Text = "C:\\Program Files\\ndd Medizintechnik\\Easy on-PC\\Database";
            // 
            // tabPageWorkflow
            // 
            this.tabPageWorkflow.Controls.Add(this.checkBoxCloseAfterTest);
            this.tabPageWorkflow.Location = new System.Drawing.Point(4, 22);
            this.tabPageWorkflow.Name = "tabPageWorkflow";
            this.tabPageWorkflow.Padding = new System.Windows.Forms.Padding(3);
            this.tabPageWorkflow.Size = new System.Drawing.Size(306, 236);
            this.tabPageWorkflow.TabIndex = 1;
            this.tabPageWorkflow.Text = "Workflow";
            this.tabPageWorkflow.UseVisualStyleBackColor = true;
            // 
            // checkBoxCloseAfterTest
            // 
            this.checkBoxCloseAfterTest.AutoSize = true;
            this.checkBoxCloseAfterTest.Checked = true;
            this.checkBoxCloseAfterTest.CheckState = System.Windows.Forms.CheckState.Checked;
            this.checkBoxCloseAfterTest.Location = new System.Drawing.Point(16, 17);
            this.checkBoxCloseAfterTest.Name = "checkBoxCloseAfterTest";
            this.checkBoxCloseAfterTest.Size = new System.Drawing.Size(224, 17);
            this.checkBoxCloseAfterTest.TabIndex = 2;
            this.checkBoxCloseAfterTest.Text = "close application after test (return to EMR)";
            this.checkBoxCloseAfterTest.UseVisualStyleBackColor = true;
            // 
            // errorProviderValidation
            // 
            this.errorProviderValidation.ContainerControl = this;
            // 
            // tabAttachment
            // 
            this.tabAttachment.Controls.Add(this.label15);
            this.tabAttachment.Controls.Add(this.comboBoxFormat);
            this.tabAttachment.Controls.Add(this.checkBoxAttachPDF);
            this.tabAttachment.Location = new System.Drawing.Point(4, 22);
            this.tabAttachment.Name = "tabAttachment";
            this.tabAttachment.Padding = new System.Windows.Forms.Padding(3);
            this.tabAttachment.Size = new System.Drawing.Size(306, 236);
            this.tabAttachment.TabIndex = 3;
            this.tabAttachment.Text = "Attachment";
            this.tabAttachment.UseVisualStyleBackColor = true;
            // 
            // checkBoxAttachPDF
            // 
            this.checkBoxAttachPDF.AutoSize = true;
            this.checkBoxAttachPDF.Checked = true;
            this.checkBoxAttachPDF.CheckState = System.Windows.Forms.CheckState.Checked;
            this.checkBoxAttachPDF.Location = new System.Drawing.Point(19, 73);
            this.checkBoxAttachPDF.Name = "checkBoxAttachPDF";
            this.checkBoxAttachPDF.Size = new System.Drawing.Size(87, 17);
            this.checkBoxAttachPDF.TabIndex = 14;
            this.checkBoxAttachPDF.Text = "Attach report";
            this.checkBoxAttachPDF.UseVisualStyleBackColor = true;
            // 
            // label15
            // 
            this.label15.AutoSize = true;
            this.label15.Location = new System.Drawing.Point(16, 102);
            this.label15.Name = "label15";
            this.label15.Size = new System.Drawing.Size(93, 13);
            this.label15.TabIndex = 19;
            this.label15.Text = "Attachment format";
            // 
            // comboBoxFormat
            // 
            this.comboBoxFormat.FormattingEnabled = true;
            this.comboBoxFormat.Items.AddRange(new object[] {
            "PDF",
            "CSV",
            "TXT",
            "RTF",
            "XLS",
            "PNG",
            "GIF",
            "TIF",
            "JPG",
            "MHT"});
            this.comboBoxFormat.Location = new System.Drawing.Point(19, 121);
            this.comboBoxFormat.Name = "comboBoxFormat";
            this.comboBoxFormat.Size = new System.Drawing.Size(121, 21);
            this.comboBoxFormat.TabIndex = 20;
            this.comboBoxFormat.Text = "PDF";
            // 
            // UserControlEmrConfig
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSize = true;
            this.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.Controls.Add(this.tabControlConfig);
            this.Name = "UserControlEmrConfig";
            this.Size = new System.Drawing.Size(317, 265);
            this.tabControlConfig.ResumeLayout(false);
            this.tabPageExchange.ResumeLayout(false);
            this.tabPageExchange.PerformLayout();
            this.panelUseExternalCmdFile.ResumeLayout(false);
            this.panelUseExternalCmdFile.PerformLayout();
            this.tabPageWorkflow.ResumeLayout(false);
            this.tabPageWorkflow.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.errorProviderValidation)).EndInit();
            this.tabAttachment.ResumeLayout(false);
            this.tabAttachment.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TabControl tabControlConfig;
        private System.Windows.Forms.TabPage tabPageWorkflow;
        private System.Windows.Forms.ErrorProvider errorProviderValidation;
      private System.Windows.Forms.TabPage tabPageExchange;
      private System.Windows.Forms.Label label3;
      private System.Windows.Forms.TextBox textBoxExchangeFolder;
      private System.Windows.Forms.CheckBox checkBoxCloseAfterTest;
      private System.Windows.Forms.CheckBox checkBoxIncludeCurve;
      private System.Windows.Forms.Panel panelUseExternalCmdFile;
      private System.Windows.Forms.TextBox textBoxCmdFileIn;
      private System.Windows.Forms.Label label1;
      private System.Windows.Forms.TextBox textBoxCmdFileOut;
      private System.Windows.Forms.Label label2;
      private System.Windows.Forms.CheckBox checkBoxUseCmdFile;
      private System.Windows.Forms.CheckBox checkBoxIncludeValues;
        private System.Windows.Forms.TabPage tabAttachment;
        private System.Windows.Forms.CheckBox checkBoxAttachPDF;
        private System.Windows.Forms.Label label15;
        private System.Windows.Forms.ComboBox comboBoxFormat;
    }
}
