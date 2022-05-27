package com.thoriuslight.professionsmod.event;

import java.util.stream.Collectors;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.RecipeSerializerInit;
import com.thoriuslight.professionsmod.item.crafting.AlchemyRecipe;
import com.thoriuslight.professionsmod.item.crafting.CrushingRecipe;
import com.thoriuslight.professionsmod.item.crafting.GrindingRecipe;
import com.thoriuslight.professionsmod.item.crafting.IAlchemyRecipe;
import com.thoriuslight.professionsmod.item.crafting.ICrushingRecipe;
import com.thoriuslight.professionsmod.item.crafting.IGrindingRecipe;
import com.thoriuslight.professionsmod.item.crafting.ISmithRecipe;
import com.thoriuslight.professionsmod.item.crafting.OvenRecipe;
import com.thoriuslight.professionsmod.item.crafting.SmithRecipe;

import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public @Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.FORGE)
class RecipeUpdatedEvent {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void update(RecipesUpdatedEvent event) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if(server!=null) {
			RecipeManager recipes = server.getRecipeManager();
			SmithRecipe.recipeList = recipes.getRecipes().stream().filter(recipe -> recipe.getType() == RecipeSerializerInit.SMITH_TYPE).map(ISmithRecipe.class::cast).collect(Collectors.toMap(recipe -> recipe.getId(), recipe -> recipe));
			AlchemyRecipe.recipeList = recipes.getRecipes().stream().filter(recipe -> recipe.getType() == RecipeSerializerInit.ALCHEMY_TYPE).map(IAlchemyRecipe.class::cast).collect(Collectors.toMap(recipe -> recipe.getId(), recipe -> recipe));
			GrindingRecipe.recipeList = recipes.getRecipes().stream().filter(recipe -> recipe.getType() == RecipeSerializerInit.GRINDING_TYPE).map(IGrindingRecipe.class::cast).collect(Collectors.toMap(recipe -> recipe.getId(), recipe -> recipe));
			CrushingRecipe.recipeList = recipes.getRecipes().stream().filter(recipe -> recipe.getType() == RecipeSerializerInit.CRUSHING_TYPE).map(ICrushingRecipe.class::cast).collect(Collectors.toMap(recipe -> recipe.getId(), recipe -> recipe));
			OvenRecipe.recipeList = recipes.getRecipes().stream().filter(recipe -> recipe.getType() == RecipeSerializerInit.OVEN_TYPE).map(AbstractCookingRecipe.class::cast).collect(Collectors.toMap(recipe -> recipe.getId(), recipe -> recipe));
		}
	}
}
