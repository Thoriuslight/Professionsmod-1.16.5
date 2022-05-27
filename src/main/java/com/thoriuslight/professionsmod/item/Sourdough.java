package com.thoriuslight.professionsmod.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Sourdough extends Item{

	public Sourdough(Properties properties) {
		super(properties);
	}
	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		ItemStack stack = new ItemStack(itemStack.getItem());
		if(itemStack.getDamageValue()==24)
		stack.setDamageValue(0);
		else
		stack.setDamageValue(itemStack.getDamageValue()+1);
		return stack;
	}
}
