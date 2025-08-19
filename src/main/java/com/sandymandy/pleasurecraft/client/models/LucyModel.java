package com.sandymandy.pleasurecraft.client.models;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.entity.girls.LucyEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoRenderer;

public class LucyModel extends AbstractGirlModel<LucyEntity> {

    @Override
    public Identifier getModelResource(LucyEntity entity, GeoRenderer<LucyEntity> renderer) {
        return Identifier.of(PleasureCraft.MOD_ID, "geo/lucy.geo.json");
    }

    @Override
    public Identifier getTextureResource(LucyEntity entity, GeoRenderer<LucyEntity> renderer) {
        return Identifier.of(PleasureCraft.MOD_ID, "textures/entities/lucy.png");
    }

    @Override
    public Identifier getAnimationResource(LucyEntity entity) {
        return Identifier.of(PleasureCraft.MOD_ID, "animations/lucy.animation.json");
    }



}