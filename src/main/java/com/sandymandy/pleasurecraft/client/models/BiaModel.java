package com.sandymandy.pleasurecraft.client.models;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.entity.girls.BiaEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BiaModel extends AbstractGirlModel<BiaEntity> {

    @Override
    public Identifier getModelResource(BiaEntity entity, GeoRenderer<BiaEntity> renderer) {
        return Identifier.of(PleasureCraft.MOD_ID, "geo/bia.geo.json");
    }

    @Override
    public Identifier getTextureResource(BiaEntity entity, GeoRenderer<BiaEntity> renderer) {
        return Identifier.of(PleasureCraft.MOD_ID, "textures/entities/bia.png");
    }

    @Override
    public Identifier getAnimationResource(BiaEntity entity) {
        return Identifier.of(PleasureCraft.MOD_ID, "animations/bia.animation.json");
    }

}