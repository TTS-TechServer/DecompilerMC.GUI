using DGP.DecompilerMC.Helper;
using DGP.DecompilerMC.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DGP.DecompilerMC.Service
{
    internal class VersionsService
    {
        internal const string ReleasesUrl = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
        internal static async Task<Minecraft> GetMinecraftAsync()
        {
            Minecraft minecraft = await Json.GetWebRequestObjectAsync<Minecraft>(ReleasesUrl);
            minecraft.Versions.RemoveAll(version => version.ReleaseTime < new DateTime(2019, 9, 5));
            return minecraft;
        }
    }
}
