using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media;

namespace DGP.DecompilerMC.Model
{
    public class LatestVersion
    {
        [JsonProperty("release")] public string Release { get; set; }
        [JsonProperty("snapshot")] public string Snapshot { get; set; }
    }
    public class Version
    {
        [JsonProperty("id")] public string Id { get; set; }
        [JsonProperty("type")] public string ReleaseType { get; set; }
        [JsonProperty("url")] public string JsonUrl { get; set; }
        [JsonProperty("time")] public DateTime Time { get; set; }
        [JsonProperty("releaseTime")] public DateTime ReleaseTime { get; set; }
        public SolidColorBrush Brush 
        {
            get
            {
                switch (ReleaseType)
                {
                    case "release":
                        return new SolidColorBrush(Color.FromRgb(45, 177, 45));//green
                    case "snapshot":
                        return new SolidColorBrush(Color.FromRgb(245, 145, 0));//orange
                    default:
                        return new SolidColorBrush(Color.FromRgb(115, 115, 115));//gary
                }
            }
        }
    }
    public class Minecraft
    {
        [JsonProperty("latest")] public LatestVersion Latest { get; set; }
        [JsonProperty("versions")] public List<Version> Versions { get; set; }
    }

}
