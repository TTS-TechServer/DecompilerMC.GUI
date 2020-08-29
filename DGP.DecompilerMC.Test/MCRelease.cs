using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DGP.DecompilerMC.Model
{
    public class LatestVersion
    {
        [JsonProperty("release")] public string Release { get; set; }
        [JsonProperty("snapshot")] public string Snapshot { get; set; }
    }
    public class Release
    {
        [JsonProperty("id")] public string Version { get; set; }
        [JsonProperty("type")] public string ReleaseType { get; set; }
        [JsonProperty("url")] public string JsonUrl { get; set; }
        [JsonProperty("time")] public DateTime time { get; set; }
        [JsonProperty("releaseTime")] public DateTime ReleaseTime { get; set; }
    }
    public class MCReleases
    {
        [JsonProperty("latest")] public LatestVersion Latest { get; set; }
        [JsonProperty("versions")] public List<Release> Releases { get; set; }
    }

}
