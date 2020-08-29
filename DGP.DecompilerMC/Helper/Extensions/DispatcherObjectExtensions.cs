using System;
using System.Windows;
using System.Windows.Threading;

namespace DGP.Decompiler.Helper.Extensions
{
    internal static class DispatcherObjectExtensions
    {
        internal static void Invoke(this DispatcherObject dispatcherObject, Action invokeAction)
        {
            if (dispatcherObject == null)
                throw new ArgumentNullException(nameof(dispatcherObject));
            if (invokeAction == null)
                throw new ArgumentNullException(nameof(invokeAction));
            if (dispatcherObject.Dispatcher.CheckAccess())
                invokeAction();
            else
                dispatcherObject.Dispatcher.Invoke(invokeAction);
        }
    }
}
