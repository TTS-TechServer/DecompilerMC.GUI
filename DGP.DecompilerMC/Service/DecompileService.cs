using DGP.DecompilerMC.Helper;
using DGP.DecompilerMC.Model;
using DGP.Decompiler.Helper.Download;
using System;
using System.Diagnostics;
using System.IO;

namespace DGP.DecompilerMC.Service
{
    class DecompileService
    {
        private static Model.Version Version { get; set; }
        private static Side Side { get; set; }

        internal static async void ExcuteDecompileAysnc(Model.Version version,Side side)
        {
            Version = version;
            Side = side;
            FileHelper.LoadSpecialSourceLib();
            FileHelper.LoadCFRLib();
            MappingService.MappingDownloadCompleted += MappingService_MappingDownloadCompleted;
            MappingService.MappingDownloadProgressChanged += MappingService_MappingDownloadProgressChanged;
            await MappingService.GetMappingAsync(version,side);
        }

        private static void MappingService_MappingDownloadProgressChanged(object sender,DownloadFileProgressChangedArgs e)
        {
            MainWindow.Current.Dispatcher.Invoke(() => {
                MainWindow.Current.Progress.Value = e.ProgressPercentage;
                MainWindow.Current.DialogInfoText.Text = e.ProgressPercentage + "%";
            });
            
        }
        private static async void MappingService_MappingDownloadCompleted(object sender, DownloadFileCompletedArgs e)
        {
            MappingService.ConvertCompleted += MappingService_ConvertCompleted;

            MainWindow.Current.DialogTitle.Text = "正在转换文件格式";
            MainWindow.Current.DialogInfoText.Text = "";
            MainWindow.Current.Progress.IsIndeterminate = true;
            await MappingService.ConvertMapping2Tsrg();
        }


        private static void MappingService_ConvertCompleted(object sender, EventArgs e)
        {
            
            MainWindow.Current.Dispatcher.Invoke(() => 
            {
                MainWindow.Current.Progress.IsIndeterminate = false;
                MainWindow.Current.DialogTitle.Text = "正在下载源Jar文件"; 
            });
            SourceJarService.DownloadCompleted += SourceJarService_DownloadCompleted;
            SourceJarService.DownloadProgressChanged += SourceJarService_DownloadProgressChanged;
            SourceJarService.GetSourceJarAsync(Side);
        }


        private static void SourceJarService_DownloadProgressChanged(object sender, DownloadFileProgressChangedArgs e)
        {
            MainWindow.Current.Dispatcher.Invoke(() =>
            {
                MainWindow.Current.Progress.Value = e.ProgressPercentage;
                MainWindow.Current.DialogInfoText.Text = e.ProgressPercentage + "%";
            });
        }
        private static void SourceJarService_DownloadCompleted(object sender, DownloadFileCompletedArgs e)
        {
            RemapService.OutputDataReceived += RemapService_OutputDataReceived;
            RemapService.ProcessExited += RemapService_ProcessExited;
            RemapService.Remap();
        }


        private static void RemapService_OutputDataReceived(object sender, DataReceivedEventArgs e)
        {
            if(e.Data is object)//null check
            {
                if (e.Data.Contains("Loading mappings"))
                {
                    string temp = e.Data.Replace("Loading mappings", "");
                    if (string.IsNullOrEmpty(temp))//firstline
                    {
                        MainWindow.Current.Dispatcher.Invoke(() =>
                        {
                            MainWindow.Current.DialogTitle.Text = "正在加载映射文件";
                        });
                    }
                    else
                    {
                        MainWindow.Current.Dispatcher.Invoke(() =>
                        {
                            MainWindow.Current.Progress.Value =
                                int.Parse(temp.Replace(".", "").Trim().Replace("%", ""));
                            MainWindow.Current.DialogInfoText.Text = temp.Replace(".", "").Trim();
                        });
                    }
                }
                else if (e.Data.Contains("Remapping jar..."))
                {
                    string temp = e.Data.Replace("Remapping jar...", "");
                    MainWindow.Current.Dispatcher.Invoke(() =>
                    {
                        MainWindow.Current.Progress.Value =
                            int.Parse(temp.Replace(" ", "").Replace("%", ""));
                        MainWindow.Current.DialogInfoText.Text = temp.Trim();
                    });
                }
                else//Remapping final jar
                    MainWindow.Current.Dispatcher.Invoke(() =>
                    {
                        MainWindow.Current.DialogTitle.Text = "正在重映射Jar文件";
                    });
            }
        }
        private static void RemapService_ProcessExited(object sender, EventArgs e)
        {
            if((sender as Process).ExitCode==0)
            {
                MainWindow.Current.Dispatcher.Invoke(() =>
                {
                    MainWindow.Current.DialogTitle.Text = "正在反编译Jar";
                    MainWindow.Current.Progress.IsIndeterminate = true;
                    MainWindow.Current.DialogInfoText.Text = "这可能需要一段时间";
                });
                DecompileWithCFR();
            }
            else
            {
                // error
            }

        }
        private static async void DecompileWithCFR()
        {
            string currentPath = Environment.CurrentDirectory;
            string cfr = currentPath + @"\lib\cfr.jar";
            string remappedJar = currentPath + @"\jar\remapped.jar";
            string outputdir = currentPath + $@"\src\{Version.Id}";

            Process javaProcess = new Process();
            javaProcess.StartInfo.FileName = PathHelper.GetJavaInstallationDirectory() + @"\bin\java.exe";
            javaProcess.StartInfo.Arguments
                = $" -Xmx2G -Xms1G -jar {cfr} {remappedJar} --outputdir {outputdir} --caseinsensitivefs true --silent true";
            javaProcess.StartInfo.UseShellExecute = false;
            javaProcess.StartInfo.RedirectStandardOutput = true;
            javaProcess.StartInfo.CreateNoWindow = true;
            //cfr output is not readable
            //javaProcess.OutputDataReceived += CFRProcess_OutputDataReceived;
            javaProcess.Start();
            //javaProcess.BeginOutputReadLine();
            javaProcess.WaitForExit();

            File.Delete(outputdir + @"\summary.txt");
            Process.Start("explorer.exe", outputdir);
            
            await MainWindow.Current.Dispatcher.Invoke(async () =>
            {
                MainWindow.Current.ProcessDialog.Visibility = System.Windows.Visibility.Hidden;
                await MainWindow.Current.InfoDialog.ShowAsync();
            });
        }
    }
}
