using DGP.DecompilerMC.Helper;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DGP.DecompilerMC.Service
{
    class RemapService
    {
        public static void Remap()
        {
            Process javaProcess = new Process();
            javaProcess.StartInfo.FileName = WorkingFile.Java;
            javaProcess.StartInfo.Arguments= string.Join("",
                $" -jar {WorkingFile.SpecialSource}", 
                $" --in-jar {WorkingFile.SourceJar}",
                $" --out-jar {WorkingFile.RemappedJar}", 
                $" --srg-in {WorkingFile.MappingInfoTsrg}",
                " --kill-lvt --progress-interval 1");
                //= $" -jar {WorkingFile.SpecialSource} --in-jar {WorkingFile.SourceJar} --out-jar {WorkingFile.RemappedJar} --srg-in {WorkingFile.MappingInfoTsrg} --kill-lvt --progress-interval 1";
            javaProcess.StartInfo.UseShellExecute = false;
            javaProcess.StartInfo.RedirectStandardOutput = true;
            javaProcess.StartInfo.CreateNoWindow = true;
            javaProcess.OutputDataReceived += (sender,e)=> { OutputDataReceived.Invoke(sender, e); };
            javaProcess.Start();
            javaProcess.BeginOutputReadLine();
            javaProcess.WaitForExit();
            ProcessExited.Invoke(javaProcess, new EventArgs());
            javaProcess.Dispose();
        }
        internal static event DataReceivedEventHandler OutputDataReceived;
        internal static event EventHandler ProcessExited;
    }
}
