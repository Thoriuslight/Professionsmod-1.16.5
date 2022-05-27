package com.thoriuslight.professionsmod.util.combat.jei;

import javax.annotation.Nullable;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.item.crafting.AlchemyRecipe;
import com.thoriuslight.professionsmod.item.crafting.CrushingRecipe;
import com.thoriuslight.professionsmod.item.crafting.GrindingRecipe;
import com.thoriuslight.professionsmod.item.crafting.OvenRecipe;
import com.thoriuslight.professionsmod.item.crafting.SmithRecipe;
import com.thoriuslight.professionsmod.util.combat.jei.alchemy.AlchemistRecipeCategory;
import com.thoriuslight.professionsmod.util.combat.jei.alchemy.GrindingRecipeCategory;
import com.thoriuslight.professionsmod.util.combat.jei.alchemy.OvenRecipeCategory;
import com.thoriuslight.professionsmod.util.combat.jei.smith.CrushingRecipeCategory;
import com.thoriuslight.professionsmod.util.combat.jei.smith.SmithRecipeCategory;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class ProfessionPlugin implements IModPlugin{
	@Nullable
	private SmithRecipeCategory smithCategory;
	@Nullable
	private AlchemistRecipeCategory alchemistCategory;
	@Nullable
	private GrindingRecipeCategory grindingCategory;
	@Nullable
	private CrushingRecipeCategory crushingCategory;
	@Nullable
	private OvenRecipeCategory ovenCategory;
	
	ResourceLocation Uid =  new ResourceLocation(ProfessionsMod.MODID, "main");
	@Override
	public ResourceLocation getPluginUid() {
		return Uid;
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		smithCategory = new SmithRecipeCategory(guiHelper);
		alchemistCategory = new AlchemistRecipeCategory(guiHelper);
		grindingCategory = new GrindingRecipeCategory(guiHelper);
		crushingCategory = new CrushingRecipeCategory(guiHelper);
		ovenCategory = new OvenRecipeCategory(guiHelper);
		registration.addRecipeCategories(smithCategory);
		registration.addRecipeCategories(alchemistCategory);
		registration.addRecipeCategories(grindingCategory);
		registration.addRecipeCategories(crushingCategory);
		registration.addRecipeCategories(ovenCategory);
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(SmithRecipe.recipeList.values(), SmithRecipeCategory.Uid);
		registration.addRecipes(AlchemyRecipe.recipeList.values(), AlchemistRecipeCategory.Uid);
		registration.addRecipes(GrindingRecipe.recipeList.values(), GrindingRecipeCategory.Uid);
		registration.addRecipes(CrushingRecipe.recipeList.values(), CrushingRecipeCategory.Uid);
		registration.addRecipes(OvenRecipe.recipeList.values(), OvenRecipeCategory.Uid);
	}
}
