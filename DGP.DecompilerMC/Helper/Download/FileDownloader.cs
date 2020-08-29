//----------------------------------------------------------------------------------------------------
// <copyright company="Avira Operations GmbH & Co. KG and its licensors">
// © 2016 Avira Operations GmbH & Co. KG and its licensors.  All rights reserved.
// </copyright>
//----------------------------------------------------------------------------------------------------

using DGP.Decompiler.Helper.Extensions;
using System;
using System.ComponentModel;
using System.IO;
using System.Net;
using System.Threading;

namespace DGP.Decompiler.Helper.Download
{
    /// <summary>
    /// Class used for downloading files. The .NET WebClient is used for downloading.
    /// </summary>
    internal class FileDownloader : IFileDownloader
    {
        private readonly IDownloadCache downloadCache;
        //private readonly ILogger logger = LoggerFacade.GetCurrentClassLogger();
        private readonly ManualResetEvent readyToDownload = new ManualResetEvent(true);
        private readonly System.Timers.Timer attemptTimer = new System.Timers.Timer();
        private readonly object cancelSync = new object();

        private bool isCancelled;
        private bool disposed;
        private bool useFileNameFromServer = true;
        private bool isFallback;

        private int attemptNumber;

        private string localFileName;
        private string destinationFileName;
        private string destinationFolder;

        private Uri fileSource;
        private StreamCopyWorker worker;
        private DownloadWebClient downloadWebClient;

        /// <summary>
        /// Initializes a new instance of the <see cref="FileDownloader"/> class. No download cache would be used, resume is not supported
        /// </summary>
        internal FileDownloader()
            : this(null)
        {
        }

        /// <summary>
        /// Initializes a new instance of the <see cref="FileDownloader"/> class.
        /// </summary>
        /// <param name="downloadCache">IDownloadCache instance</param>
        internal FileDownloader(IDownloadCache downloadCache)
        {
            DnsFallbackResolver = null;

            MaxAttempts = 60;
            DelayBetweenAttempts = TimeSpan.FromSeconds(3);
            SafeWaitTimeout = TimeSpan.FromSeconds(15);
            SourceStreamReadTimeout = TimeSpan.FromSeconds(5);

            this.downloadCache = downloadCache;
            disposed = false;

            attemptTimer.Elapsed += OnDownloadAttemptTimer;
        }

        /// <summary>
        /// Fired when download is finished, even if it's failed.
        /// </summary>
        public event EventHandler<DownloadFileCompletedArgs> DownloadFileCompleted;

        /// <summary>
        /// Fired when download progress is changed.
        /// </summary>
        public event EventHandler<DownloadFileProgressChangedArgs> DownloadProgressChanged;

        /// <summary>
        /// Gets or sets the DNS fallback resolver. Default is null.
        /// </summary>
        public IDnsFallbackResolver DnsFallbackResolver { get; set; }

        /// <summary>
        /// Gets or sets the delay between download attempts. Default is 3 seconds. 
        /// </summary>
        public TimeSpan DelayBetweenAttempts { get; set; }

        /// <summary>
        /// Gets or sets the maximum waiting timeout for pending request to be finished. Default is 15 seconds.
        /// </summary>
        public TimeSpan SafeWaitTimeout { get; set; }

        /// <summary>
        /// Gets or sets the timeout for source stream. Default is 5 seconds.
        /// </summary>
        internal TimeSpan SourceStreamReadTimeout { get; set; }

        /// <summary>
        /// Gets or sets the maximum number of download attempts. Default is 60.
        /// </summary>
        public int MaxAttempts { get; set; }

        /// <summary>
        /// Gets the total bytes received so far
        /// </summary>
        public long BytesReceived { get; internal set; }

        /// <summary>
        /// Gets the total bytes to receive
        /// </summary>
        public long TotalBytesToReceive { get; internal set; }

        /// <summary>
        /// Gets or sets the time when download was started
        /// </summary>
        internal DateTime DownloadStartTime { get; set; }

        private bool UseCaching
        {
            get
            {
                return downloadCache != null;
            }
        }

