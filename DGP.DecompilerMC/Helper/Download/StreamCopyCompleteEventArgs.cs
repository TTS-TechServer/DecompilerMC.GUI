//----------------------------------------------------------------------------------------------------
// <copyright company="Avira Operations GmbH & Co. KG and its licensors">
// © 2016 Avira Operations GmbH & Co. KG and its licensors.  All rights reserved.
// </copyright>
//----------------------------------------------------------------------------------------------------

using System;

namespace DGP.Decompiler.Helper.Download
{
    internal class StreamCopyCompleteEventArgs : EventArgs
    {
        public CompletedState CompleteState { get; set; }
        public System.Exception Exception { get; set; }
    }
}