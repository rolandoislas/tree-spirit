package com.rolandoislas.treespirit.potion;

import com.rolandoislas.treespirit.TreeSpirit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class PotionFadingLight extends Potion {
    private ResourceLocation icon = new ResourceLocation(TreeSpirit.MODID, "textures/icons/potion_fading_light.png");

    public PotionFadingLight() {
        super(true, new Color(94, 74, 119).getRGB());
        setRegistryName("fading_light");
        setPotionName("effect." + TreeSpirit.MODID + ".fading_light");
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        // Do nothing
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return false;
    }

    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        mc.renderEngine.bindTexture(icon);
        Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        mc.renderEngine.bindTexture(icon);
        Gui.drawModalRectWithCustomSizedTexture(x + 4, y + 4, 0, 0, 16, 16, 16, 16);
    }
}
