package com.thoriuslight.professionsmod.item;

import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.item.ItemStack;

public class MetalHammerItem extends HammerItem implements IMeltable{
	private final ModItemTier tier;
	public MetalHammerItem(Properties properties, ModItemTier tier, int strength) {
		super(properties, tier, strength);
		this.tier = tier;
	}
	@Override
	public int getNuggetAmount(ItemStack stack) {
		return (Tool.HAMMER.getNuggetAmount() * (100 - (50*this.getDamage(stack))/this.getMaxDamage(stack)))/100;
	}
	@Override
	public ModItemTier getType(ItemStack stack) {
		return this.tier;
	}
}
