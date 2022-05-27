package com.thoriuslight.professionsmod.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AlchemyContainerProvider implements INamedContainerProvider{
	private BlockPos pos;
	private static final ITextComponent name = new TranslationTextComponent("container.professionsmod.alchemistcrafting");
	
	public AlchemyContainerProvider(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity p_createMenu_3_) {
		return new AlchemistCraftingContainer(windowId, playerInventory, IWorldPosCallable.create(p_createMenu_3_.level, pos));
	}

	@Override
	public ITextComponent getDisplayName() {
		return name;
	}

}
