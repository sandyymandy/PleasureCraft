package com.sandymandy.pleasurecraft.util.entity.client;

import com.sandymandy.pleasurecraft.util.entity.AbstractGirlEntity;
import com.sandymandy.pleasurecraft.util.jigglePhysics.JiggleBoneConfig;
import com.sandymandy.pleasurecraft.util.jigglePhysics.JigglePhysics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGirlModel<T extends AbstractGirlEntity> extends GeoModel<T> {
    private final Map<Long, Map<String, JigglePhysics>> jiggleMapByEntity = new HashMap<>();
    private final Map<Long, Map<String, Vec3d>> defaultRotationsByEntity = new HashMap<>();

    @Override
    public abstract Identifier getModelResource(T animatable, GeoRenderer<T> renderer);

    @Override
    public abstract Identifier getTextureResource(T animatable, GeoRenderer<T> renderer);

    @Override
    public abstract Identifier getAnimationResource(T animatable);

    private void applyJiggle(String boneKey, GeoBone bone, Vec3d motion, Map<String, JigglePhysics> jMap, Map<String, Vec3d> dMap) {
        JigglePhysics jiggle = jMap.get(boneKey);
        Vec3d defaultRot = dMap.get(boneKey);
        if (jiggle == null || defaultRot == null) return;

        jiggle.update(motion);
        Vec3d offset = jiggle.getDisplacement();

        bone.setRotX((float) (defaultRot.x + offset.x));
        bone.setRotY((float) (defaultRot.y + offset.y));
        bone.setRotZ((float) (defaultRot.z + offset.z));
    }

    protected List<JiggleBoneConfig> JIGGLE_BONES(T entity){
        List<JiggleBoneConfig> bones = new ArrayList<>();

        bones.add(new JiggleBoneConfig("cheekL", 0.2, 0.1));
        bones.add(new JiggleBoneConfig("cheekR", 0.2, 0.1));

        if(!entity.isStripped()){
            bones.add(new JiggleBoneConfig("boobs", 0.2, 0.3));
        }

        return bones;
    }


    @Override
    public void setCustomAnimations(T girl, long instanceId, AnimationState<T> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        float currentYaw = girl.getYaw();
        float yawDelta = currentYaw - girl.previousYaw;
        if (yawDelta > 180) yawDelta -= 360;
        if (yawDelta < -180) yawDelta += 360;

        Vec3d velocity = girl.getVelocity();
        Vec3d deltaVelocity = velocity.subtract(girl.previousVelocity);

        double yawInfluenceX = Math.sin(Math.toRadians(currentYaw)) * yawDelta * 0.05;
        double yawInfluenceZ = Math.cos(Math.toRadians(currentYaw)) * yawDelta * 0.05;

        Vec3d inertiaForce = deltaVelocity.multiply(1.2).add(new Vec3d(yawInfluenceX, 0, yawInfluenceZ));

        // Init maps
        jiggleMapByEntity.putIfAbsent(instanceId, new HashMap<>());
        defaultRotationsByEntity.putIfAbsent(instanceId, new HashMap<>());

        Map<String, JigglePhysics> jiggleMap = jiggleMapByEntity.get(instanceId);
        Map<String, Vec3d> defaultRotations = defaultRotationsByEntity.get(instanceId);

        // Iterate bones
        for (JiggleBoneConfig config : JIGGLE_BONES(girl)) {
            GeoBone bone = getAnimationProcessor().getBone(config.boneName());
            if (bone == null) continue;

            // Save default rotation once
            defaultRotations.putIfAbsent(config.boneName(), new Vec3d(bone.getRotX(), bone.getRotY(), bone.getRotZ()));

            // Setup physics object once
            jiggleMap.putIfAbsent(config.boneName(), new JigglePhysics(config.stiffness(), config.damping()));

            // Apply jiggle
            applyJiggle(config.boneName(), bone, inertiaForce, jiggleMap, defaultRotations);
        }


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
