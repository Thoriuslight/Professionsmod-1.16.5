package com.thoriuslight.professionsmod.inventory.container;

import com.thoriuslight.professionsmod.init.ModContainerTypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OvenContainer  extends RecipeBookContainer<IInventory> {
	private final IInventory ovenInventory;
	private final IIntArray furnaceData;
	protected final World world;
	public OvenContainer(int windowId, PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, new Inventory(3), new IntArray(4));
	}
	public OvenContainer(final int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray furnaceDataIn) {
		super(ModContainerTypes.OVEN.get(), windowId);
		this.ovenInventory = inventory;
		this.furnaceData = furnaceDataIn;
		checkContainerSize(ovenInventory, 3);
		checkContainerDataCount(furnaceDataIn, 4);
	    this.world = playerInventory.player.level;
	    this.addSlot(new Slot(inventory, 0, 56, 17));
	    this.addSlot(new OvenFuelSlot(this, inventory, 1, 56, 53));
	    this.addSlot(new OvenResultSlot(playerInventory.player, inventory, 2, 116, 35));
	    for(int i = 0; i < 3; ++i) {
	    	for(int j = 0; j < 9; ++j) {
	    		this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	    	}
	    }
	    for(int k = 0; k < 9; ++k) {
	    	this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
	    }
	    this.addDataSlots(furnaceDataIn);
	}

	@Override
	public void fillCraftSlotsStackedContents(RecipeItemHelper itemHelperIn) {
		if (this.ovenInventory instanceof IRecipeHelperPopulator) {
			((IRecipeHelperPopulator)this.ovenInventory).fillStackedContents(itemHelperIn);
		}
	}

	@Override
	public void clearCraftingContent() {
		this.ovenInventory.clearContent();
	}

	@Override
	public boolean recipeMatches(IRecipe<? super IInventory> recipeIn) {
		return recipeIn.matches(this.ovenInventory, this.world);
	}

	@Override
	public int getResultSlotIndex() {
		return 2;
	}

	@Override
	public int getGridWidth() {
		return 1;
	}

	@Override
	public int getGridHeight() {
		return 1;
	}

	@Override
	public int getSize() {
		return 3;
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return this.ovenInventory.stillValid(playerIn);
	}
	protected boolean isFuel(ItemStack stack) {
		return AbstractFurnaceTileEntity.isFuel(stack);
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 2) {
				if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (this.hasRecipe(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (this.isFuel(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 3 && index < 30) {
					if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 30 && index < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
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
	protected boolean hasRecipe(ItemStack stack) {
		return this.world.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, new Inventory(stack), this.world).isPresent();
	}
	@OnlyIn(Dist.CLIENT)
	public int getCookProgressionScaled() {
		int i = this.furnaceData.get(2);
		int j = this.furnaceData.get(3);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}
	@OnlyIn(Dist.CLIENT)
	public int getBurnLeftScaled() {
		int i = this.furnaceData.get(1);
		if (i == 0) {
			i = 200;
		}
		return this.furnaceData.get(0) * 13 / i;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isBurning() {
		return this.furnaceData.get(0) > 0;
	}
	@Override
	public RecipeBookCategory getRecipeBookType() {
		return null;
	}
}
