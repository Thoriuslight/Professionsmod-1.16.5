package com.thoriuslight.professionsmod.item.crafting;

import java.util.Collections;
import java.util.Map;

import com.thoriuslight.professionsmod.init.RecipeSerializerInit;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GrindingRecipe implements IGrindingRecipe{
	
	protected final Ingredient ingredient;
	private final ItemStack recipeOutput;
	private final ResourceLocation id;
	private final int requiredSkills;
	public static Map<ResourceLocation, IGrindingRecipe> recipeList = Collections.emptyMap();
	
	public GrindingRecipe(ResourceLocation id, Ingredient ingredientIn, ItemStack output, int requirement){
		this.id = id;
		this.ingredient = ingredientIn;
		this.recipeOutput = output;
		this.requiredSkills = requirement;
	}
	@Override
	public boolean matches(IInventory inv, World worldIn) {
		ItemStack stack = inv.getItem(0);
		if (stack == null) {
			return false;
		} else if (ingredient.isEmpty()) {
	          return stack.isEmpty();
		} else {
			for(ItemStack itemstack : ingredient.getItems()) {
				if (itemstack.getItem() == stack.getItem() && itemstack.getCount() <= stack.getCount()) {
					return true;
				}
			}
			return false;
		}
	}
	
	@Override
	public boolean isUnlocked(int skillList) {
		skillList &= this.requiredSkills;
		return skillList == this.requiredSkills;
	}
	
	@Override
	public ItemStack assemble(IInventory inv) {
		return this.recipeOutput.copy();
	}

	@Override
	public ItemStack getResultItem() {
		return this.recipeOutput;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RecipeSerializerInit.ALCHEMY_SERIALIZER.get();
	}
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}
	
	public int getRequirements() {
		return this.requiredSkills;
	}
}
