using DGP.DecompilerMC.Helper;
using DGP.DecompilerMC.Model;
using DGP.Decompiler.Helper.Download;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace DGP.DecompilerMC.Service
{
    internal class MappingService
    {
        private static IFileDownloader FileDownloadervar;
        public static VersionInfo VersioninfoCache;

        internal static async Task GetMappingAsync(Model.Version version,Side side)
        {

			FileDownloadervar = new FileDownloader();

            FileDownloadervar.DownloadProgressChanged += (sender, e) => 
			{ 
                MappingDownloadProgressChanged.Invoke(sender, e); 
                Debug.WriteLine(e.ProgressPercentage); 
            };
			FileDownloadervar.DownloadFileCompleted += (sender, e) =>
			{
				if (e.State == CompletedState.Succeeded)
				{
					Debug.WriteLine("download mapping success");
					MappingDownloadCompleted.Invoke(sender, e);
				}
				else
					Debug.WriteLine("download mapping failed");
			};

            VersionInfo versioninfo = await Json.GetWebRequestObjectAsync<VersionInfo>(version.JsonUrl);
            VersioninfoCache = versioninfo;

            if (side == Side.Client)
                FileDownloadervar.DownloadFileAsync(versioninfo.Downloads.ClientMappings.url, WorkingFile.MappingInfoTxt);
            else
                FileDownloadervar.DownloadFileAsync(versioninfo.Downloads.ServerMappings.url, WorkingFile.MappingInfoTxt);
        }

        internal static event EventHandler<DownloadFileProgressChangedArgs> MappingDownloadProgressChanged;
        internal static event EventHandler<DownloadFileCompletedArgs> MappingDownloadCompleted;
        #region Convert
        private static readonly Dictionary<string, string> classMap = new Dictionary<string, string>();
		private static string TypeToDescriptor(string type)
		{
			if (type.EndsWith("[]", StringComparison.Ordinal))
			{
				return "[" + TypeToDescriptor(type.Substring(0, type.Length - 2));
			}
			if (type.Contains("."))
			{
				if (classMap.TryGetValue(type.Replace(".", "/"), out string result))
					return "L" + result + ";";
				return "L" + type.Replace(".", "/") + ";";
			}

			switch (type)
			{
				case "void":
					return "V";
				case "int":
					return "I";
				case "float":
					return "F";
				case "char":
					return "C";
				case "byte":
					return "B";
				case "boolean":
					return "Z";
				case "double":
					return "D";
				case "long":
					return "J";
				case "short":
					return "S";
				default:
					return "";
			}
		}
		private static void LoadClasses(Stream mojangMapSource)
		{
			StreamReader buf = new StreamReader(mojangMapSource, Encoding.UTF8);

			bool loop = true;
			while (loop)
			{
				string s = buf.ReadLine();
				if (s is object && s.Length > 0)
				{
					if (s.StartsWith("#", StringComparison.Ordinal))
					{
						continue;
					}

					if (s.StartsWith(" ", StringComparison.Ordinal)) // We only care about lines mapping classes.
					{
						continue;
					}
					else
					{ // Read the class name into the map.
						string[] parts = s.Split(new string[]{" "},StringSplitOptions.RemoveEmptyEntries);
						Debug.Assert(parts.Length == 3);

						string className = parts[0].Replace(".", "/");
						string obfName = parts[2].Substring(0, parts[2].Length - 1);
						if (obfName.Contains("."))
						{
							obfName = obfName.Replace(".", "/");
						}

						classMap[className] = obfName;
					}
				}
				else
				{
					loop = false;
				}
			}
			buf.Close();
		}
		private static void WriteTsrg(Stream mojangMapSource, FileStream outputStream)
		{
			StreamReader txt = new StreamReader(mojangMapSource, Encoding.UTF8);
			StreamWriter buf = new StreamWriter(outputStream);

			bool loop = true;
			while (loop)
			{
				string s = txt.ReadLine();
				if (s is object && s.Length > 0)
				{
					if (s.StartsWith("#", StringComparison.Ordinal))
					{
						continue;
					}

					if (s.StartsWith(" ", StringComparison.Ordinal))
					{ // This is a field or a method.
						s = s.Substring(4);
						string[] parts = s.Split(new string[] { " " }, StringSplitOptions.RemoveEmptyEntries);
						Debug.Assert(parts.Length == 4);

						if (parts[1].EndsWith(")", StringComparison.Ordinal))
						{ // This is a method.
							string returnType = parts[0].Contains(":") ? parts[0].Split(":", true)[2] : parts[0]; // Split line numbers.
							string obfName = parts[3];
							// Separate params from name.
							string methodName = parts[1].Substring(0, parts[1].IndexOf('('));
							string @params = parts[1].Substring( parts[1].IndexOf('(')+1, parts[1].IndexOf(')') -parts[1].IndexOf('(')-1);

							returnType = TypeToDescriptor(returnType);

                            @params = string.Join("", @params.Split(",", true).Select(TypeToDescriptor));

							//if (!methodName.Equals("<init>") && !methodName.Equals("<clinit>"))
							buf.Write("\t" + obfName + " (" + @params + ")" + returnType + " " + methodName + "\n");
						
						}
						else
						{ // This is a field.
							string fieldName = parts[1];
							string obfName = parts[3];

							buf.Write("\t" + obfName + " " + fieldName + "\n");
						}
					}
					else
					{ // Classes have no dependencies.
						string[] parts = s.Split(" ", true);
						//Debug.Assert(parts.Length == 3);

						string className = parts[0].Replace("/", ".");
						string obfName = parts[2].Substring(0, parts[2].Length - 1);
						if (obfName.Contains("."))
						{
							obfName = obfName.Replace(".", "/");
						}
						buf.Write(obfName + " " + className.Replace('.','/') + "\n"); // Write class entry.
					}
				}
				else
				{
					loop = false;
				}
			}

			buf.Close();
			txt.Close();
			///
			ConvertCompleted.Invoke(null, new EventArgs());
		}
		internal static Task ConvertMapping2Tsrg()
		{
			return Task.Run(() =>
			{
				string currentPath = Environment.CurrentDirectory;
				LoadClasses(new FileStream(WorkingFile.MappingInfoTxt, FileMode.Open));
				WriteTsrg(new FileStream(WorkingFile.MappingInfoTxt, FileMode.Open), File.Create(WorkingFile.MappingInfoTsrg));
			});
		}

		internal static event EventHandler ConvertCompleted;
		#endregion
	}
}


