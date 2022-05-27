package com.thoriuslight.professionsmod.item;

import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;

import net.minecraft.item.ItemStack;

public interface IMeltable {
	public int getNuggetAmount(ItemStack stack);
	public ModItemTier getType(ItemStack stack);
}
