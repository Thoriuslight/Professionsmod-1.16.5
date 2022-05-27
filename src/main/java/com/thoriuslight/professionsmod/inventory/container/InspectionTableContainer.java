package com.thoriuslight.professionsmod.inventory.container;

import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.ModContainerTypes;
import com.thoriuslight.professionsmod.item.ToolCoreItem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;

public class InspectionTableContainer extends Container{
	private final IWorldPosCallable canInteractWithCallable;
	private final IInventory tool = new Inventory(1) {
	    @Override
	    public boolean canPlaceItem(int index, ItemStack stack) {
	    	return (stack.getItem() instanceof ToolCoreItem);
	    }
	    @Override
		public int getMaxStackSize() {
			return 1;
		}
	};

	public InspectionTableContainer(int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, IWorldPosCallable.NULL);
	}
	public InspectionTableContainer(int windowId, final PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
		super(ModContainerTypes.INSPECTION_TABLE.get(), windowId);
		this.canInteractWithCallable = worldPosCallable;
		this.addSlot(new Slot(tool, 0, 13, 16));
		
	    for(int k = 0; k < 3; ++k) {
	    	for(int i1 = 0; i1 < 9; ++i1) {
	    		this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	    	}
	    }

	    for(int l = 0; l < 9; ++l) {
	    	this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
	    }
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return stillValid(this.canInteractWithCallable, playerIn, BlockInit.INSPECTIONTABLE_BLOCK.get());
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 0) {
				if (!this.moveItemStackTo(itemstack1, 1, 37, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (this.moveItemStackTo(itemstack1, 0, 1, false)) { //Forge Fix Shift Clicking in beacons with stacks larger then 1.
				return ItemStack.EMPTY;
			} else if (index >= 1 && index < 28) {
				if (!this.moveItemStackTo(itemstack1, 28, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 28 && index < 37) {
				if (!this.moveItemStackTo(itemstack1, 1, 28, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 1, 37, false)) {
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
			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}
	
	@Override
	public void removed(PlayerEntity playerIn) {
		super.removed(playerIn);
		this.canInteractWithCallable.execute((p_217068_2_, p_217068_3_) -> {
			this.clearContainer(playerIn, p_217068_2_, this.tool);
		});
	}
}
