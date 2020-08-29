/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 *  com.google.common.collect.Ordering
 *  com.mojang.authlib.GameProfile
 *  javax.annotation.Nullable
 */
package net.minecraft.client.gui.components;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class PlayerTabOverlay
extends GuiComponent {
    private static final Ordering<PlayerInfo> PLAYER_ORDERING = Ordering.from((Comparator)new PlayerInfoComparator());
    private final Minecraft minecraft;
    private final Gui gui;
    private Component footer;
    private Component header;
    private long visibilityId;
    private boolean visible;

    public PlayerTabOverlay(Minecraft minecraft, Gui gui) {
        this.minecraft = minecraft;
        this.gui = gui;
    }

    public Component getNameForDisplay(PlayerInfo playerInfo) {
        if (playerInfo.getTabListDisplayName() != null) {
            return this.decorateName(playerInfo, playerInfo.getTabListDisplayName().copy());
        }
        return this.decorateName(playerInfo, PlayerTeam.formatNameForTeam(playerInfo.getTeam(), new TextComponent(playerInfo.getProfile().getName())));
    }

    private Component decorateName(PlayerInfo playerInfo, MutableComponent mutableComponent) {
        return playerInfo.getGameMode() == GameType.SPECTATOR ? mutableComponent.withStyle(ChatFormatting.ITALIC) : mutableComponent;
    }

    public void setVisible(boolean bl) {
        if (bl && !this.visible) {
            this.visibilityId = Util.getMillis();
        }
        this.visible = bl;
    }

    public void render(PoseStack poseStack, int n, Scoreboard scoreboard, @Nullable Objective objective) {
        int n2;
        int n3;
        boolean bl;
        int n4;
        int n5;
        ClientPacketListener clientPacketListener = this.minecraft.player.connection;
        List list = PLAYER_ORDERING.sortedCopy(clientPacketListener.getOnlinePlayers());
        int n6 = 0;
        int n7 = 0;
        for (PlayerInfo playerInfo : list) {
            n5 = this.minecraft.font.width(this.getNameForDisplay(playerInfo));
            n6 = Math.max(n6, n5);
            if (objective == null || objective.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) continue;
            n5 = this.minecraft.font.width(" " + scoreboard.getOrCreatePlayerScore(playerInfo.getProfile().getName(), objective).getScore());
            n7 = Math.max(n7, n5);
        }
        list = list.subList(0, Math.min(list.size(), 80));
        int n8 = n4 = list.size();
        n5 = 1;
        while (n8 > 20) {
            n8 = (n4 + ++n5 - 1) / n5;
        }
        boolean bl2 = bl = this.minecraft.isLocalServer() || this.minecraft.getConnection().getConnection().isEncrypted();
        int n9 = objective != null ? (objective.getRenderType() == ObjectiveCriteria.RenderType.HEARTS ? 90 : n7) : 0;
        int n10 = Math.min(n5 * ((bl ? 9 : 0) + n6 + n9 + 13), n - 50) / n5;
        int n11 = n / 2 - (n10 * n5 + (n5 - 1) * 5) / 2;
        int n12 = 10;
        int n13 = n10 * n5 + (n5 - 1) * 5;
        List<FormattedCharSequence> list2 = null;
        if (this.header != null) {
            list2 = this.minecraft.font.split(this.header, n - 50);
            for (FormattedCharSequence iterator : list2) {
                n13 = Math.max(n13, this.minecraft.font.width(iterator));
            }
        }
        Object object = null;
        if (this.footer != null) {
            object = this.minecraft.font.split(this.footer, n - 50);
            Iterator n17 = object.iterator();
            while (n17.hasNext()) {
                FormattedCharSequence formattedCharSequence = (FormattedCharSequence)n17.next();
                n13 = Math.max(n13, this.minecraft.font.width(formattedCharSequence));
            }
        }
        if (list2 != null) {
            int n14 = n / 2 - n13 / 2 - 1;
            int n15 = n / 2 + n13 / 2 + 1;
            int n16 = list2.size();
            this.minecraft.font.getClass();
            PlayerTabOverlay.fill(poseStack, n14, n12 - 1, n15, n12 + n16 * 9, Integer.MIN_VALUE);
            for (FormattedCharSequence formattedCharSequence : list2) {
                n3 = this.minecraft.font.width(formattedCharSequence);
                this.minecraft.font.drawShadow(poseStack, formattedCharSequence, (float)(n / 2 - n3 / 2), (float)n12, -1);
                this.minecraft.font.getClass();
                n12 += 9;
            }
            ++n12;
        }
        PlayerTabOverlay.fill(poseStack, n / 2 - n13 / 2 - 1, n12 - 1, n / 2 + n13 / 2 + 1, n12 + n8 * 9, Integer.MIN_VALUE);
        int n17 = this.minecraft.options.getBackgroundColor(0x20FFFFFF);
        for (int i = 0; i < n4; ++i) {
            int n18;
            int n19;
            n3 = i / n8;
            n2 = i % n8;
            int n20 = n11 + n3 * n10 + n3 * 5;
            int n21 = n12 + n2 * 9;
            PlayerTabOverlay.fill(poseStack, n20, n21, n20 + n10, n21 + 8, n17);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            if (i >= list.size()) continue;
            PlayerInfo playerInfo = (PlayerInfo)list.get(i);
            GameProfile gameProfile = playerInfo.getProfile();
            if (bl) {
                Player player = this.minecraft.level.getPlayerByUUID(gameProfile.getId());
                n19 = player != null && player.isModelPartShown(PlayerModelPart.CAPE) && ("Dinnerbone".equals(gameProfile.getName()) || "Grumm".equals(gameProfile.getName())) ? 1 : 0;
                this.minecraft.getTextureManager().bind(playerInfo.getSkinLocation());
                int n22 = 8 + (n19 != 0 ? 8 : 0);
                int n23 = 8 * (n19 != 0 ? -1 : 1);
                GuiComponent.blit(poseStack, n20, n21, 8, 8, 8.0f, n22, 8, n23, 64, 64);
                if (player != null && player.isModelPartShown(PlayerModelPart.HAT)) {
                    int n24 = 8 + (n19 != 0 ? 8 : 0);
                    int n25 = 8 * (n19 != 0 ? -1 : 1);
                    GuiComponent.blit(poseStack, n20, n21, 8, 8, 40.0f, n24, 8, n25, 64, 64);
                }
                n20 += 9;
            }
            this.minecraft.font.drawShadow(poseStack, this.getNameForDisplay(playerInfo), (float)n20, (float)n21, playerInfo.getGameMode() == GameType.SPECTATOR ? -1862270977 : -1);
            if (objective != null && playerInfo.getGameMode() != GameType.SPECTATOR && (n19 = (n18 = n20 + n6 + 1) + n9) - n18 > 5) {
                this.renderTablistScore(objective, n21, gameProfile.getName(), n18, n19, playerInfo, poseStack);
            }
            this.renderPingIcon(poseStack, n10, n20 - (bl ? 9 : 0), n21, playerInfo);
        }
        if (object != null) {
            int n26 = n / 2 - n13 / 2 - 1;
            int n27 = n / 2 + n13 / 2 + 1;
            int n28 = object.size();
            this.minecraft.font.getClass();
            PlayerTabOverlay.fill(poseStack, n26, (n12 += n8 * 9 + 1) - 1, n27, n12 + n28 * 9, Integer.MIN_VALUE);
            Iterator iterator = object.iterator();
            while (iterator.hasNext()) {
                FormattedCharSequence formattedCharSequence = (FormattedCharSequence)iterator.next();
                n2 = this.minecraft.font.width(formattedCharSequence);
                this.minecraft.font.drawShadow(poseStack, formattedCharSequence, (float)(n / 2 - n2 / 2), (float)n12, -1);
                this.minecraft.font.getClass();
                n12 += 9;
            }
        }
    }

    protected void renderPingIcon(PoseStack poseStack, int n, int n2, int n3, PlayerInfo playerInfo) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(GUI_ICONS_LOCATION);
        boolean bl = false;
        int n4 = playerInfo.getLatency() < 0 ? 5 : (playerInfo.getLatency() < 150 ? 0 : (playerInfo.getLatency() < 300 ? 1 : (playerInfo.getLatency() < 600 ? 2 : (playerInfo.getLatency() < 1000 ? 3 : 4))));
        this.setBlitOffset(this.getBlitOffset() + 100);
        this.blit(poseStack, n2 + n - 11, n3, 0, 176 + n4 * 8, 10, 8);
        this.setBlitOffset(this.getBlitOffset() - 100);
    }

    private void renderTablistScore(Objective objective, int n, String string, int n2, int n3, PlayerInfo playerInfo, PoseStack poseStack) {
        int n4 = objective.getScoreboard().getOrCreatePlayerScore(string, objective).getScore();
        if (objective.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) {
            boolean bl;
            this.minecraft.getTextureManager().bind(GUI_ICONS_LOCATION);
            long l = Util.getMillis();
            if (this.visibilityId == playerInfo.getRenderVisibilityId()) {
                if (n4 < playerInfo.getLastHealth()) {
                    playerInfo.setLastHealthTime(l);
                    playerInfo.setHealthBlinkTime(this.gui.getGuiTicks() + 20);
                } else if (n4 > playerInfo.getLastHealth()) {
                    playerInfo.setLastHealthTime(l);
                    playerInfo.setHealthBlinkTime(this.gui.getGuiTicks() + 10);
                }
            }
            if (l - playerInfo.getLastHealthTime() > 1000L || this.visibilityId != playerInfo.getRenderVisibilityId()) {
                playerInfo.setLastHealth(n4);
                playerInfo.setDisplayHealth(n4);
                playerInfo.setLastHealthTime(l);
            }
            playerInfo.setRenderVisibilityId(this.visibilityId);
            playerInfo.setLastHealth(n4);
            int n5 = Mth.ceil((float)Math.max(n4, playerInfo.getDisplayHealth()) / 2.0f);
            int n6 = Math.max(Mth.ceil(n4 / 2), Math.max(Mth.ceil(playerInfo.getDisplayHealth() / 2), 10));
            boolean bl2 = bl = playerInfo.getHealthBlinkTime() > (long)this.gui.getGuiTicks() && (playerInfo.getHealthBlinkTime() - (long)this.gui.getGuiTicks()) / 3L % 2L == 1L;
            if (n5 > 0) {
                int n7 = Mth.floor(Math.min((float)(n3 - n2 - 4) / (float)n6, 9.0f));
                if (n7 > 3) {
                    int n8;
                    for (n8 = n5; n8 < n6; ++n8) {
                        this.blit(poseStack, n2 + n8 * n7, n, bl ? 25 : 16, 0, 9, 9);
                    }
                    for (n8 = 0; n8 < n5; ++n8) {
                        this.blit(poseStack, n2 + n8 * n7, n, bl ? 25 : 16, 0, 9, 9);
                        if (bl) {
                            if (n8 * 2 + 1 < playerInfo.getDisplayHealth()) {
                                this.blit(poseStack, n2 + n8 * n7, n, 70, 0, 9, 9);
                            }
                            if (n8 * 2 + 1 == playerInfo.getDisplayHealth()) {
                                this.blit(poseStack, n2 + n8 * n7, n, 79, 0, 9, 9);
                            }
                        }
                        if (n8 * 2 + 1 < n4) {
                            this.blit(poseStack, n2 + n8 * n7, n, n8 >= 10 ? 160 : 52, 0, 9, 9);
                        }
                        if (n8 * 2 + 1 != n4) continue;
                        this.blit(poseStack, n2 + n8 * n7, n, n8 >= 10 ? 169 : 61, 0, 9, 9);
                    }
                } else {
                    float f = Mth.clamp((float)n4 / 20.0f, 0.0f, 1.0f);
                    int n9 = (int)((1.0f - f) * 255.0f) << 16 | (int)(f * 255.0f) << 8;
                    String string2 = "" + (float)n4 / 2.0f;
                    if (n3 - this.minecraft.font.width(string2 + "hp") >= n2) {
                        string2 = string2 + "hp";
                    }
                    this.minecraft.font.drawShadow(poseStack, string2, (float)((n3 + n2) / 2 - this.minecraft.font.width(string2) / 2), (float)n, n9);
                }
            }
        } else {
            String string3 = (Object)((Object)ChatFormatting.YELLOW) + "" + n4;
            this.minecraft.font.drawShadow(poseStack, string3, (float)(n3 - this.minecraft.font.width(string3)), (float)n, 0xFFFFFF);
        }
    }

    public void setFooter(@Nullable Component component) {
        this.footer = component;
    }

    public void setHeader(@Nullable Component component) {
        this.header = component;
    }

    public void reset() {
        this.header = null;
        this.footer = null;
    }

    static class PlayerInfoComparator
    implements Comparator<PlayerInfo> {
        private PlayerInfoComparator() {
        }

        @Override
        public int compare(PlayerInfo playerInfo, PlayerInfo playerInfo2) {
            PlayerTeam playerTeam = playerInfo.getTeam();
            PlayerTeam playerTeam2 = playerInfo2.getTeam();
            return ComparisonChain.start().compareTrueFirst(playerInfo.getGameMode() != GameType.SPECTATOR, playerInfo2.getGameMode() != GameType.SPECTATOR).compare((Comparable)((Object)(playerTeam != null ? playerTeam.getName() : "")), (Comparable)((Object)(playerTeam2 != null ? playerTeam2.getName() : ""))).compare((Object)playerInfo.getProfile().getName(), (Object)playerInfo2.getProfile().getName(), String::compareToIgnoreCase).result();
        }

        @Override
        public /* synthetic */ int compare(Object object, Object object2) {
            return this.compare((PlayerInfo)object, (PlayerInfo)object2);
        }
    }
}

