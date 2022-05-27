package com.thoriuslight.professionsmod.state.properties;

import net.minecraft.util.IStringSerializable;

public enum Tool implements IStringSerializable{
	PICKAXE("pickaxe", 27),
	SHOVEL("shovel", 9),
	AXE("axe", 27),
	KNIFE("knife", 9),
	HOE("hoe", 18),
	HAMMER("hammer", 18),
	MORTAR("mortar", 9),
	RINGS("rings", 4, 8, 0),
	ORE("ore", 5, 1, 30),
	CHUNK("chunk", 1, 16, 10),
	ORE_DUST("ore_dust", 1, 24, 5),
	ROASTED_ORE("roasted_ore", 9, 3, 4),
	NUGGET("nugget", 1, 32, 0),
	INGOT("ingot", 9, 6, 0),
	NOTHING("nothing", 0);
	
	private final String name;
	private final int reqNugget;
	private final int stackSize;
	private final int inpurity; //x10
	private Tool(String name, int requiredNuggets){
		this(name, requiredNuggets, 1, 0);
	}
	private Tool(String name, int requiredNuggets, int stackSzize, int impurity){
		this.name = name;
		this.reqNugget = requiredNuggets;
		this.stackSize = stackSzize;
		this.inpurity = impurity;
	}
	@Override
	public String getSerializedName() {
		return this.name;
	}
	public int getNuggetAmount() {
		return this.reqNugget;
	}
	public int getStackSize() {
		return this.stackSize;
	}
	public int getInpurity() {
		return this.inpurity;
	}
}
