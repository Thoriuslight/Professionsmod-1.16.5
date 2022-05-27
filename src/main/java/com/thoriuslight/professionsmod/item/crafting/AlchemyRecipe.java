package com.thoriuslight.professionsmod.item.crafting;

import java.util.Collections;
import java.util.Map;

import com.thoriuslight.professionsmod.init.RecipeSerializerInit;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class AlchemyRecipe implements IAlchemyRecipe{
	
	private final int recipeWidth;
	private final int recipeHeight;
	private final NonNullList<Ingredient> recipeItems;
	private final ItemStack recipeOutput;
	private final ResourceLocation id;
	private final int requiredSkills;
	public static Map<ResourceLocation, IAlchemyRecipe> recipeList = Collections.emptyMap();
	
	public AlchemyRecipe(ResourceLocation id, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack output, int requirement){
		this.recipeWidth = recipeWidthIn;
		this.recipeHeight = recipeHeightIn;
		this.id = id;
		this.recipeItems = recipeItemsIn;
		this.recipeOutput = output;
		this.requiredSkills = requirement;
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		//Horizontal shift
		for(int x = 0; x <= inv.getWidth() - this.recipeWidth; ++x) {
			//Vertical shift
			for(int y = 0; y <= inv.getHeight() - this.recipeHeight; ++y) {
				if (this.checkMatch(inv, x, y, true)) {
					return true;
				}
				if (this.checkMatch(inv, x, y, false)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean isUnlocked(int skillList) {
		skillList &= this.requiredSkills;
		return skillList == this.requiredSkills;
	}

	private boolean checkMatch(CraftingInventory craftingInventory, int p_77573_2_, int p_77573_3_, boolean p_77573_4_) {
		for(int i = 0; i < craftingInventory.getWidth(); ++i) {
			for(int j = 0; j < craftingInventory.getHeight(); ++j) {
				int k = i - p_77573_2_;
				int l = j - p_77573_3_;
				Ingredient ingredient = Ingredient.EMPTY;
				if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
					if (p_77573_4_) {
						ingredient = this.recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
					} else {
						ingredient = this.recipeItems.get(k + l * this.recipeWidth);
					}
				}
				if (!ingredient.test(craftingInventory.getItem(i + j * craftingInventory.getWidth()))) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public ItemStack assemble(CraftingInventory inv) {
		return this.recipeOutput.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= this.recipeWidth && height >= this.recipeHeight;
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
	
	public int getRecipeHeight() {
		return this.recipeHeight;
	}
	
	public int getRecipeWidth() {
		return this.recipeWidth;
	}
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.recipeItems;
	}
	
	public int getRequirements() {
		return this.requiredSkills;
	}
}
