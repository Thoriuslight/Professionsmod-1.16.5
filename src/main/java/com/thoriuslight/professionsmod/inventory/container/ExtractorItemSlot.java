package com.thoriuslight.professionsmod.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ExtractorItemSlot extends Slot {

	public ExtractorItemSlot(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
    public int getMaxStackSize() {
        return 1;
    }
}