        /// <summary>
        /// Start async download of source to destinationPath
        /// </summary>
        /// <param name="source">Source URI</param>
        /// <param name="destinationPath">Full path with file name.</param>
        public void DownloadFileAsync(Uri source, string destinationPath)
        {
            DownloadFileAsync(source, destinationPath, false);
        }

        /// <summary>
        /// Start download of source file to downloadDirectory. File would be saved with filename taken from server 
        /// </summary>
        /// <param name="source">Source URI</param>
        /// <param name="destinationDirectory">Destination directory</param>
        public void DownloadFileAsyncPreserveServerFileName(Uri source, string destinationDirectory)
        {
            DownloadFileAsync(source, Path.Combine(destinationDirectory, Guid.NewGuid().ToString()), true);
        }

        /// <summary>
        /// Cancel current download
        /// </summary>
        public void CancelDownloadAsync()
        {
            lock (cancelSync)
            {
                if (isCancelled)
                {
                    return;
                }
                isCancelled = true;
            }

            //logger.Debug("CancelDownloadAsync called.");
            if (worker != null)
            {
                worker.Cancel();
            }

            TriggerDownloadWebClientCancelAsync();
            DeleteDownloadedFile();  ////todo: maybe this is equal to InvalidateCache? Can we get rid of DeleteDownloadedFile ?

            readyToDownload.Set();
        }

        private void DeleteDownloadedFile()
        {
            FileUtils.TryFileDelete(localFileName);
        }

        private void InvalidateCache(Uri uri)
        {
            if (!UseCaching)
            {
                return;
            }

            downloadCache.Invalidate(uri);
            //logger.Debug("Cached resource was invalidated: {0}", uri);
        }

        private void DownloadFileAsync(Uri source, string destinationPath, bool useServerFileName)
        {
            if (!WaitSafeStart())
            {
                throw new System.Exception("Unable to start download because another request is still in progress.");
            }

            //logger.Debug("DownloadFileAsync({0}, {1}) is called.", source, destinationPath);

            useFileNameFromServer = useServerFileName;
            fileSource = source;
            BytesReceived = 0;
            destinationFileName = destinationPath;
            destinationFolder = Path.GetDirectoryName(destinationPath);
            isCancelled = false;
            localFileName = string.Empty;

            DownloadStartTime = DateTime.Now;

            attemptNumber = 0;

            StartDownload();
        }

        private void OnDownloadAttemptTimer(object sender, EventArgs eventArgs)
        {
            StartDownload();
        }

        private void StartDownload()
        {
            if (IsCancelled())
            {
                return;
            }

            //logger.Debug("FileDownloader attempt {0} of {1}.", attemptNumber, MaxAttempts);

            localFileName = ComposeLocalFilename();

            if (!UseCaching)
            {
                TriggerWebClientDownloadFileAsync();
                return;
            }

            TotalBytesToReceive = -1;
            var headers = GetHttpHeaders(fileSource);
            if (headers != null)
            {
                TotalBytesToReceive = headers.GetContentLength();
            }

            if (TotalBytesToReceive == -1)
            {
                TotalBytesToReceive = 0;
                //logger.Warn("Received no Content-Length header from server for {0}. Cache is not used, Resume is not supported", fileSource);
                TriggerWebClientDownloadFileAsync();
            }
            else
            {
                ResumeDownload(headers);
            }
        }

        private void ResumeDownload(WebHeaderCollection headers)
        {
            isFallback = false;

            string downloadedFileName = GetDestinationFileName(headers);

            long downloadedFileSize;
            if (!FileUtils.TryGetFileSize(downloadedFileName, out downloadedFileSize))
            {
                ////todo: handle this case in future. Now in case of error we simply proceed with downloadedFileSize=0
            }

            if (UseCaching)
            {
                downloadCache.Add(fileSource, localFileName, headers);
            }

            if (downloadedFileSize > TotalBytesToReceive)
            {
                InvalidateCache(fileSource);
            }

            if (downloadedFileSize != TotalBytesToReceive)
            {
                if (!FileUtils.ReplaceFile(downloadedFileName, localFileName))
                {
                    InvalidateCache(fileSource);
                }

                Download(fileSource, localFileName, TotalBytesToReceive);
            }
            else
            {
                DownloadFromCache(downloadedFileName);
            }
        }

