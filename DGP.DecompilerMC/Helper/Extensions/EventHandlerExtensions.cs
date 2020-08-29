using System;

namespace DGP.Decompiler.Helper.Extensions
{
    internal static class EventHandlerExtensions
    {
        internal static void SafeInvoke<T>(this EventHandler<T> evt, object sender, T e) where T : EventArgs => evt?.Invoke(sender, e);
    }
}
