package com.sandymandy.pleasurecraft.util.inventory;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.entity.player.PlayerEntity;

public interface GirlInventory extends Inventory {
    // Slot indices
    int MAIN_HAND_SLOT = 0;

    // Armor slots mapped to vanilla slots
    int ARMOR_FEET_SLOT = 1;
    int ARMOR_LEGS_SLOT = 2;
    int ARMOR_CHEST_SLOT = 3;
    int ARMOR_HEAD_SLOT = 4;

    int ARMOR_START = ARMOR_FEET_SLOT;
    int ARMOR_END = ARMOR_HEAD_SLOT;

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


    // === Armor access helpers ===

    default ItemStack getArmorStack(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> getStack(4);
            case CHEST -> getStack(3);
            case LEGS -> getStack(2);
            case FEET -> getStack(1);
            default -> ItemStack.EMPTY; // mainhand/offhand not handled here
        };
    }

    default void setArmorStack(EquipmentSlot slot, ItemStack stack) {
        switch (slot) {
            case HEAD -> setStack(ARMOR_HEAD_SLOT, stack);
            case CHEST -> setStack(ARMOR_CHEST_SLOT, stack);
            case LEGS -> setStack(ARMOR_LEGS_SLOT, stack);
            case FEET -> setStack(ARMOR_FEET_SLOT, stack);
            default -> {}
        }
    }


    // === Inventory defaults ===

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
}
