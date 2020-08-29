using Newtonsoft.Json;
using System;

namespace DGP.DecompilerMC.Model
{
    internal class Mapping
    {
        [JsonProperty("sha1")] internal string sha1 { get; set; }
        [JsonProperty("size")] internal int size { get; set; }
        [JsonProperty("url")] internal Uri url { get; set; }
    }
    internal class Downloads
    {
        [JsonProperty("client")] internal Mapping Client { get; set; }
        [JsonProperty("client_mappings")] internal Mapping ClientMappings { get; set; }
        [JsonProperty("server")] internal Mapping Server { get; set; }
        [JsonProperty("server_mappings")] internal Mapping ServerMappings { get; set; }
    }
    internal class VersionInfo
    {
        [JsonProperty("downloads")] internal Downloads Downloads { get; set; } 
    }

}
