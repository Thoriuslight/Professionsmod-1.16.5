package com.thoriuslight.professionsmod.inventory.container;

import com.thoriuslight.professionsmod.init.ModContainerTypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class ExtractorContainer extends Container{
	private final IInventory extractor;
	private final IIntArray extractorData;
	public ExtractorContainer(int windowId, PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, new Inventory(11), new IntArray(1));
	}
	public ExtractorContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray dataIn) {
		super(ModContainerTypes.EXTRACTOR.get(), windowId);
	    checkContainerSize(inventory, 11);
		this.extractor = inventory;
		this.extractorData = dataIn;
		checkContainerDataCount(dataIn, 1);
		inventory.startOpen(playerInventory.player);
	    for(int i = 0; i < 3; ++i) {
	    	for(int j = 0; j < 3; ++j) {
	    		this.addSlot(new ExtractorItemSlot(inventory, j + i * 3, 98 + j * 18, 17 + i * 18));
	    	}
	    }
		this.addSlot(new ExtractorBucketSlot(inventory, 9, 62, 17));
		this.addSlot(new ExtractorOutputSlot(inventory, 10, 62, 53));
	    for(int k = 0; k < 3; ++k) {
	    	for(int i1 = 0; i1 < 9; ++i1) {
	    		this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	    	}
	    }
	    for(int l = 0; l < 9; ++l) {
	    	this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
	    }
	    this.addDataSlots(dataIn);
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return this.extractor.stillValid(playerIn);
	}
	@Override
	public ItemStack quickMoveStack(PlayerEntity p_82846_1_, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 11) {
				if (!this.moveItemStackTo(itemstack1, 11, 47, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 9, 10, false)) {
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(p_82846_1_, itemstack1);
		}
		return itemstack;
	}
	@Override
	public void removed(PlayerEntity p_75134_1_) {
		super.removed(p_75134_1_);
		this.extractor.stopOpen(p_75134_1_);
	}
	public int getFluidAmount() {
		return this.extractorData.get(0);
	}
	
	public class ExtractorBucketSlot extends Slot {

		public ExtractorBucketSlot(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
			super(inventoryIn, slotIndex, xPosition, yPosition);
		}
		@Override
		public boolean mayPlace(ItemStack stack) {
			Item item = stack.getItem();
			return (item.equals(Items.BUCKET) || item.equals(Items.GLASS_BOTTLE));
		}
	}
	public class ExtractorOutputSlot extends Slot {

		public ExtractorOutputSlot(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
			super(inventoryIn, slotIndex, xPosition, yPosition);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return false;
		}
	}
}
