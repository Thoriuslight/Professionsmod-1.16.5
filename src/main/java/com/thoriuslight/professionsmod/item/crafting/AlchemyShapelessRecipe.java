package com.thoriuslight.professionsmod.item.crafting;

import com.thoriuslight.professionsmod.init.RecipeSerializerInit;
import com.thoriuslight.professionsmod.item.Sourdough;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class AlchemyShapelessRecipe implements IAlchemyRecipe{
	
	private final NonNullList<Ingredient> recipeItems;
	private final ItemStack recipeOutput;
	private final ResourceLocation id;
	private final int requiredSkills;
	private final boolean isSimple;
	
	public AlchemyShapelessRecipe(ResourceLocation id, NonNullList<Ingredient> recipeItemsIn, ItemStack output, int requirement){
		this.id = id;
		this.recipeItems = recipeItemsIn;
		this.recipeOutput = output;
		this.requiredSkills = requirement;
		this.isSimple = recipeItemsIn.stream().allMatch(Ingredient::isSimple);
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;
		for(int j = 0; j < inv.getContainerSize(); ++j) {
			ItemStack itemstack = inv.getItem(j);
			if (!itemstack.isEmpty()) {
				if(itemstack.getItem() instanceof Sourdough) {
					if(itemstack.getDamageValue() == 0) return false;
				}
				++i;
				if (isSimple)
					recipeitemhelper.accountStack(itemstack, 1);
				else inputs.add(itemstack);
			}
		}
		return i == this.recipeItems.size() && (isSimple ? recipeitemhelper.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.recipeItems) != null);
	}
	
	@Override
	public boolean isUnlocked(int skillList) {
		skillList &= this.requiredSkills;
		return skillList == this.requiredSkills;
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		return this.recipeOutput.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.recipeItems.size();
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
		return RecipeSerializerInit.ALCHEMY_SHAPELESS_SERIALIZER.get();
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.recipeItems;
	}
	
	public int getRequirements() {
		return this.requiredSkills;
	}
}
