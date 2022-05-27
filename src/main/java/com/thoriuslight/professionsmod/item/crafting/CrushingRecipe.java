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

public class CrushingRecipe implements ICrushingRecipe{
	protected final Ingredient ingredient;
	private final ItemStack recipeOutput;
	private final ResourceLocation id;
	private final int requiredSkills;
	private final int hardness;
	public static Map<ResourceLocation, ICrushingRecipe> recipeList = Collections.emptyMap();
	
	public CrushingRecipe(ResourceLocation id, Ingredient ingredientIn, ItemStack output, int requirement, int hardness){
		this.id = id;
		this.ingredient = ingredientIn;
		this.recipeOutput = output;
		this.requiredSkills = requirement;
		this.hardness = hardness;
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
		return RecipeSerializerInit.CRUSHING_SERIALIZER.get();
	}

	@Override
	public boolean isUnlocked(int skillList) {
		skillList &= this.requiredSkills;
		return skillList == this.requiredSkills;
	}

	@Override
	public int getHardness() {
		return hardness;
	}
	
	public int getRequirements() {
		return this.requiredSkills;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}
}