        private void DownloadFromCache(string cachedResource)
        {
            //logger.Debug("Taking file from cache.");
            OnDownloadProgressChanged(this, new DownloadFileProgressChangedArgs(100, TotalBytesToReceive, TotalBytesToReceive));
            InvokeDownloadCompleted(CompletedState.Succeeded, cachedResource, null, true);
            readyToDownload.Set();
        }

        private void TriggerWebClientDownloadFileAsync()
        {
            //logger.Debug("Falling back to legacy DownloadFileAsync.");
            try
            {
                isFallback = true;
                var destinationDirectory = Path.GetDirectoryName(localFileName);
                if (destinationDirectory != null && !Directory.Exists(destinationDirectory))
                {
                    Directory.CreateDirectory(destinationDirectory);
                }
                TryCleanupExistingDownloadWebClient();

                downloadWebClient = CreateWebClient();
                downloadWebClient.DownloadFileAsync(fileSource, localFileName);
                //logger.Debug("Download async started. Source: {0} Destination: {1}", fileSource, localFileName);
            }
            catch (System.Exception ex)
            {
                //logger.Warn("Failed to download Source:{0}, Destination:{1}, Error:{2}.", fileSource, localFileName, ex.Message);
                if (!AttemptDownload())
                {
                    InvokeDownloadCompleted(CompletedState.Failed, localFileName, ex);
                }
            }
        }

        private DownloadWebClient CreateWebClient()
        {
            var webClient = new DownloadWebClient();
            webClient.DownloadFileCompleted += OnDownloadCompleted;
            webClient.DownloadProgressChanged += OnDownloadProgressChanged;
            webClient.OpenReadCompleted += OnOpenReadCompleted;
            return webClient;
        }

        private void TryCleanupExistingDownloadWebClient()
        {
            if (downloadWebClient == null)
            {
                return;
            }
            try
            {
                lock (this)
                {
                    if (downloadWebClient != null)
                    {
                        downloadWebClient.DownloadFileCompleted -= OnDownloadCompleted;
                        downloadWebClient.DownloadProgressChanged -= OnDownloadProgressChanged;
                        downloadWebClient.OpenReadCompleted -= OnOpenReadCompleted;
                        downloadWebClient.CancelAsync();
                        downloadWebClient.Dispose();
                        downloadWebClient = null;
                    }
                }
            }
            catch (System.Exception)
            {
                //logger.Warn("Error while cleaning up web client : {0}", e.Message);
            }
        }

        private bool AttemptDownload()
        {
            if (++attemptNumber <= MaxAttempts)
            {
                attemptTimer.Interval = DelayBetweenAttempts.TotalMilliseconds;
                attemptTimer.AutoReset = false;
                attemptTimer.Start();
                //logger.Debug("Downloader scheduled next attempt in {0} seconds.", DelayBetweenAttempts.TotalSeconds);
                return true;
            }

            readyToDownload.Set();
            return false;
        }

        private string GetDestinationFileName(WebHeaderCollection headers)
        {
            if (!UseCaching)
            {
                //logger.Debug("Not using cache. Source: {0} Destination: {1}", fileSource, localFileName);
                return localFileName;
            }

            var cachedDestinationPath = downloadCache.Get(fileSource, headers);
            if (cachedDestinationPath == null)
            {
                //logger.Debug("No cache item found. Source: {0} Destination: {1}", fileSource, localFileName);
                DeleteDownloadedFile();
                return localFileName;
            }

            //logger.Debug("Download resource was found in cache. Source: {0} Destination: {1}", fileSource, cachedDestinationPath);
            return cachedDestinationPath;
        }

        private string ComposeLocalFilename()
        {
            if (useFileNameFromServer)
            {
                return Path.Combine(destinationFolder, string.Format("{0}.tmp", Guid.NewGuid()));
            }
            return Path.Combine(destinationFolder, destinationFileName);
        }

