package com.thoriuslight.professionsmod.inventory.container;

import com.thoriuslight.professionsmod.init.RecipeSerializerInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SmithCraftingResultSlot extends Slot{
	private final CraftingInventory craftMatrix;
	private final PlayerEntity player;
	private int amountCrafted;

	public SmithCraftingResultSlot(PlayerEntity player, CraftingInventory craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.player = player;
		this.craftMatrix = craftingInventory;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
	 */
	@Override
	public ItemStack remove(int amount) {
		if (this.hasItem()) {
			this.amountCrafted += Math.min(amount, this.getItem().getCount());
		}
		return super.remove(amount);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
	 * internal count then calls onCrafting(item).
	 */
	@Override
	protected void onQuickCraft(ItemStack stack, int amount) {
		this.amountCrafted += amount;
		this.checkTakeAchievements(stack);
	}
	@Override
	protected void onSwapCraft(int p_190900_1_) {
		this.amountCrafted += p_190900_1_;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
	 */
	@Override
	protected void checkTakeAchievements(ItemStack stack) {
		if (this.amountCrafted > 0) {
			stack.onCraftedBy(this.player.level, this.player, this.amountCrafted);
			net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, stack, this.craftMatrix);
		}
		if (this.container instanceof IRecipeHolder) {
			((IRecipeHolder)this.container).awardUsedRecipes(this.player);
		}
		this.amountCrafted = 0;
	}
	@Override
	public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
		this.checkTakeAchievements(stack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
		NonNullList<ItemStack> nonnulllist = thePlayer.level.getRecipeManager().getRemainingItemsFor(RecipeSerializerInit.SMITH_TYPE, this.craftMatrix, thePlayer.level);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = this.craftMatrix.getItem(i);
			ItemStack itemstack1 = nonnulllist.get(i);
			if (!itemstack.isEmpty()) {
				this.craftMatrix.removeItem(i, 1);
				itemstack = this.craftMatrix.getItem(i);
			}
			if (!itemstack1.isEmpty()) {
				if (itemstack.isEmpty()) {
					this.craftMatrix.setItem(i, itemstack1);
				} else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1)) {
					itemstack1.grow(itemstack.getCount());
					this.craftMatrix.setItem(i, itemstack1);
				} else if (!this.player.inventory.add(itemstack1)) {
					this.player.drop(itemstack1, false);
				}
			}
		}
		return stack;
	}
}
