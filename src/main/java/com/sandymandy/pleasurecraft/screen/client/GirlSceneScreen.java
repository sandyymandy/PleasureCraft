package com.sandymandy.pleasurecraft.screen.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sandymandy.pleasurecraft.networking.C2S.StartSceneC2SPacket;
import com.sandymandy.pleasurecraft.scene.SceneOption;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class GirlSceneScreen extends Screen {
    private final int entityId;
    private final List<SceneOption> options;

    public GirlSceneScreen(int entityId, List<SceneOption> options) {
        super(Text.literal("Scene Options"));
        this.entityId = entityId;
        this.options = options;
    }

    @Override
    protected void init() {
        int y = this.height / 4;
        for (SceneOption option : options) {
            this.addDrawableChild(ButtonWidget.builder(Text.of(option.name()), button -> {
                ClientPlayNetworking.send(new StartSceneC2SPacket(
                        this.entityId,
                        option.introAnim(),
                        option.slowAnim(),
                        option.fastAnim(),
                        option.cumAnim()
                ));
                MinecraftClient.getInstance().setScreen(null); // close after sending
            }).dimensions(this.width / 2 - 100, y, 200, 20).build());
            y += 25;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int alpha = 120; // adjust blur opacity
        context.fillGradient(alpha, 0, 0, this.height, this.width, 0xAA000000, 0xAA000000);
        super.render(context, mouseX, mouseY, delta);
    }

}