        private void Download(Uri source, string fileDestination, long totalBytesToReceive)
        {
            try
            {
                long seekPosition;
                FileUtils.TryGetFileSize(fileDestination, out seekPosition);

                TryCleanupExistingDownloadWebClient();
                downloadWebClient = CreateWebClient();
                downloadWebClient.OpenReadAsync(source, seekPosition);
                //logger.Debug("Download started. Source: {0} Destination: {1} Size: {2}", source, fileDestination, totalBytesToReceive);
            }
            catch (System.Exception e)
            {
                //logger.Debug("Download failed: {0}", e.Message);
                if (!AttemptDownload())
                {
                    InvokeDownloadCompleted(CompletedState.Failed, localFileName, e);
                }
            }
        }

        private WebHeaderCollection GetHttpHeaders(Uri source)
        {
            try
            {
                var webRequest = WebRequest.Create(source);
                webRequest.Method = WebRequestMethods.Http.Head;

                using (var webResponse = webRequest.GetResponse())
                {
                    return webResponse.Headers;
                }
            }
            catch (System.Exception)
            {
                //logger.Warn("Unable to read http headers for {0}: {1}; typeof(Exception)={2}", source, e.Message, e.GetType());
                return null;
            }
        }

        private void OnDownloadProgressChanged(object sender, DownloadProgressChangedEventArgs args)
        {
            var e = new DownloadFileProgressChangedArgs(args.ProgressPercentage, args.BytesReceived, args.TotalBytesToReceive);

            OnDownloadProgressChanged(sender, e);
        }

        private void OnDownloadProgressChanged(object sender, DownloadFileProgressChangedArgs args)
        {
            if (BytesReceived < args.BytesReceived)
            {
                ////bytes growing? we have connection!
                attemptNumber = 1;
            }

            BytesReceived = args.BytesReceived;
            TotalBytesToReceive = args.TotalBytesToReceive;

            DownloadProgressChanged.SafeInvoke(sender, args);
        }

        private void InvokeDownloadCompleted(CompletedState downloadCompletedState, string fileName, System.Exception error = null, bool fromCache = false)
        {
            var downloadTime = fromCache ? TimeSpan.Zero : DateTime.Now.Subtract(DownloadStartTime);
            if (worker != null)
            {
                BytesReceived = worker.Position;
            }

            DownloadFileCompleted.SafeInvoke(this, new DownloadFileCompletedArgs(downloadCompletedState, fileName, fileSource, downloadTime, TotalBytesToReceive, BytesReceived, error));
        }

        private void OnOpenReadCompleted(object sender, OpenReadCompletedEventArgs args)
        {
            var webClient = sender as DownloadWebClient;
            if (webClient == null)
            {
                //logger.Warn("Wrong sender in OnOpenReadCompleted: Actual:{0} Expected:{1}", sender.GetType(), typeof(DownloadWebClient));
                return;
            }

            lock (cancelSync)
            {
                if (isCancelled)
                {
                    //logger.Debug("Download was cancelled.");
                    return;
                }

                if (!webClient.HasResponse)
                {
                    //logger.Debug("DownloadWebClient returned no response.");
                    TriggerWebClientDownloadFileAsync();
                    return;
                }

                bool appendExistingChunk = webClient.IsPartialResponse;
                var destinationStream = CreateDestinationStream(appendExistingChunk);
                if (destinationStream != null)
                {
                    TrySetStreamReadTimeout(args.Result, (int)SourceStreamReadTimeout.TotalMilliseconds);

                    worker = new StreamCopyWorker();
                    worker.Completed += OnWorkerCompleted;
                    worker.ProgressChanged += OnWorkerProgressChanged;
                    worker.CopyAsync(args.Result, destinationStream, TotalBytesToReceive);
                }
            }
        }

        private bool TrySetStreamReadTimeout(Stream stream, int timeout)
        {
            try
            {
                stream.ReadTimeout = timeout;
                return true;
            }
            catch (System.Exception)
            {
                //logger.Warn("Unable to set read timeout for source stream {0}", e.Message);
                return false;
            }
        }

