/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.exception;

import com.mojang.realmsclient.exception.RealmsServiceException;

public class RetryCallException
extends RealmsServiceException {
    public final int delaySeconds;

    public RetryCallException(int n) {
        super(503, "Retry operation", -1, "");
        this.delaySeconds = n < 0 || n > 120 ? 5 : n;
    }
}

