package com.thoriuslight.professionsmod.item;

import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class BrokenTool extends Item implements IMeltable {
	private int colouredLayer = 1;
	public BrokenTool(Properties properties, Tool tool, int colouredLayer) {
		this(properties, tool);
		this.colouredLayer = colouredLayer;
	}
	public BrokenTool(Properties properties, Tool tool) {
		super(properties.stacksTo(1));
	}
	public static void init(ItemStack stack, ModItemTier metal, int nuggets) {
		BrokenTool.setMetal(stack, metal);
		BrokenTool.setNuggets(stack, nuggets);
	}
	protected static int getNuggets(ItemStack stack) {
		CompoundNBT compoundnbt = stack.getTagElement("properties");
		return compoundnbt != null && compoundnbt.contains("nuggets", 99) ? compoundnbt.getInt("nuggets") : 0;
	}
	protected static void setNuggets(ItemStack stack, int nuggets) {
		stack.getOrCreateTagElement("properties").putInt("nuggets", nuggets);
	}
	protected static ModItemTier getMetal(ItemStack stack) {
		CompoundNBT compoundnbt = stack.getTagElement("properties");
		return compoundnbt != null && compoundnbt.contains("nuggets", 99) ? ModItemTier.valueOf(compoundnbt.getString("metal")) : ModItemTier.COPPER;
	}
	protected static void setMetal(ItemStack stack, ModItemTier metal) {
		stack.getOrCreateTagElement("properties").putString("metal", metal.toString());
	}
	@Override
	public ITextComponent getName(ItemStack stack) {
		return new StringTextComponent(I18n.get("material.professionsmod.broken") + " " + I18n.get("material.professionsmod." + BrokenTool.getMetal(stack).toString().toLowerCase()) + " " + super.getName(stack).getString());
	}
	@Override
	public int getNuggetAmount(ItemStack stack) {
		return BrokenTool.getNuggets(stack);
	}
	@Override
	public ModItemTier getType(ItemStack stack) {
		return BrokenTool.getMetal(stack);
	}
	public int getLayer() {
		return this.colouredLayer;
	}
	public static int getItemColour(ItemStack stack, int tintIndex) {
		if(tintIndex == ((BrokenTool)stack.getItem()).getLayer()) {
			return BrokenTool.getMetal(stack).getColour();
		}
		return 0xFFFFFF;
	}
}
