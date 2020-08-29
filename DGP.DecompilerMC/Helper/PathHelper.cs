using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DGP.DecompilerMC.Helper
{
    internal class PathHelper
    {
        internal static string GetJavaInstallationDirectory()
        {
            string javaKey = "SOFTWARE\\JavaSoft\\Java Runtime Environment";
            using (var baseKey = RegistryKey.OpenBaseKey(RegistryHive.LocalMachine, RegistryView.Registry64).OpenSubKey(javaKey))
            {
                string currentVersion = baseKey.GetValue("CurrentVersion").ToString();
                using (var homeKey = baseKey.OpenSubKey(currentVersion))
                    return homeKey.GetValue("JavaHome").ToString();
            }
        }


    }
}
