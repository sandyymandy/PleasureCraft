package com.sandymandy.pleasurecraft.networking;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import com.sandymandy.pleasurecraft.networking.C2S.*;
import com.sandymandy.pleasurecraft.networking.S2C.ClothingArmorVisibilityS2CPacket;
import com.sandymandy.pleasurecraft.networking.S2C.SceneOptionsS2CPacket;
import com.sandymandy.pleasurecraft.screen.client.GirlSceneScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;

import java.util.Objects;

public class PleasureCraftPackets {

    public static void registerPackets(){
        // --- C2S (client → server) ---
        PayloadTypeRegistry.playC2S().register(InventoryButtonC2SPacket.ID, InventoryButtonC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(BonePosSyncC2SPacket.ID, BonePosSyncC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(AnimationSyncC2SPacket.ID, AnimationSyncC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(CumKeybindC2SPacket.ID, CumKeybindC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(ThrustKeybindC2SPacket.ID, ThrustKeybindC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(NextSceneAnimationC2SPacket.ID, NextSceneAnimationC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(StartSceneC2SPacket.ID, StartSceneC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(MovementLockStateC2SPacket.ID, MovementLockStateC2SPacket.CODEC);



        // --- S2C (server → client) ---
        PayloadTypeRegistry.playS2C().register(ClothingArmorVisibilityS2CPacket.ID, ClothingArmorVisibilityS2CPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(SceneOptionsS2CPacket.ID, SceneOptionsS2CPacket.CODEC);


    }

    public static void registerC2SPackets(){
        // --- C2S (client → server) ---
        ServerPlayNetworking.registerGlobalReceiver(InventoryButtonC2SPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                            var entity = context.player().getWorld().getEntityById(packet.entityId());
                            if (entity instanceof AbstractGirlEntity girl) {
                                switch (packet.actionId()) {
                                    case "stripOrDressup" -> girl.setStripped(!girl.isStripped());
                                    case "breakUp" -> girl.breakUp(context.player());
                                    case "setBase" -> girl.setBasePosHere();
                                    case "talk" -> ServerPlayNetworking.send(context.player(), new SceneOptionsS2CPacket(girl.getId(), girl.getSceneOptions()));
                                    case "testAnim1" -> girl.playAnimation("downed",false,false);
                                    case "goToBase" -> girl.teleportToBase();
                                    case "sit" -> girl.setSit(!girl.isSittingdown());
                                    case "follow" -> girl.setFollowing(!girl.isFollowing());
                                    default -> PleasureCraft.LOGGER.warn("Unknown Girl interaction: " + packet.actionId());
                                }
                            }
                        }
                ));

        ServerPlayNetworking.registerGlobalReceiver(BonePosSyncC2SPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                            var entity = context.player().getWorld().getEntityById(packet.entityId());
                            if (entity instanceof AbstractGirlEntity girl) {
                                girl.cachePassengerBone(true, packet.position());
                            }

                        }
                ));

        ServerPlayNetworking.registerGlobalReceiver(AnimationSyncC2SPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                            var entity = context.player().getWorld().getEntityById(packet.entityId());
                            if (entity instanceof AbstractGirlEntity girl) {
                                girl.setOverrideAnim(packet.animationState());
                                girl.setOverrideLoop(packet.loopState());
                                girl.setOverrideHold(packet.holdState());
                            }

                        }
                ));

        ServerPlayNetworking.registerGlobalReceiver(CumKeybindC2SPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                    var entity = context.player().getVehicle();
                    if (entity instanceof AbstractGirlEntity girl) {
                        if (packet.pressed()) {
                            girl.getSceneManager().tryTriggerCum();
                        }
                    }
                }));

        ServerPlayNetworking.registerGlobalReceiver(ThrustKeybindC2SPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                    var entity = context.player().getVehicle();
                    if (entity instanceof AbstractGirlEntity girl) {
                        girl.getSceneManager().setKeyHeld(packet.held());
                    }
                }));

        ServerPlayNetworking.registerGlobalReceiver(NextSceneAnimationC2SPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                    var entity = context.player().getWorld().getEntityById(packet.entityId());
                    if (entity instanceof AbstractGirlEntity girl) {
                        girl.getSceneManager().onAnimationFinished(packet.finishedAnimation());
                    }
                }));

        ServerPlayNetworking.registerGlobalReceiver(StartSceneC2SPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                    var entity = context.player().getWorld().getEntityById(packet.entityId());
                    if (entity instanceof AbstractGirlEntity girl) {
                        girl.getSceneManager().startScene(context.player(), packet.introAnim(), packet.slowAnim(),packet.fastAnim(),packet.cumAnim());
                    }
                }));

        ServerPlayNetworking.registerGlobalReceiver(MovementLockStateC2SPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                    var entity = context.player().getWorld().getEntityById(packet.entityId());
                    if (entity instanceof AbstractGirlEntity girl) {
                        girl.setMovementLockedState(packet.data());
                    }
                }));

    }

    public static void registerS2CPackets(){
        // --- S2C (server → client) ---
        ClientPlayNetworking.registerGlobalReceiver(ClothingArmorVisibilityS2CPacket.ID,
                (packet, context) -> context.client().execute(() -> {
                    var world = context.client().world;
                    if (world == null) return;

                    Entity entity = world.getEntityById(packet.entityId());
                    if (entity instanceof AbstractGirlEntity girl) {
                        int i = 0;
                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            girl.clothingVisibility.put(slot, packet.clothing().get(i));
                            girl.armorVisibility.put(slot, packet.armor().get(i));
                            i++;
                        }
                        girl.applyClothingAndArmor();
                    }
                }));

        ClientPlayNetworking.registerGlobalReceiver(SceneOptionsS2CPacket.ID, (packet, context) -> {
            context.client().execute(() -> {
                MinecraftClient.getInstance().setScreen(new GirlSceneScreen(packet.entityId(), packet.options()));
            });
        });

    }

}
