package com.sandymandy.pleasurecraft.entity.lucy.client;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.util.entity.client.AbstractGirlModel;
import com.sandymandy.pleasurecraft.entity.lucy.LucyEntity;
import com.sandymandy.pleasurecraft.util.jigglePhysics.JiggleBoneConfig;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected List<JiggleBoneConfig> JIGGLE_BONES(LucyEntity lucy) {
        List<JiggleBoneConfig> bones = new ArrayList<>(super.JIGGLE_BONES(lucy));

        if(lucy.isStripped()) {
            bones.add(new JiggleBoneConfig("boobL", 0.2, 0.3));
            bones.add(new JiggleBoneConfig("boobR", 0.2, 0.3));
        }
        return bones;
    }

}