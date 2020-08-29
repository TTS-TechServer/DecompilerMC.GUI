/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.searchtree;

import net.minecraft.client.searchtree.SearchTree;

public interface MutableSearchTree<T>
extends SearchTree<T> {
    public void add(T var1);

    public void clear();

    public void refresh();
}

