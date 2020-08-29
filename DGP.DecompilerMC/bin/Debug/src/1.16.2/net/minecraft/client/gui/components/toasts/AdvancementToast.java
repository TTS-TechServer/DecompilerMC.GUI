/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.components.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class AdvancementToast
implements Toast {
    private final Advancement advancement;
    private boolean playedSound;

    public AdvancementToast(Advancement advancement) {
        this.advancement = advancement;
    }

    @Override
    public Toast.Visibility render(PoseStack poseStack, ToastComponent toastComponent, long l) {
        toastComponent.getMinecraft().getTextureManager().bind(TEXTURE);
        RenderSystem.color3f(1.0f, 1.0f, 1.0f);
        DisplayInfo displayInfo = this.advancement.getDisplay();
        toastComponent.blit(poseStack, 0, 0, 0, 0, this.width(), this.height());
        if (displayInfo != null) {
            int n;
            List<FormattedCharSequence> list = toastComponent.getMinecraft().font.split(displayInfo.getTitle(), 125);
            int n2 = n = displayInfo.getFrame() == FrameType.CHALLENGE ? 0xFF88FF : 0xFFFF00;
            if (list.size() == 1) {
                toastComponent.getMinecraft().font.draw(poseStack, displayInfo.getFrame().getDisplayName(), 30.0f, 7.0f, n | 0xFF000000);
                toastComponent.getMinecraft().font.draw(poseStack, list.get(0), 30.0f, 18.0f, -1);
            } else {
                int n3 = 1500;
                float f = 300.0f;
                if (l < 1500L) {
                    int n4 = Mth.floor(Mth.clamp((float)(1500L - l) / 300.0f, 0.0f, 1.0f) * 255.0f) << 24 | 0x4000000;
                    toastComponent.getMinecraft().font.draw(poseStack, displayInfo.getFrame().getDisplayName(), 30.0f, 11.0f, n | n4);
                } else {
                    int n5 = Mth.floor(Mth.clamp((float)(l - 1500L) / 300.0f, 0.0f, 1.0f) * 252.0f) << 24 | 0x4000000;
                    int n6 = this.height() / 2;
                    int n7 = list.size();
                    toastComponent.getMinecraft().font.getClass();
                    int n8 = n6 - n7 * 9 / 2;
                    for (FormattedCharSequence formattedCharSequence : list) {
                        toastComponent.getMinecraft().font.draw(poseStack, formattedCharSequence, 30.0f, (float)n8, 0xFFFFFF | n5);
                        toastComponent.getMinecraft().font.getClass();
                        n8 += 9;
                    }
                }
            }
            if (!this.playedSound && l > 0L) {
                this.playedSound = true;
                if (displayInfo.getFrame() == FrameType.CHALLENGE) {
                    toastComponent.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f));
                }
            }
            toastComponent.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(displayInfo.getIcon(), 8, 8);
            return l >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        }
        return Toast.Visibility.HIDE;
    }
}

