using Microsoft.Win32;
using System;
using System.IO;
using System.Windows;
using System.Windows.Resources;

namespace DGP.DecompilerMC.Helper
{
    internal class FileHelper
    {
        internal static void ClearWorkFiles()
        {
            Directory.Delete(WorkingFolder.JarFolder);
            Directory.Delete(WorkingFolder.LibFolder);
            Directory.Delete(WorkingFolder.MappingFolder);
        }

        internal static void LoadSpecialSourceLib()
        {
            using (FileStream fs = new FileStream(WorkingFile.SpecialSource, FileMode.Create))
            {
                StreamResourceInfo resource = Application.GetResourceStream(new Uri(@"pack://application:,,,/DGP.DecompilerMC;component/Resources/SpecialSource.jar", UriKind.Absolute));
                resource.Stream.CopyTo(fs);
            }
        }
        internal static void LoadCFRLib()
        {
            using (FileStream fs = new FileStream(WorkingFile.CFR, FileMode.Create))
            {
                StreamResourceInfo resource = Application.GetResourceStream(new Uri(@"pack://application:,,,/DGP.DecompilerMC;component/Resources/cfr.jar", UriKind.Absolute));
                resource.Stream.CopyTo(fs);
            }
        }
    }
    internal class WorkingFolder
    {
        private static readonly string appPath = Environment.CurrentDirectory;
        internal static string MappingFolder 
        {
            get
            {
                if (!Directory.Exists(appPath + @"\mapping"))
                    Directory.CreateDirectory(appPath + @"\mapping");
                return appPath + @"\mapping";
            }
        }
        internal static string LibFolder
        {
            get
            {
                if (!Directory.Exists(appPath + @"\lib"))
                    Directory.CreateDirectory(appPath + @"\lib");
                return appPath + @"\lib";
            }
        }
        internal static string JarFolder
        {
            get
            {
                if (!Directory.Exists(appPath + @"\jar"))
                    Directory.CreateDirectory(appPath + @"\jar");
                return appPath + @"\jar";
            }
        }
        internal static string JavaDirectory
        {
            get
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
    internal class WorkingFile
    {
        internal static string Java
        {
            get
            {
                return WorkingFolder.JavaDirectory + @"\bin\java.exe";
            }
        }
        internal static string MappingInfoTxt
        {
            get
            {
                return WorkingFolder.MappingFolder + @"\mapinfo.txt";
            }
        }
        internal static string MappingInfoTsrg
        {
            get
            {
                return WorkingFolder.MappingFolder + @"\mapinfo.tsrg";
            }
        }
        /// <summary>
        /// delete SpecialSource when get,cause we can't trust jar file if it was replaced by user.
        /// </summary>
        internal static string SpecialSource
        {
            get
            {
                string result = WorkingFolder.LibFolder + @"\SpecialSource.jar";
                return result;
            }
        }
        /// <summary>
        /// delete CFR when get,cause we can't trust jar file if it was replaced by user.
        /// </summary>
        internal static string CFR
        {
            get
            {
                string result = WorkingFolder.LibFolder + @"\cfr.jar";
                return result;
            }
        }
        internal static string SourceJar
        {
            get
            {
                return WorkingFolder.JarFolder + @"\source.jar";
            }
        }
        internal static string RemappedJar
        {
            get
            {
                return WorkingFolder.JarFolder + @"\remapped.jar";
            }
        }
    }

}
