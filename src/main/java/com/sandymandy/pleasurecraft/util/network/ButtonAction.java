package com.sandymandy.pleasurecraft.util.network;

import com.sandymandy.pleasurecraft.util.entity.AbstractGirlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public record ButtonAction(Text label, BiConsumer<AbstractGirlEntity, PlayerEntity> action) {}
