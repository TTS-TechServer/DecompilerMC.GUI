//----------------------------------------------------------------------------------------------------
// <copyright company="Avira Operations GmbH & Co. KG and its licensors">
// © 2016 Avira Operations GmbH & Co. KG and its licensors.  All rights reserved.
// </copyright>
//----------------------------------------------------------------------------------------------------

using System;

namespace DGP.Decompiler.Helper.Download
{
    /// <summary>
    /// DownloadFileCompleted event args
    /// </summary>
    internal class DownloadFileCompletedArgs : EventArgs
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="DownloadFileCompletedArgs"/> class
        /// </summary>
        /// <param name="state">State of download</param>
        /// <param name="fileName">Local path to downloaded file</param>
        /// <param name="fileSource">Downloaded file source</param>
        /// <param name="downloadTime">Downloaded time</param>
        /// <param name="bytesTotal">File size</param>
        /// <param name="bytesReceived">Received bytes</param>
        /// <param name="error">Exception object</param>
        internal DownloadFileCompletedArgs(CompletedState state, string fileName, Uri fileSource, TimeSpan downloadTime, long bytesTotal, long bytesReceived, System.Exception error)
        {
            State = state;
            FileName = fileName;
            FileSource = fileSource;
            Error = error;
            DownloadTime = downloadTime;
            BytesTotal = bytesTotal;
            BytesReceived = bytesReceived;
        }

        /// <summary>
        /// Gets the download state 
        /// </summary>
        internal CompletedState State { get; private set; }

        /// <summary>
        /// Gets the name of downloaded file
        /// </summary>
        internal string FileName { get; private set; }

        /// <summary>
        /// Gets the download source
        /// </summary>
        internal Uri FileSource { get; private set; }

        /// <summary>
        /// Gets the error, or null if there is no error
        /// </summary>
        internal Exception Error { get; private set; }

        /// <summary>
        /// Gets the total download time
        /// </summary>
        internal TimeSpan DownloadTime { get; private set; }

        /// <summary>
        /// Gets the number of received bytes
        /// </summary>
        internal long BytesReceived { get; private set; }

        /// <summary>
        /// Gets the number of total bytes which should be received
        /// </summary>
        internal long BytesTotal { get; private set; }

        /// <summary>
        /// Gets the download progress in percent, from 0 to 100
        /// </summary>
        internal int DownloadProgress
        {
            get
            {
                if (BytesTotal <= 0 || BytesReceived <= 0)
                {
                    return 0;
                }
                return Convert.ToInt32((float)BytesReceived / BytesTotal * 100);
            }
        }

        /// <summary>
        /// Gets the download speed in kilobytes per second
        /// </summary>
        internal int DownloadSpeedInKiloBytesPerSecond
        {
            get
            {
                if (DownloadTime == TimeSpan.Zero || BytesReceived == 0)
                {
                    return 0;
                }

                var kiloBytesReceived = BytesReceived / 1024.0;

                return Convert.ToInt32(kiloBytesReceived / DownloadTime.TotalSeconds);
            }
        }
    }
}