package com.thoriuslight.professionsmod.util;

import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

public class MetalItem{
	private final int nuggets;
	private final int inpurity;
	private final int maxStackSize;
	private final ModItemTier material;
	public MetalItem(Tool tool, ModItemTier material){
		this.nuggets = tool.getNuggetAmount();
		this.inpurity = tool.getInpurity();
		this.maxStackSize = tool.getStackSize();
		this.material = material;
	}
	public MetalItem(int nuggets, int inpurity, ModItemTier material){
		this.nuggets = nuggets;
		this.inpurity = inpurity;
		this.maxStackSize = 1;
		this.material = material;
	}
	public ModItemTier getMaterial() {
		return this.material;
	}
	public int getNuggets() {
		return this.nuggets;
	}
	public int getInpurity() {
		return this.inpurity;
	}
	public int getStackSize() {
		return this.maxStackSize;
	}
	public int getMeltingPoint() {
		return this.material.getMeltingPoint();
	}
}