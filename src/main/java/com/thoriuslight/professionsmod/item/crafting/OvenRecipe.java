package com.thoriuslight.professionsmod.item.crafting;

import java.util.Collections;
import java.util.Map;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.RecipeSerializerInit;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;

public class OvenRecipe extends AbstractCookingRecipe{
	private final boolean isExothermic;
	private final int inCount;
	private final ItemStack bonusItem;
	private final float chance;
	private final FluidStack fluid;
	public static ResourceLocation RECIPE_TYPE_ID = new ResourceLocation(ProfessionsMod.MODID, "oven");
	public static Map<ResourceLocation, AbstractCookingRecipe> recipeList = Collections.emptyMap();
	
	public OvenRecipe(ResourceLocation idIn, Ingredient ingredientIn, ItemStack resultIn, ItemStack bonusItem, boolean type, int inCount, float chance, FluidStack fluid) {
		super(Registry.RECIPE_TYPE.getOptional(RECIPE_TYPE_ID).get(), idIn, "", ingredientIn, resultIn, 0, 200);
		this.isExothermic = type;
		this.inCount = inCount;
		this.bonusItem = bonusItem;
		this.chance = chance;
		this.fluid = fluid;
	}
	public ItemStack getIcon() {
		return new ItemStack(Blocks.FURNACE);
	}
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RecipeSerializerInit.OVEN_RECIPE_SERIALIZER;
	}
	
	public boolean isExothermic() {
		return this.isExothermic;
	}
	public int getInputCount() {
		return this.inCount;
	}
	public ItemStack getBonus() {
		return this.bonusItem;
	}
	public float getChance() {
		return this.chance;
	}
	public FluidStack getFluid() {
		return this.fluid;
	}
}
