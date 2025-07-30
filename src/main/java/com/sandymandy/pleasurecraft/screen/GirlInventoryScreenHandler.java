package com.sandymandy.pleasurecraft.screen;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.util.entity.AbstractGirlEntity;
import com.sandymandy.pleasurecraft.util.inventory.slot.PublicArmorSlot;
import com.sandymandy.pleasurecraft.util.inventory.GirlInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;

import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Map;

public class GirlInventoryScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final AbstractGirlEntity girl;
    public static final Identifier EMPTY_HELMET_SLOT_TEXTURE = Identifier.ofVanilla( "container/slot/helmet");
    public static final Identifier EMPTY_CHESTPLATE_SLOT_TEXTURE = Identifier.ofVanilla( "container/slot/chestplate");
    public static final Identifier EMPTY_LEGGINGS_SLOT_TEXTURE = Identifier.ofVanilla( "container/slot/leggings");
    public static final Identifier EMPTY_BOOTS_SLOT_TEXTURE = Identifier.ofVanilla( "container/slot/boots");
    private static final Map<EquipmentSlot, Identifier> EMPTY_ARMOR_SLOT_TEXTURES = Map.of(
            EquipmentSlot.FEET,
            EMPTY_BOOTS_SLOT_TEXTURE,
            EquipmentSlot.LEGS,
            EMPTY_LEGGINGS_SLOT_TEXTURE,
            EquipmentSlot.CHEST,
            EMPTY_CHESTPLATE_SLOT_TEXTURE,
            EquipmentSlot.HEAD,
            EMPTY_HELMET_SLOT_TEXTURE
    );
    private static final EquipmentSlot[] EQUIPMENT_SLOT_ORDER = new EquipmentSlot[]{
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    // The codec-compatible constructor
    public GirlInventoryScreenHandler(int syncId, PlayerInventory playerInventory, PleasureCraft.GirlScreenData data) {
        this(syncId, playerInventory, data.entityId());
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    // and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public GirlInventoryScreenHandler(int syncId, PlayerInventory playerInventory, int girlId) {
        super(PleasureCraft.GIRL_SCREEN_HANDLER, syncId);
        PlayerEntity player = playerInventory.player;
        World world = player.getWorld();

        Entity entity = world.getEntityById(girlId);
        if (!(entity instanceof AbstractGirlEntity girlEntity)) {
            throw new IllegalStateException("LucyEntity not found or mismatched entity ID");
        }
        this.girl = girlEntity;

        Inventory inventory;
        if (girl != null) {
            inventory = girl.getInventory(); // ← Use your custom implementation, not a copy
        } else {
            inventory = new SimpleInventory(GirlInventory.TOTAL_SLOTS);
        }
        this.inventory = inventory;

        checkSize(inventory, GirlInventory.TOTAL_SLOTS);


        // ───── Backpack slots (4x3) = indices 5..16 ─────Add commentMore actions
        int slotIndex = GirlInventory.BACKPACK_START; // 5
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                this.addSlot(new Slot(inventory, slotIndex++, 98 + col * 18, 6 + row * 18));
            }
        }

        // ───── Main Hand Slot = index 0 ─────
        this.addSlot(new Slot(inventory, GirlInventory.MAIN_HAND_SLOT, 125, 63) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof SwordItem;
            }
        });


        for (int i = 0; i < 4; i++) {
            EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
            Identifier identifier = (Identifier)EMPTY_ARMOR_SLOT_TEXTURES.get(equipmentSlot);
            this.addSlot(new PublicArmorSlot(inventory, girl, equipmentSlot, GirlInventory.ARMOR_END - i, 8, 6 + i * 18, identifier));
        }



        int m;
        int l;
        // The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            int girlSlotCount = GirlInventory.TOTAL_SLOTS;
            int playerInvStart = girlSlotCount;
            int playerInvEnd = playerInvStart + 27;
            int hotbarStart = playerInvEnd;
            int hotbarEnd = hotbarStart + 9;

            EquipmentSlot preferredSlot = player.getPreferredEquipmentSlot(originalStack);

            if (index < girlSlotCount) {
                // Moving from girl's inventory → player inventory
                if (!this.insertItem(originalStack, playerInvStart, hotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player → girl's main hand slot
                if ((originalStack.getItem() instanceof SwordItem)
                        && !this.slots.get(GirlInventory.MAIN_HAND_SLOT).hasStack()) {
                    if (!this.insertItem(originalStack, GirlInventory.MAIN_HAND_SLOT, GirlInventory.MAIN_HAND_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }

                    // From player → girl's armor slots
                } else if (preferredSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    // Find correct index in EQUIPMENT_SLOT_ORDER
                    for (int armorIdx = 0; armorIdx < EQUIPMENT_SLOT_ORDER.length; armorIdx++) {
                        if (EQUIPMENT_SLOT_ORDER[armorIdx] == preferredSlot) {
                            int armorSlotIndex = GirlInventory.ARMOR_START + armorIdx;
                            if (!this.slots.get(armorSlotIndex).hasStack()) {
                                if (!this.insertItem(originalStack, armorSlotIndex, armorSlotIndex + 1, false)) {
                                    return ItemStack.EMPTY;
                                }
                                break;
                            }
                        }
                    }
                } else if (!this.insertItem(originalStack, GirlInventory.BACKPACK_START, GirlInventory.BACKPACK_END + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY, newStack);
            } else {
                slot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, originalStack);
        }

        return newStack;
    }






    public AbstractGirlEntity getGirl(){
        return this.girl;
    }



}
