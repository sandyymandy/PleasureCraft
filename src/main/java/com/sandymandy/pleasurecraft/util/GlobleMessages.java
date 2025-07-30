package com.sandymandy.pleasurecraft.util;

import net.minecraft.text.Text;
import net.minecraft.world.World;

public class GlobleMessages {
    public void GlobleMessage(World world, String messageContent) {
        Text message = Text.literal(messageContent);
        if (world.isClient) return; // Don't run on client


        world.getServer()
                .getPlayerManager()
                .broadcast(message, false);
    }

}
