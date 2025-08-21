package com.sandymandy.pleasurecraft;


import com.sandymandy.pleasurecraft.client.PleasureCraftKeybinds;
import com.sandymandy.pleasurecraft.client.renderers.BiaRenderer;
import com.sandymandy.pleasurecraft.client.renderers.LucyRenderer;
import com.sandymandy.pleasurecraft.entity.PleasureCraftEntities;
import com.sandymandy.pleasurecraft.networking.PleasureCraftPackets;
import com.sandymandy.pleasurecraft.networking.C2S.CumKeybindC2SPacket;
import com.sandymandy.pleasurecraft.networking.C2S.ThrustKeybindC2SPacket;
import com.sandymandy.pleasurecraft.screen.client.GirlInventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;



public class PleasureCraftClient implements ClientModInitializer {
//    public static ShaderProgram ENTITY_UNLIT_TRANSLUCENT_SHADER;

    @Override
    public void onInitializeClient() {
        HandledScreens.register(PleasureCraft.GIRL_INVENTORY_SCREEN_HANDLER, GirlInventoryScreen::new);

        EntityRendererRegistry.register(PleasureCraftEntities.LUCY, LucyRenderer::new);
        EntityRendererRegistry.register(PleasureCraftEntities.BIA, BiaRenderer::new);
        PleasureCraftKeybinds.register();
        PleasureCraftPackets.registerS2CPackets();
        handleKeybinds();
    }

    private static void handleKeybinds() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Thrust button held
            boolean thrustHeld = PleasureCraftKeybinds.thrustKey.isPressed();
            ClientPlayNetworking.send(new ThrustKeybindC2SPacket(thrustHeld));

            // Cum button (pressed once)
            ClientPlayNetworking.send(new CumKeybindC2SPacket(PleasureCraftKeybinds.cumKey.wasPressed()));

        });
    }

}
