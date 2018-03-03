package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.potion.PotionFadingLight;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Potions {
    public static final Potion FADING_LIGHT = new PotionFadingLight();

    public static void register() {
        // Potions
        ForgeRegistries.POTIONS.register(FADING_LIGHT);
    }
}
