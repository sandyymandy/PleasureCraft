package com.sandymandy.pleasurecraft.client;

import com.sandymandy.pleasurecraft.network.players.CumKeybindC2SPacket;
import com.sandymandy.pleasurecraft.network.players.ThrustKeybindC2SPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PleasureCraftKeybinds {
    public static KeyBinding thrustKey;
    public static KeyBinding cumKey;


    public static void register() {
        thrustKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pleasurecraft.thrust", // translation key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z, // default key
                "key.categories.pleasurecraft" // translation category
        ));

        cumKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pleasurecraft.cum",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.pleasurecraft"
        ));

  }


}
