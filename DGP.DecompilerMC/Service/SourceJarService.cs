using DGP.DecompilerMC.Model;
using DGP.Decompiler.Helper.Download;
using System;
using System.Diagnostics;
using System.IO;

namespace DGP.DecompilerMC.Service
{
    internal class SourceJarService
    {
        private static IFileDownloader FileDownloadervar;
        internal static void GetSourceJarAsync(Side side)
        {
            string currentPath = Environment.CurrentDirectory;
            if (!Directory.Exists(currentPath + @"\jar"))
                Directory.CreateDirectory(currentPath + @"\jar");

            FileDownloadervar = new FileDownloader();

            FileDownloadervar.DownloadProgressChanged += (sender, e) => 
            {
                DownloadProgressChanged.Invoke(sender, e);
                Debug.WriteLine(e.ProgressPercentage);
            };

            FileDownloadervar.DownloadFileCompleted += (sender, e) =>
            {
                if (e.State == CompletedState.Succeeded)
                {
                    Debug.WriteLine("download mapping success");
                    DownloadCompleted.Invoke(sender, e);
                }
                else
                {
                    Debug.WriteLine("download mapping failed");
                }
            };
            VersionInfo versioninfo = MappingService.VersioninfoCache;
            if (side == Side.Client)
                FileDownloadervar.DownloadFileAsync(versioninfo.Downloads.Client.url, currentPath + @"\jar\source.jar");
            else
                FileDownloadervar.DownloadFileAsync(versioninfo.Downloads.Server.url, currentPath + @"\jar\source.jar");
        }
        internal static event EventHandler<DownloadFileProgressChangedArgs> DownloadProgressChanged;
        internal static event EventHandler<DownloadFileCompletedArgs> DownloadCompleted;
    }
}
