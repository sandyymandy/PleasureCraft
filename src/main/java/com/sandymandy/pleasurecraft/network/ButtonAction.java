package com.sandymandy.pleasurecraft.network;

import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public record ButtonAction(Text label, BiConsumer<AbstractGirlEntity, PlayerEntity> action) {}
