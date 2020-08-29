using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DGP.DecompilerMC.Helper
{
    internal static class StringExtensions
    {
        //impl the java like split
        internal static string[] Split(this string self, string regexDelimiter, bool trimTrailingEmptyStrings)
        {
            string[] splitArray = System.Text.RegularExpressions.Regex.Split(self, regexDelimiter);
            if (trimTrailingEmptyStrings)
            {
                if (splitArray.Length > 1)
                {
                    for (int i = splitArray.Length; i > 0; i--)
                    {
                        if (splitArray[i - 1].Length > 0)
                        {
                            if (i < splitArray.Length)
                                Array.Resize(ref splitArray, i);
                            break;
                        }
                    }
                }
            }
            return splitArray;
        }
    }
}