        private Stream CreateDestinationStream(bool append)
        {
            FileStream destinationStream = null;
            try
            {
                var destinationDirectory = Path.GetDirectoryName(localFileName);
                if (destinationDirectory != null && !Directory.Exists(destinationDirectory))
                {
                    Directory.CreateDirectory(destinationDirectory);
                }

                destinationStream = new FileStream(localFileName, FileMode.OpenOrCreate, FileAccess.Write, FileShare.None);
                if (append)
                {
                    destinationStream.Seek(0, SeekOrigin.End);
                }
                else
                {
                    destinationStream.SetLength(0);
                }
            }
            catch (System.Exception ex)
            {
                if (destinationStream != null)
                {
                    destinationStream.Dispose();
                    destinationStream = null;
                }
                OnDownloadCompleted(downloadWebClient, new AsyncCompletedEventArgs(ex, false, null));
            }
            return destinationStream;
        }

        private void OnWorkerProgressChanged(object sender, StreamCopyProgressEventArgs eventArgs)
        {
            if (isCancelled)
            {
                return;
            }

            if (TotalBytesToReceive == 0)
            {
                return;
            }
            var progress = eventArgs.BytesReceived / TotalBytesToReceive;
            var progressPercentage = (int)(progress * 100);

            OnDownloadProgressChanged(this, new DownloadFileProgressChangedArgs(progressPercentage, eventArgs.BytesReceived, TotalBytesToReceive));
        }

        private void OnWorkerCompleted(object sender, StreamCopyCompleteEventArgs eventArgs)
        {
            try
            {
                OnDownloadCompleted(downloadWebClient, new AsyncCompletedEventArgs(eventArgs.Exception, eventArgs.CompleteState == CompletedState.Canceled, null));
            }
            finally
            {
                worker.ProgressChanged -= OnWorkerProgressChanged;
                worker.Completed -= OnWorkerCompleted;
                worker.Dispose();
            }
        }

        /// <summary>
        /// OnDownloadCompleted event handler
        /// </summary>
        /// <param name="sender">Sender object</param>
        /// <param name="args">AsyncCompletedEventArgs instance</param>
        protected void OnDownloadCompleted(object sender, AsyncCompletedEventArgs args)
        {
            var webClient = sender as DownloadWebClient;
            if (webClient == null)
            {
                //logger.Warn("Wrong sender in OnDownloadCompleted: Actual:{0} Expected:{1}", sender.GetType(), typeof(DownloadWebClient));
                InvokeDownloadCompleted(CompletedState.Failed, localFileName);
                return;
            }

            if (args.Cancelled)
            {
                //logger.Debug("Download cancelled. Source: {0} Destination: {1}", fileSource, localFileName);
                DeleteDownloadedFile();

                InvokeDownloadCompleted(CompletedState.Canceled, localFileName);
                readyToDownload.Set();
            }
            else if (args.Error != null)
            {
                if (isFallback)
                {
                    DeleteDownloadedFile();
                }

                ////We may have NameResolutionFailure on internet connectivity problem.
                ////We don't use DnsFallbackResolver if we successfully started downloading, and then got internet problem.
                ////If we change [this.fileSource] here - we lose downloaded chunk in Cache (i.e. we create a new Cache item for new [this.fileSource]
                if (attemptNumber == 1 && DnsFallbackResolver != null && IsNameResolutionFailure(args.Error))
                {
                    var newFileSource = DnsFallbackResolver.Resolve(fileSource);
                    if (newFileSource != null)
                    {
                        fileSource = newFileSource;
                        //logger.Debug("Download failed in case of DNS resolve error. Retry downloading with new source: {0}.", fileSource);
                        AttemptDownload();
                        return;
                    }
                }

                //logger.Debug("Download failed. Source: {0} Destination: {1} Error: {2}", fileSource, localFileName, args.Error);

                if (!AttemptDownload())
                {
                    InvokeDownloadCompleted(CompletedState.Failed, null, args.Error);
                    readyToDownload.Set();
                }
            }
            else
            {
                if (useFileNameFromServer)
                {
                    localFileName = ApplyNewFileName(localFileName, webClient.GetOriginalFileNameFromDownload());
                }

                //logger.Debug("Download completed. Source: {0} Destination: {1}", fileSource, localFileName);
                if (UseCaching)
                {
                    downloadCache.Add(fileSource, localFileName, webClient.ResponseHeaders);
                }

                ////we may have the destination file not immediately closed after downloading
                WaitFileClosed(localFileName, TimeSpan.FromSeconds(3));

                InvokeDownloadCompleted(CompletedState.Succeeded, localFileName, null);
                readyToDownload.Set();
            }
        }

