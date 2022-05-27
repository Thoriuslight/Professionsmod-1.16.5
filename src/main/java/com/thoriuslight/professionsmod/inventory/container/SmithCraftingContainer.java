package com.thoriuslight.professionsmod.inventory.container;

import java.util.Optional;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.ModContainerTypes;
import com.thoriuslight.professionsmod.init.RecipeSerializerInit;
import com.thoriuslight.professionsmod.item.crafting.ISmithRecipe;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SmithCraftingContainer extends RecipeBookContainer<CraftingInventory>{
	private final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
	private final CraftResultInventory craftResult = new CraftResultInventory();
	private final IWorldPosCallable canInteractWithCallable;
	private final PlayerEntity player;
	
	public SmithCraftingContainer(int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, IWorldPosCallable.NULL);
	}
	
	public SmithCraftingContainer(int windowId, final PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
		super(ModContainerTypes.SMITH_CRAFTING.get(), windowId);
		this.canInteractWithCallable = worldPosCallable;
		this.player = playerInventory.player;
	    this.addSlot(new SmithCraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
	    
	    for(int i = 0; i < 3; ++i) {
	    	for(int j = 0; j < 3; ++j) {
	    		this.addSlot(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
	    	}
	    }

	    for(int k = 0; k < 3; ++k) {
	    	for(int i1 = 0; i1 < 9; ++i1) {
	    		this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	    	}
	    }

	    for(int l = 0; l < 9; ++l) {
	    	this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
	    }
	}
	
	protected static void slotChangedCraftingGrid(int p_217066_0_, World world, PlayerEntity p_217066_2_, CraftingInventory craftInv, CraftResultInventory p_217066_4_) {
		if (!world.isClientSide) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)p_217066_2_;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<ISmithRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeSerializerInit.SMITH_TYPE, craftInv, world);
			if (optional.isPresent()) {
				IProfession iProfession = serverplayerentity.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
				ISmithRecipe ismithrecipe = optional.get();
				if (p_217066_4_.setRecipeUsed(world, serverplayerentity, ismithrecipe) && ismithrecipe.isUnlocked(iProfession.getSkillTree())) {
					itemstack = ismithrecipe.assemble(craftInv);
				}
			}

			p_217066_4_.setItem(0, itemstack);
			serverplayerentity.connection.send(new SSetSlotPacket(p_217066_0_, 0, itemstack));
		}
	}
	
	@Override
	public void slotsChanged(IInventory inventoryIn) {
		this.canInteractWithCallable.execute((world, p_217069_2_) -> {
			slotChangedCraftingGrid(this.containerId, world, this.player, this.craftMatrix, this.craftResult);
		});
	}
	
	@Override
	public void fillCraftSlotsStackedContents(RecipeItemHelper itemHelperIn) {
		this.craftMatrix.fillStackedContents(itemHelperIn);
	}

	@Override
	public void clearCraftingContent() {
		this.craftMatrix.clearContent();
		this.craftResult.clearContent();
	}

	@Override
	public boolean recipeMatches(IRecipe<? super CraftingInventory> recipeIn) {
		return recipeIn.matches(this.craftMatrix, this.player.level);
	}
	
	@Override
	public void removed(PlayerEntity playerIn) {
		super.removed(playerIn);
		this.canInteractWithCallable.execute((p_217068_2_, p_217068_3_) -> {
			this.clearContainer(playerIn, p_217068_2_, this.craftMatrix);
		});
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return stillValid(this.canInteractWithCallable, playerIn, BlockInit.SMITHCRAFTINGTABLE_BLOCK.get());
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 0) {
				this.canInteractWithCallable.execute((p_217067_2_, p_217067_3_) -> {
					itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, playerIn);
				});
				if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index >= 10 && index < 46) {
				if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
					if (index < 37) {
						if (!this.moveItemStackTo(itemstack1, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.moveItemStackTo(itemstack1, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
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

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
			if (index == 0) {
				playerIn.drop(itemstack2, false);
			}
		}

		return itemstack;
	}
	
	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
		return slotIn.container != this.craftResult && super.canTakeItemForPickAll(stack, slotIn);
	}
	
	@Override
	public int getResultSlotIndex() {
		return 0;
	}
	
	@Override
	public int getGridWidth() {
		return this.craftMatrix.getWidth();
	}
	
	@Override
	public int getGridHeight() {
		return this.craftMatrix.getHeight();
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public int getSize() {
		return 10;
	}

	@Override
	public RecipeBookCategory getRecipeBookType() {
		return null;
	}
}
