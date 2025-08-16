package com.sandymandy.pleasurecraft;


import com.sandymandy.pleasurecraft.client.CustomKeybinds;
import com.sandymandy.pleasurecraft.entity.EntityInit;
import com.sandymandy.pleasurecraft.client.renderers.BiaRenderer;
import com.sandymandy.pleasurecraft.client.renderers.LucyRenderer;
import com.sandymandy.pleasurecraft.network.CumKeybindPacket;
import com.sandymandy.pleasurecraft.network.ThrustKeybindPacket;
import com.sandymandy.pleasurecraft.network.girls.AnimationSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import com.sandymandy.pleasurecraft.screen.client.GirlInventoryScreen;



public class PleasureCraftClient implements ClientModInitializer {
//    public static ShaderProgram ENTITY_UNLIT_TRANSLUCENT_SHADER;

    @Override
    public void onInitializeClient() {
        HandledScreens.register(PleasureCraft.GIRL_SCREEN_HANDLER, GirlInventoryScreen::new);

        EntityRendererRegistry.register(EntityInit.LUCY, LucyRenderer::new);
        EntityRendererRegistry.register(EntityInit.BIA, BiaRenderer::new);

        CustomKeybinds.register();

        handleKeybinds();

    }

    private void handleKeybinds() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Thrust button held
            boolean thrustHeld = CustomKeybinds.thrustKey.isPressed();
            sendThrustState(thrustHeld);

            // Cum button (pressed once)
            sendCumTrigger(CustomKeybinds.cumKey.wasPressed());
        });
    }

    private void sendThrustState(boolean held) {
        // tell server whether key is down
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ClientPlayNetworking.send(new ThrustKeybindPacket(held));
        }
    }

    private void sendCumTrigger(boolean pressed) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ClientPlayNetworking.send(new CumKeybindPacket(pressed));
        }
    }

}
