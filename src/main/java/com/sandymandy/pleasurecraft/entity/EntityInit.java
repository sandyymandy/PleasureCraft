package com.sandymandy.pleasurecraft.entity;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import com.sandymandy.pleasurecraft.entity.girls.BiaEntity;
import com.sandymandy.pleasurecraft.entity.girls.LucyEntity;
import com.sandymandy.pleasurecraft.network.AnimationSyncPacket;
import com.sandymandy.pleasurecraft.network.BonePosSyncPacket;
import com.sandymandy.pleasurecraft.network.ButtonPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class EntityInit {
    public static final Identifier LUCY_ID = Identifier.of(PleasureCraft.MOD_ID, "lucy");
    public static final Identifier BIA_ID = Identifier.of(PleasureCraft.MOD_ID, "bia");

    private static final RegistryKey<EntityType<?>> LUCY_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(PleasureCraft.MOD_ID, "bia"));

    private static final RegistryKey<EntityType<?>> BIA_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(PleasureCraft.MOD_ID, "bia"));

    public static final EntityType<LucyEntity> LUCY = Registry.register(
            Registries.ENTITY_TYPE,
            LUCY_ID,
            EntityType.Builder.create(LucyEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.5f, 1.95f)
                    .build(LUCY_KEY)
    );

    public static final EntityType<BiaEntity> BIA = Registry.register(
            Registries.ENTITY_TYPE,
            BIA_ID,
            EntityType.Builder.create(BiaEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.5f, 1.65f)
                    .build(BIA_KEY)
    );

    public static void init() {
        // Attributes
        FabricDefaultAttributeRegistry.register(LUCY, LucyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(BIA, BiaEntity.createAttributes());

        // Network
        PayloadTypeRegistry.playC2S().register(ButtonPacket.ID, ButtonPacket.CODEC);

        PayloadTypeRegistry.playC2S().register(BonePosSyncPacket.ID, BonePosSyncPacket.CODEC);

        PayloadTypeRegistry.playC2S().register(AnimationSyncPacket.ID, AnimationSyncPacket.CODEC);



        ServerPlayNetworking.registerGlobalReceiver(ButtonPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                    var entity = context.player().getWorld().getEntityById(packet.entityId());
                    if (entity instanceof AbstractGirlEntity girl) {
                        switch (packet.actionId()) {
                            case "stripOrDressup" -> girl.setStripped(!girl.isStripped());
                            case "breakUp" -> girl.breakUp(context.player());
                            case "setBase" -> girl.setBasePosHere();
                            case "sex" -> girl.getSceneManager().startScene(context.player());
                            case "talk" -> girl.messageAsEntity(context.player(),"Hello");
                            case "testAnim1" -> girl.playAnimation("ride",true);
                            case "testAnim2" -> girl.playAnimation("downed",false);
                            case "goToBase" -> girl.teleportToBase();
                            case "sit" -> girl.setSit(!girl.isSittingdown());
                            case "follow" -> girl.setFollowing(!girl.isFollowing());
                            default -> PleasureCraft.LOGGER.warn("Unknown Girl interaction: " + packet.actionId());
                        }
                    }
                }
        ));

        ServerPlayNetworking.registerGlobalReceiver(BonePosSyncPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                    var entity = context.player().getWorld().getEntityById(packet.entityId());
                    if (entity instanceof AbstractGirlEntity girl) {
                        girl.cachePassengerBone(true, packet.position());
                    }

                }
        ));

        ServerPlayNetworking.registerGlobalReceiver(AnimationSyncPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                            var entity = context.player().getWorld().getEntityById(packet.entityId());
                            if (entity instanceof AbstractGirlEntity girl) {
                                girl.setOverrideAnim(packet.animationState());
                            }

                        }
                ));

    }
}

