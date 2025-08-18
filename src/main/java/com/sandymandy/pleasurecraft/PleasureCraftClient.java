package com.sandymandy.pleasurecraft;


import com.sandymandy.pleasurecraft.client.PleasureCraftKeybinds;
import com.sandymandy.pleasurecraft.client.renderers.BiaRenderer;
import com.sandymandy.pleasurecraft.client.renderers.LucyRenderer;
import com.sandymandy.pleasurecraft.entity.PleasureCraftEntity;
import com.sandymandy.pleasurecraft.network.PleasureCraftPackets;
import com.sandymandy.pleasurecraft.screen.client.GirlInventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;



public class PleasureCraftClient implements ClientModInitializer {
//    public static ShaderProgram ENTITY_UNLIT_TRANSLUCENT_SHADER;

    @Override
    public void onInitializeClient() {
        HandledScreens.register(PleasureCraft.GIRL_SCREEN_HANDLER, GirlInventoryScreen::new);

        EntityRendererRegistry.register(PleasureCraftEntity.LUCY, LucyRenderer::new);
        EntityRendererRegistry.register(PleasureCraftEntity.BIA, BiaRenderer::new);
        PleasureCraftKeybinds.register();
        PleasureCraftPackets.registerS2CPackets();


    }

}
