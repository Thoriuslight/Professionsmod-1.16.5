package com.thoriuslight.professionsmod.item.crafting;

import java.util.List;

import com.google.common.collect.Lists;
import com.thoriuslight.professionsmod.init.RecipeInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.item.ButtedChainArmorItem;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SmithChainArmorRecipe implements ISmithRecipe{
	
	private final ResourceLocation id;
	private final int requiredSkills;
	
	public SmithChainArmorRecipe(ResourceLocation id){
		this.id = id;
		this.requiredSkills = 1 << SkillInit.WEAK_CHAIN_ARMOR.getId();
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		List<ItemStack> list = Lists.newArrayList();
		boolean chainArmor = false;
		for(int i = 0; i < inv.getContainerSize(); ++i) {
	    	ItemStack itemstack = inv.getItem(i);
	    	if (!itemstack.isEmpty()) {
	    		list.add(itemstack);
	    		if(itemstack.getItem() instanceof ButtedChainArmorItem)
	    			chainArmor = true;
	    	}
		}
	    if (list.size() == 2 && chainArmor) {
	    	ItemStack stack = null;
	    	ItemStack chain = null;
	    	for(int j = 0; j < 2; ++j) {
	    		if(list.get(j).getItem() instanceof ButtedChainArmorItem) {
	    			stack = list.get(j);
	    		} 
	    		else {
	    			chain = list.get(j);
	    		}
	    	}
	    	if(stack != null && chain != null) {
	    		if(stack.getDamageValue() != 0 && chain.getItem().equals(((ButtedChainArmorItem)stack.getItem()).getRepairMaterial()))
	    			return true;
	    	}
		}
		return false;
	   }
	
	@Override
	public boolean isUnlocked(int skillList) {
		skillList &= this.requiredSkills;
		return skillList == this.requiredSkills;
	}
	
	@Override
	public ItemStack assemble(CraftingInventory inv) {
		ItemStack recipeOutput = ItemStack.EMPTY;
		for(int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack itemstack = inv.getItem(i);
			if(itemstack.getItem() instanceof ButtedChainArmorItem) {
				recipeOutput = itemstack.copy();
				((ButtedChainArmorItem)recipeOutput.getItem()).addMaterial(recipeOutput);
			}
		}
		return recipeOutput;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RecipeInit.CHAINMAIL_CRAFTING;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return null;
	}
	
	public int getRequirements() {
		return this.requiredSkills;
	}
	@Override
	public boolean isSpecial() {
		return true;
	}
}
