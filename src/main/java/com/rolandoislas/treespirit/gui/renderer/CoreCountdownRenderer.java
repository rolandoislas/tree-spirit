package com.rolandoislas.treespirit.gui.renderer;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Rolando on 3/1/2017.
 */
public class CoreCountdownRenderer {
	private static final ResourceLocation TEXTURE_FOG;
	private static final ResourceLocation TEXTURE_PUMPKIN;
	private static final ResourceLocation TEXTURE_BAR;
	private static boolean render;
	private static int deathTime;
	private static long ticks;

	static {
		TEXTURE_FOG = new ResourceLocation(TreeSpirit.MODID, "textures/gui/countdown.png");
		TEXTURE_PUMPKIN = new ResourceLocation("minecraft", "textures/misc/pumpkinblur.png");
		TEXTURE_BAR = new ResourceLocation("minecraft", "textures/gui/bars.png");
	}

	/**
	 * Start a countdown
	 * @param deathTime seconds before death
	 * @param elapsedTicks ticks that have passed
	 */
	public static void startCountdown(int deathTime, long elapsedTicks) {
		CoreCountdownRenderer.deathTime = deathTime;
		render = true;
		ticks = elapsedTicks;
	}

	public static void render(RenderGameOverlayEvent event) {
		if (!event.getType().equals(RenderGameOverlayEvent.ElementType.ALL) || !render)
			return;
		switch (Config.deathWarningType) {
			case FOG:
				renderFog(event);
				break;
			case PUMPKIN:
				renderPumpkin(event);
				break;
			case BAR:
				renderBar(event);
				break;
		}
	}

	private static void renderBar(RenderGameOverlayEvent event) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_BAR);
		float percent = ticks / (deathTime * 40f);
		GlStateManager.color(1, 1, 1);
		int x = event.getResolution().getScaledWidth() / 2 - 182 / 2;
		int y = 5;
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, 0, 30, 182, 5);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, 0, 35,
				(int) (182 - 182 * percent), 5);
	}

	private static void renderPumpkin(RenderGameOverlayEvent event) {
		float opacity = ticks / (deathTime * 40f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_PUMPKIN);
		GlStateManager.color(1, 1, 1, opacity);
		GlStateManager.enableBlend();
		Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 256, 256,
				event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(),
				256, 256);
	}

	private static void renderFog(RenderGameOverlayEvent event) {
		float opacity = ticks / (deathTime * 40f);
		float color = opacity - .3f;
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_FOG);
		GlStateManager.color(2 - color, 1 - color, 1 - color, opacity);
		GlStateManager.enableBlend();
		Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 256, 256,
				event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(),
				256, 256);
	}

	public static void stopCountdown() {
		render = false;
		ticks = 0;
	}

	public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
		if (render && event.player == Minecraft.getMinecraft().player)
			ticks++;
	}
}
