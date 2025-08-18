package com.sandymandy.pleasurecraft.client.renderers;

import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import java.util.Map;

public abstract class AbstractGirlRenderer<T extends AbstractGirlEntity> extends GeoEntityRenderer<T> {
    public AbstractGirlRenderer(EntityRendererFactory.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    @Override
    public void defaultRender(MatrixStack poseStack, T animatable, VertexConsumerProvider bufferSource, @Nullable RenderLayer renderType, @Nullable VertexConsumer buffer, float partialTick, int packedLight) {
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, partialTick, packedLight);
    }

    @Override
    public void preRender(MatrixStack poseStack, T entity, BakedGeoModel model,
                          VertexConsumerProvider bufferSource, VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight,
                          int packedOverlay, int color) {

        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender,
                partialTick, packedLight, packedOverlay, color);

        if (entity.boneVisibility != null) {
            for (Map.Entry<String, Boolean> entry : entity.boneVisibility.entrySet()) {
                String boneName = entry.getKey();
                boolean isVisible = entry.getValue();

                model.getBone(boneName).ifPresent(bone -> {
                    bone.setHidden(!isVisible);
                    bone.setChildrenHidden(!isVisible);
                });
            }
        }
    }

    @Override
    public void renderFinal(MatrixStack matrices, T entity, BakedGeoModel model,
                            VertexConsumerProvider vertexConsumers, VertexConsumer vertexConsumer,
                            float tickDelta, int light, int overlay, int color) {
        super.renderFinal(matrices, entity, model, vertexConsumers, vertexConsumer,
                tickDelta, light, overlay, color);

        entity.handlePassengerBone(getGeoModel().getBone(entity.getSceneManager().passengerBoneName).get());

    }


}
