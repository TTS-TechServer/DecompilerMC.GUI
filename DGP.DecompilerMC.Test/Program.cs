using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DGP.DecompilerMC.Test
{
    class Program
    {
        static void Main(string[] args)
        {
            Debug.WriteLine(DateTime.Parse("2020-08-07T14:35:39+00:00"));
            Process.Start("explorer.exe", @"\mapping\");
        }
    }
}
