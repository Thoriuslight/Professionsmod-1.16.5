package com.thoriuslight.professionsmod.item;

import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;

import net.minecraft.item.Item;

public class MoltenMetalItem extends Item{
	private final ModItemTier metal;
	public MoltenMetalItem(Properties properties, ModItemTier metal) {
		super(properties);
		this.metal = metal;
	}
	
	public ModItemTier getMetal() {
		return this.metal;
	}
}
