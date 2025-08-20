package com.sandymandy.pleasurecraft.screen.client;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import com.sandymandy.pleasurecraft.screen.GirlInventoryScreenHandler;
import com.sandymandy.pleasurecraft.screen.ButtonAction;
import com.sandymandy.pleasurecraft.screen.ButtonRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GirlInventoryScreen extends HandledScreen<GirlInventoryScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(PleasureCraft.MOD_ID, "/textures/gui/inventory.png");
    private float xMouse;
    private float yMouse;
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 170;
    private final AbstractGirlEntity girl;
    private final PlayerEntity player;



    public GirlInventoryScreen(GirlInventoryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.girl = handler.getGirl();
        this.player = inventory.player;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context,mouseX,mouseY);

    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        //Stops the container names from rendering
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int centerX = (width - GUI_WIDTH) / 2;
        int centerY = (height - GUI_HEIGHT) / 2;
        int i = this.x;
        int j = this.y;
        context.drawTexture(RenderLayer::getGuiTextured,TEXTURE, centerX, centerY, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);
        InventoryScreen.drawEntity(context, i + 26, j + 8, i + 75, j + 78, this.girl.getSizeGUI(), this.girl.getYAxisGUI(), mouseX, mouseY, this.girl);
    }

    @Override
    protected void init() {
        super.init();
        int centerX = (this.width - GUI_WIDTH) / 2;
        int centerY = (this.height - GUI_HEIGHT) / 2;

        int buttonHeight = 22;
        int buttonWidth = 80;

        int paddingX = 10;
        int paddingY = 4;

        int startX = centerX - (buttonWidth + paddingX);
        int startY = centerY + 15;

        if (girl.isTamed()){
            for (int i = 0; i < ButtonRegistry.BUTTONS_LEFT.size(); i++) {
                ButtonAction action = ButtonRegistry.BUTTONS_LEFT.get(i);
                int y = startY + i * (buttonHeight + paddingY);
                Text dynamicLabel = action.label();

                if (action.label().getString().equals("Strip") && girl.isStripped()) {
                    dynamicLabel = Text.literal("Dress Up");
                }

                this.addDrawableChild(ButtonWidget.builder(
                    dynamicLabel,
                    btn -> {
                        if (girl != null && client != null && player != null) {
                            action.action().accept(girl, player);  // Run the button's logic
                            this.client.setScreen(null);
                        }
                    }
                ).dimensions(startX, y, buttonWidth, buttonHeight).build());
            }

            for (int i = 0; i < ButtonRegistry.BUTTONS_RIGHT.size(); i++) {
                ButtonAction action = ButtonRegistry.BUTTONS_RIGHT.get(i);
                int y = startY + i * (buttonHeight + paddingY);
                Text dynamicLabel = action.label();

                if (action.label().getString().equals("Sit") && girl.isSittingdown()){
                    dynamicLabel = Text.literal("Stand");
                }
                else if (action.label().getString().equals("Follow Me") && girl.isFollowing()){
                    dynamicLabel = Text.literal("Stop Following");
                }
                else if (action.label().getString().equals("Freeze") && girl.isFrozenInPlace()){
                    dynamicLabel = Text.literal("Unfreeze");
                }

                this.addDrawableChild(ButtonWidget.builder(
                    dynamicLabel,
                    btn -> {
                        if (girl != null && client != null && player != null) {
                            action.action().accept(girl, player);  // Run the button's logic
                            this.client.setScreen(null);
                        }
                    }
                ).dimensions(centerX + 176 + paddingX, y, buttonWidth, buttonHeight).build());
            }
        }

    }

}
