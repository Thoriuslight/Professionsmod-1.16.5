package com.thoriuslight.professionsmod.util.combat.jei.alchemy;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.item.crafting.OvenRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class OvenRecipeCategory implements IRecipeCategory<AbstractCookingRecipe>{
	public static final ResourceLocation Uid = new ResourceLocation(ProfessionsMod.MODID, "oven_crafting");
	private final String localizedName;
	private final IDrawableStatic background;
	private final IDrawable icon;
	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 1;
	public OvenRecipeCategory(IGuiHelper helper) {
		this.localizedName = I18n.get("container.professionsmod.ovencrafting");
		this.background = helper.createDrawable(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 0, 220, 82, 34);
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.INSULATION_BRICK_BLOCK.get().asItem()));
	}
	@Override
	public ResourceLocation getUid() {
		return Uid;
	}

	@Override
	public Class<? extends AbstractCookingRecipe> getRecipeClass() {
		return AbstractCookingRecipe.class;
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(AbstractCookingRecipe recipe, IIngredients ingredients) {
		OvenRecipe oRecipe = (OvenRecipe)recipe;
	    NonNullList<Ingredient> nonnulllist = NonNullList.create();
		for(Ingredient ingredient : oRecipe.getIngredients()) {
			ItemStack[] stackArray = ingredient.getItems().clone();
			for(ItemStack stack : stackArray) {
				stack.setCount(oRecipe.getInputCount());
			}
			nonnulllist.add(Ingredient.of(stackArray));
		}
		ingredients.setInputIngredients(nonnulllist);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, AbstractCookingRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(craftInputSlot1, true, 0, 8);
		guiItemStacks.init(craftOutputSlot, false, 60, 8);
		guiItemStacks.set(ingredients);
	}

}
