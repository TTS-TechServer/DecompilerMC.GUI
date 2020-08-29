/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntSet
 *  it.unimi.dsi.fastutil.ints.IntSets
 *  javax.annotation.Nullable
 */
package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.font.RawGlyph;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import javax.annotation.Nullable;
import net.minecraft.client.gui.font.glyphs.MissingGlyph;

public class AllMissingGlyphProvider
implements GlyphProvider {
    @Override
    @Nullable
    public RawGlyph getGlyph(int n) {
        return MissingGlyph.INSTANCE;
    }

    @Override
    public IntSet getSupportedGlyphs() {
        return IntSets.EMPTY_SET;
    }
}

