package com.thoriuslight.professionsmod.item.crafting;

import java.util.List;

import com.google.common.collect.Lists;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.RecipeInit;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class DoughRecipe extends SpecialRecipe{
	public DoughRecipe(ResourceLocation idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		List<ItemStack> list = Lists.newArrayList();
		int flourCount = 0;
		boolean water = false;
		for(int i = 0; i < inv.getContainerSize(); ++i) {
	    	ItemStack itemstack = inv.getItem(i);
	    	if (!itemstack.isEmpty()) {
	    		list.add(itemstack);
	    		if(itemstack.getItem() == ItemInit.FLOUR.get())
	    		++flourCount;
	    		if(itemstack.getItem() == Items.WATER_BUCKET || itemstack.getItem() == Items.POTION)
	    			water = true;
	    	}
		}
	    if (list.size() == 5 && flourCount == 3 && water) {
	    	ItemStack stack = null;
	    	for(int j = 0; j < 5; ++j) {
	    		if(list.get(j).getItem() == ItemInit.SOURDOUGH.get()) {
	    			stack = list.get(j);
	    			break;
	    		}
	    	}
	    	if(stack != null) {
	    		if(stack.getDamageValue() != 0)
	    		return true;
	    	}
		}
		return false;
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		return new ItemStack(ItemInit.DOUGH.get(), 3);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 5;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RecipeInit.DOUGH_CRAFTING;
	}
}
