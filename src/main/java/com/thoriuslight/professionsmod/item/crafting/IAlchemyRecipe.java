package com.thoriuslight.professionsmod.item.crafting;

import javax.annotation.Nonnull;

import com.thoriuslight.professionsmod.ProfessionsMod;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public interface IAlchemyRecipe extends IRecipe<CraftingInventory>{
	ResourceLocation RECIPE_TYPE_ID = new ResourceLocation(ProfessionsMod.MODID, "alchemy");
	
	@Nonnull
	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOptional(RECIPE_TYPE_ID).get();
	}
	
	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	boolean isUnlocked(int skillList);
	public int getRequirements();
}
