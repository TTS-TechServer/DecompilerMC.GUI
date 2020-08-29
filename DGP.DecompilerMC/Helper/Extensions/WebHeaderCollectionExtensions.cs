using System.Net;
using System.Net.Mime;

namespace DGP.Decompiler.Helper.Extensions
{
    internal static class WebHeaderCollectionExtensions
    {
        internal static long GetContentLength(this WebHeaderCollection responseHeaders)
        {
            long contentLength = -1;
            if (responseHeaders != null && !string.IsNullOrEmpty(responseHeaders["Content-Length"]))
                long.TryParse(responseHeaders["Content-Length"], out contentLength);
            return contentLength;
        }

        internal static ContentDisposition GetContentDisposition(this WebHeaderCollection responseHeaders)
        {
            if (responseHeaders != null && !string.IsNullOrEmpty(responseHeaders["Content-Disposition"]))
                return new ContentDisposition(responseHeaders["Content-Disposition"]);
            return null;
        }
    }
}
