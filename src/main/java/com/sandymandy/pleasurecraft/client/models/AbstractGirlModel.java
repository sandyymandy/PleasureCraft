package com.sandymandy.pleasurecraft.client.models;

import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public abstract class AbstractGirlModel<T extends AbstractGirlEntity> extends GeoModel<T> {

    @Override
    public abstract Identifier getModelResource(T animatable, GeoRenderer<T> renderer);

    @Override
    public abstract Identifier getTextureResource(T animatable, GeoRenderer<T> renderer);

    @Override
    public abstract Identifier getAnimationResource(T animatable);

    @Override
    public void setCustomAnimations(T girl, long instanceId, AnimationState<T> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        // Set head rotation
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if (entityData != null) {
                head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
            }
        }

        var headBone = this.getAnimationProcessor().getBone("Head2");
        if (headBone != null) {
            MinecraftClient client = MinecraftClient.getInstance();

            boolean isFirstPerson = client.options.getPerspective().isFirstPerson();
            boolean isPlayerRider = client.cameraEntity == girl.getFirstPassenger();

            headBone.setHidden(isFirstPerson && isPlayerRider);
        }

    }
}
