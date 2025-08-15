package com.sandymandy.pleasurecraft.client.models;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.entity.girls.BiaEntity;
import com.sandymandy.pleasurecraft.util.jigglePhysics.JiggleBoneConfig;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected List<JiggleBoneConfig> JIGGLE_BONES(BiaEntity bia) {
        List<JiggleBoneConfig> bones = new ArrayList<>(super.JIGGLE_BONES(bia));

        if(bia.isStripped()) {
            bones.add(new JiggleBoneConfig("bra", 0.2, 0.3));
        }
        return bones;
    }
}