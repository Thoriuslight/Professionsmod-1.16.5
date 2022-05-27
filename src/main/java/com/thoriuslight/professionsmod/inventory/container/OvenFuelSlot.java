package com.thoriuslight.professionsmod.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class OvenFuelSlot extends Slot {
	private final OvenContainer container;

	public OvenFuelSlot(OvenContainer container, IInventory inventory, int index, int xPos, int yPos) {
		super(inventory, index, xPos, yPos);
		this.container = container;
	}
	@Override
	public boolean mayPlace(ItemStack stack) {
		return this.container.isFuel(stack) || isBucket(stack);
	}
	@Override
	public int getMaxStackSize(ItemStack stack) {
		return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
	}

	public static boolean isBucket(ItemStack stack) {
		return stack.getItem() == Items.BUCKET;
	}
}
