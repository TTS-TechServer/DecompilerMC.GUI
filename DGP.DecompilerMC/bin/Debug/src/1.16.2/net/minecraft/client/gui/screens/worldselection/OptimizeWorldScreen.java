/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.mojang.datafixers.DataFixer
 *  com.mojang.datafixers.util.Function4
 *  it.unimi.dsi.fastutil.booleans.BooleanConsumer
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap
 *  javax.annotation.Nullable
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Function4;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.util.worldupdate.WorldUpgrader;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OptimizeWorldScreen
extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Object2IntMap<ResourceKey<Level>> DIMENSION_COLORS = (Object2IntMap)Util.make(new Object2IntOpenCustomHashMap(Util.identityStrategy()), object2IntOpenCustomHashMap -> {
        object2IntOpenCustomHashMap.put(Level.OVERWORLD, -13408734);
        object2IntOpenCustomHashMap.put(Level.NETHER, -10075085);
        object2IntOpenCustomHashMap.put(Level.END, -8943531);
        object2IntOpenCustomHashMap.defaultReturnValue(-2236963);
    });
    private final BooleanConsumer callback;
    private final WorldUpgrader upgrader;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Nullable
    public static OptimizeWorldScreen create(Minecraft minecraft, BooleanConsumer booleanConsumer, DataFixer dataFixer, LevelStorageSource.LevelStorageAccess levelStorageAccess, boolean bl) {
        RegistryAccess.RegistryHolder registryHolder = RegistryAccess.builtin();
        try (Minecraft.ServerStem serverStem = minecraft.makeServerStem(registryHolder, Minecraft::loadDataPacks, (Function4<LevelStorageSource.LevelStorageAccess, RegistryAccess.RegistryHolder, ResourceManager, DataPackConfig, WorldData>)((Function4)Minecraft::loadWorldData), false, levelStorageAccess);){
            WorldData worldData = serverStem.worldData();
            levelStorageAccess.saveDataTag(registryHolder, worldData);
            ImmutableSet<ResourceKey<Level>> immutableSet = worldData.worldGenSettings().levels();
            OptimizeWorldScreen optimizeWorldScreen = new OptimizeWorldScreen(booleanConsumer, dataFixer, levelStorageAccess, worldData.getLevelSettings(), bl, immutableSet);
            return optimizeWorldScreen;
        }
        catch (Exception exception) {
            LOGGER.warn("Failed to load datapacks, can't optimize world", (Throwable)exception);
            return null;
        }
    }

    private OptimizeWorldScreen(BooleanConsumer booleanConsumer, DataFixer dataFixer, LevelStorageSource.LevelStorageAccess levelStorageAccess, LevelSettings levelSettings, boolean bl, ImmutableSet<ResourceKey<Level>> immutableSet) {
        super(new TranslatableComponent("optimizeWorld.title", levelSettings.levelName()));
        this.callback = booleanConsumer;
        this.upgrader = new WorldUpgrader(levelStorageAccess, dataFixer, immutableSet, bl);
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 150, 200, 20, CommonComponents.GUI_CANCEL, button -> {
            this.upgrader.cancel();
            this.callback.accept(false);
        }));
    }

    @Override
    public void tick() {
        if (this.upgrader.isFinished()) {
            this.callback.accept(true);
        }
    }

    @Override
    public void onClose() {
        this.callback.accept(false);
    }

    @Override
    public void removed() {
        this.upgrader.cancel();
    }

    @Override
    public void render(PoseStack poseStack, int n, int n2, float f) {
        this.renderBackground(poseStack);
        OptimizeWorldScreen.drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        int n3 = this.width / 2 - 150;
        int n4 = this.width / 2 + 150;
        int n5 = this.height / 4 + 100;
        int n6 = n5 + 10;
        Component component = this.upgrader.getStatus();
        int n7 = this.width / 2;
        this.font.getClass();
        OptimizeWorldScreen.drawCenteredString(poseStack, this.font, component, n7, n5 - 9 - 2, 0xA0A0A0);
        if (this.upgrader.getTotalChunks() > 0) {
            OptimizeWorldScreen.fill(poseStack, n3 - 1, n5 - 1, n4 + 1, n6 + 1, -16777216);
            OptimizeWorldScreen.drawString(poseStack, this.font, new TranslatableComponent("optimizeWorld.info.converted", this.upgrader.getConverted()), n3, 40, 0xA0A0A0);
            TranslatableComponent translatableComponent = new TranslatableComponent("optimizeWorld.info.skipped", this.upgrader.getSkipped());
            this.font.getClass();
            OptimizeWorldScreen.drawString(poseStack, this.font, translatableComponent, n3, 40 + 9 + 3, 0xA0A0A0);
            TranslatableComponent translatableComponent2 = new TranslatableComponent("optimizeWorld.info.total", this.upgrader.getTotalChunks());
            this.font.getClass();
            OptimizeWorldScreen.drawString(poseStack, this.font, translatableComponent2, n3, 40 + (9 + 3) * 2, 0xA0A0A0);
            int n8 = 0;
            for (ResourceKey resourceKey : this.upgrader.levels()) {
                int n9 = Mth.floor(this.upgrader.dimensionProgress(resourceKey) * (float)(n4 - n3));
                OptimizeWorldScreen.fill(poseStack, n3 + n8, n5, n3 + n8 + n9, n6, DIMENSION_COLORS.getInt((Object)resourceKey));
                n8 += n9;
            }
            int n10 = this.upgrader.getConverted() + this.upgrader.getSkipped();
            String string = n10 + " / " + this.upgrader.getTotalChunks();
            int n11 = this.width / 2;
            this.font.getClass();
            OptimizeWorldScreen.drawCenteredString(poseStack, this.font, string, n11, n5 + 2 * 9 + 2, 0xA0A0A0);
            String string2 = Mth.floor(this.upgrader.getProgress() * 100.0f) + "%";
            int n12 = this.width / 2;
            int n13 = n5 + (n6 - n5) / 2;
            this.font.getClass();
            OptimizeWorldScreen.drawCenteredString(poseStack, this.font, string2, n12, n13 - 9 / 2, 0xA0A0A0);
        }
        super.render(poseStack, n, n2, f);
    }
}

