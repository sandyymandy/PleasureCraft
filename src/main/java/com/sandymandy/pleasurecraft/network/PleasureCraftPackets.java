package com.sandymandy.pleasurecraft.network;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import com.sandymandy.pleasurecraft.network.girls.AnimationSyncC2SPacket;
import com.sandymandy.pleasurecraft.network.girls.BonePosSyncC2SPacket;
import com.sandymandy.pleasurecraft.network.girls.ButtonC2SPacket;
import com.sandymandy.pleasurecraft.network.girls.ClothingArmorVisibilityS2CPacket;
import com.sandymandy.pleasurecraft.network.players.CumKeybindC2SPacket;
import com.sandymandy.pleasurecraft.network.players.ThrustKeybindC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;

import java.util.Objects;

public class PleasureCraftPackets {

    public static void registerPackets(){
        // --- C2S (client → server) ---
        PayloadTypeRegistry.playC2S().register(ButtonC2SPacket.ID, ButtonC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(BonePosSyncC2SPacket.ID, BonePosSyncC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(AnimationSyncC2SPacket.ID, AnimationSyncC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(CumKeybindC2SPacket.ID, CumKeybindC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(ThrustKeybindC2SPacket.ID, ThrustKeybindC2SPacket.CODEC);

        // --- S2C (server → client) ---
        PayloadTypeRegistry.playS2C().register(ClothingArmorVisibilityS2CPacket.ID, ClothingArmorVisibilityS2CPacket.CODEC);

    }

    public static void registerC2SPackets(){
        // --- C2S (client → server) ---
        ServerPlayNetworking.registerGlobalReceiver(ButtonC2SPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                            var entity = context.player().getWorld().getEntityById(packet.entityId());
                            if (entity instanceof AbstractGirlEntity girl) {
                                switch (packet.actionId()) {
                                    case "stripOrDressup" -> girl.setStripped(!girl.isStripped());
                                    case "breakUp" -> girl.breakUp(context.player());
                                    case "setBase" -> girl.setBasePosHere();
                                    case "titjob" -> girl.getSceneManager().startScene(context.player(),"paizuri_start","paizuri_slow","paizuri_fast","paizuri_cum");
                                    case "talk" -> girl.messageAsEntity(context.player(),"Hello");
                                    case "blowjob" -> girl.getSceneManager().startScene(context.player(),"blowjob_start","blowjob_slow","blowjob_fast","blowjob_cum");
                                    case "testAnim1" -> girl.playAnimation("downed",false);
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

    }

}
