package com.thoriuslight.professionsmod.item.crafting;

import javax.annotation.Nonnull;

import com.thoriuslight.professionsmod.ProfessionsMod;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public interface ICrushingRecipe extends IRecipe<IInventory>{
	ResourceLocation RECIPE_TYPE_ID = new ResourceLocation(ProfessionsMod.MODID, "crushing");
	
	@Nonnull
	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOptional(RECIPE_TYPE_ID).get();
	}
	
	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	boolean isUnlocked(int skillList);
	public int getRequirements();
	int getHardness();
}
