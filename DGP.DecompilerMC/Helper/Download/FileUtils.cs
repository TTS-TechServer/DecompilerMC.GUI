//----------------------------------------------------------------------------------------------------
// <copyright company="Avira Operations GmbH & Co. KG and its licensors">
// © 2016 Avira Operations GmbH & Co. KG and its licensors.  All rights reserved.
// </copyright>
//----------------------------------------------------------------------------------------------------

using System;
using System.IO;

namespace DGP.Decompiler.Helper.Download
{
    internal static class FileUtils
    {
        //private static readonly ILogger Logger = LoggerFacade.GetCurrentClassLogger();

        internal static bool TryGetFileSize(string filename, out long filesize)
        {
            try
            {
                var fileInfo = new FileInfo(filename);
                filesize = fileInfo.Length;
            }
            catch (System.Exception)
            {
                //Logger.Debug("Failed to get file size for {0}. Exception: {1}", filename, e.Message);
                filesize = 0;
                return false;
            }
            return true;
        }

        internal static bool TryFileDelete(string filename)
        {
            try
            {
                File.Delete(filename);
            }
            catch (System.Exception)
            {
                //Logger.Debug("Unable to delete file {0}. Exception: {1}", filename, e.Message);
                return false;
            }
            return true;
        }

        internal static bool ReplaceFile(string source, string destination)
        {
            if (!destination.Equals(source, StringComparison.InvariantCultureIgnoreCase))
            {
                try
                {
                    File.Delete(destination);
                    File.Move(source, destination);
                }
                catch (System.Exception)
                {
                    //Logger.Warn("Unable replace local file {0} with cached resource {1}, {2}", destination, source, e.Message);
                    return false;
                }
            }
            return true;
        }
    }
}