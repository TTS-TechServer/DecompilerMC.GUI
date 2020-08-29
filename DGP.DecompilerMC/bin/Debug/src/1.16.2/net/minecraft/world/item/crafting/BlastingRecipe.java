/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;

public class BlastingRecipe
extends AbstractCookingRecipe {
    public BlastingRecipe(ResourceLocation resourceLocation, String string, Ingredient ingredient, ItemStack itemStack, float f, int n) {
        super(RecipeType.BLASTING, resourceLocation, string, ingredient, itemStack, f, n);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.BLAST_FURNACE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.BLASTING_RECIPE;
    }
}

