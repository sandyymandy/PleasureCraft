package com.sandymandy.pleasurecraft.util.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.entity.player.PlayerEntity;

public interface GirlInventory extends Inventory {
    // Slot indices
    int MAIN_HAND_SLOT = 0;
    int ARMOR_START = 1;
    int ARMOR_END = 4;
    int BACKPACK_START = 5;
    int BACKPACK_SIZE = 12;  // slots 5..16 inclusive
    int BACKPACK_END = 16;
    int TOTAL_SLOTS = BACKPACK_START + BACKPACK_SIZE;  // 17 total slots

    // Must always return the same instance backing the inventory
    DefaultedList<ItemStack> getItems();

    // Factory to create a GirlInventory backed by the given list
    static GirlInventory of(DefaultedList<ItemStack> items) {
        return () -> items;
    }

    // Factory to create a new GirlInventory with empty slots
    static GirlInventory ofSize() {
        return of(DefaultedList.ofSize(TOTAL_SLOTS, ItemStack.EMPTY));
    }

    // Inventory interface implementations

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : getItems()) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) markDirty();
        return result;
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > stack.getMaxCount()) {
            stack.setCount(stack.getMaxCount());
        }
        markDirty();
    }

    @Override
    default void markDirty() {
        // Optional: could sync inventory here, if needed
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    default void clear() {
        getItems().clear();
    }

    // Custom validation for item insertion based on slot
//    default boolean isItemValid(int slot, ItemStack stack) {
//        if (stack.isEmpty()) return true; // Allow clearing the slot
//
//        if (slot == MAIN_HAND_SLOT) {
//            // Only allow tools and swords in main hand slot
//            return stack.getItem() instanceof SwordItem || stack.getItem() instanceof ToolItem;
//        }
//
//        if (slot >= ARMOR_HEAD_SLOT && slot <= ARMOR_FEET_SLOT) {
//            // Armor slots must be armor matching the slot
//            if (!(stack.getItem() instanceof ArmorItem armor)) return false;
//
//            EquipmentSlot expectedSlot = switch (slot) {
//                case ARMOR_HEAD_SLOT -> EquipmentSlot.HEAD;
//                case ARMOR_CHEST_SLOT -> EquipmentSlot.CHEST;
//                case ARMOR_LEGS_SLOT -> EquipmentSlot.LEGS;
//                case ARMOR_FEET_SLOT -> EquipmentSlot.FEET;
//                default -> null;
//            };
//            return expectedSlot != null && armor.getSlotType() == expectedSlot;
//        }
//
//        // Backpack slots accept any item
//        return slot >= BACKPACK_START && slot < BACKPACK_START + BACKPACK_SIZE;
//    }
//
//    // Utility method to get slot index from EquipmentSlot
//    static int getSlotForArmor(EquipmentSlot slot) {
//        return switch (slot) {
//            case HEAD -> ARMOR_HEAD_SLOT;
//            case CHEST -> ARMOR_CHEST_SLOT;
//            case LEGS -> ARMOR_LEGS_SLOT;
//            case FEET -> ARMOR_FEET_SLOT;
//            default -> -1;
//        };
//    }
}