        /// <summary>
        /// Rename oldFilePath to newFileName , placing file in same folder or in temporary folder if renaming failed. 
        /// </summary>
        /// <param name="oldFilePath">Full path and name of the file to be renamed</param>
        /// <param name="newFileName">New file name</param>
        /// <returns>Full path to renamed file</returns>
        protected virtual string ApplyNewFileName(string oldFilePath, string newFileName)
        {
            var downloadedFileName = Path.GetFileName(oldFilePath);
            var downloadDirectory = Path.GetDirectoryName(oldFilePath);

            if (newFileName == null || newFileName == downloadedFileName || downloadDirectory == null)
            {
                return oldFilePath;
            }

            var newFilePath = Path.Combine(downloadDirectory, newFileName);

            if (File.Exists(newFilePath))
            {
                try
                {
                    File.Delete(newFilePath);
                }
                catch (System.Exception)
                {
                    newFilePath = Path.Combine(CreateTempFolder(downloadDirectory), newFileName);
                }
            }

            if (newFilePath == oldFilePath)
            {
                return oldFilePath;
            }

            File.Move(oldFilePath, newFilePath);
            return newFilePath;
        }

        private void TriggerDownloadWebClientCancelAsync()
        {
            if (downloadWebClient != null)
            {
                downloadWebClient.CancelAsync();
                downloadWebClient.OpenReadCompleted -= OnOpenReadCompleted;
                //logger.Debug("Successfully cancelled web client.");
            }
        }

        private string CreateTempFolder(string rootFolderPath)
        {
            while (true)
            {
                var folderPath = Path.Combine(rootFolderPath, Path.GetRandomFileName());
                if (!Directory.Exists(folderPath))
                {
                    Directory.CreateDirectory(folderPath);
                    return folderPath;
                }
            }
        }

        private bool IsNameResolutionFailure(System.Exception exception)
        {
            var webException = exception as WebException;
            return webException != null && webException.Status == WebExceptionStatus.NameResolutionFailure;
        }

        private bool WaitSafeStart()
        {
            //logger.Debug("Calling DownloadFileAsync...");
            if (!readyToDownload.WaitOne(SafeWaitTimeout))
            {
                //logger.Warn("Failed to call DownloadFileAsync, another request is in progress: Source:{0}, Destination:{1}", fileSource, localFileName);
                return false;
            }
            readyToDownload.Reset();
            return true;
        }

        private void WaitFileClosed(string fileName, TimeSpan waitTimeout)
        {
            var waitCounter = TimeSpan.Zero;
            while (waitCounter < waitTimeout)
            {
                try
                {
                    var fileHandle = File.Open(fileName, FileMode.Open, FileAccess.Read);
                    //fileHandle.Close();
                    fileHandle.Dispose();
                    Thread.Sleep(500);
                    return;
                }
                catch (System.Exception)
                {
                    waitCounter = waitCounter.Add(TimeSpan.FromMilliseconds(500));
                    Thread.Sleep(500);
                }
            }
        }

        private bool IsCancelled()
        {
            lock (cancelSync)
            {
                if (isCancelled)
                {
                    //logger.Debug("Download was cancelled.");
                    return true;
                }
            }
            return false;
        }

        /// <summary>
        /// Performs application-defined tasks associated with freeing, releasing, or resetting unmanaged resources.
        /// </summary>
        /// <filterpriority>2</filterpriority>
        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        /// <summary>
        /// Do the actual dispose
        /// </summary>
        /// <param name="disposing">True if called from Dispose</param>
        protected virtual void Dispose(bool disposing)
        {
            if (!disposed)
            {
                if (disposing)
                {
                    if (readyToDownload.WaitOne(TimeSpan.FromMinutes(10)))
                    {
                        if (worker != null)
                        {
                            worker.Dispose();
                        }
                        if (downloadWebClient != null)
                        {
                            downloadWebClient.Dispose();
                        }
                        readyToDownload.Close();
                        attemptTimer.Dispose();
                    }
                }
                disposed = true;
            }
        }
    }
}