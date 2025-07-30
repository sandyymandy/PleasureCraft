package com.sandymandy.pleasurecraft;


import com.sandymandy.pleasurecraft.entity.EntityInit;
import com.sandymandy.pleasurecraft.entity.bia.client.BiaRenderer;
import com.sandymandy.pleasurecraft.entity.lucy.client.LucyRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import com.sandymandy.pleasurecraft.screen.client.GirlInventoryScreen;



public class PleasureCraftClient implements ClientModInitializer {
//    public static ShaderProgram ENTITY_UNLIT_TRANSLUCENT_SHADER;

    @Override
    public void onInitializeClient() {
        HandledScreens.register(PleasureCraft.GIRL_SCREEN_HANDLER, GirlInventoryScreen::new);

        EntityRendererRegistry.register(EntityInit.LUCY, LucyRenderer::new);
        EntityRendererRegistry.register(EntityInit.BIA, BiaRenderer::new);
    }
//    public static ShaderProgram getEntityUnlitTranslucentShader() {
//        return Objects.requireNonNull(ENTITY_UNLIT_TRANSLUCENT_SHADER, "Shader not loaded yet.");
//    }
}
