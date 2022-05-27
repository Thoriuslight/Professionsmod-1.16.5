package com.thoriuslight.professionsmod.init;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.item.crafting.AlchemyRecipe;
import com.thoriuslight.professionsmod.item.crafting.AlchemyRecipeSerializer;
import com.thoriuslight.professionsmod.item.crafting.AlchemyShapelessRecipe;
import com.thoriuslight.professionsmod.item.crafting.AlchemyShapelessRecipeSerializer;
import com.thoriuslight.professionsmod.item.crafting.CrushingRecipe;
import com.thoriuslight.professionsmod.item.crafting.CrushingRecipeSerializer;
import com.thoriuslight.professionsmod.item.crafting.GrindingRecipe;
import com.thoriuslight.professionsmod.item.crafting.GrindingRecipeSerializer;
import com.thoriuslight.professionsmod.item.crafting.IAlchemyRecipe;
import com.thoriuslight.professionsmod.item.crafting.ICrushingRecipe;
import com.thoriuslight.professionsmod.item.crafting.IGrindingRecipe;
import com.thoriuslight.professionsmod.item.crafting.ISmithRecipe;
import com.thoriuslight.professionsmod.item.crafting.OvenRecipe;
import com.thoriuslight.professionsmod.item.crafting.OvenRecipeSerializer;
import com.thoriuslight.professionsmod.item.crafting.SmithRecipe;
import com.thoriuslight.professionsmod.item.crafting.SmithRecipeSerializer;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerInit {
	
	public static final IRecipeSerializer<SmithRecipe> SMITH_RECIPE_SERIALIZER = new SmithRecipeSerializer();
	public static final IRecipeSerializer<AlchemyRecipe> ALCHEMY_RECIPE_SERIALIZER = new AlchemyRecipeSerializer();
	public static final IRecipeSerializer<AlchemyShapelessRecipe> ALCHEMY_SHAPELESS_RECIPE_SERIALIZER = new AlchemyShapelessRecipeSerializer();
	public static final IRecipeSerializer<GrindingRecipe> GRINDING_RECIPE_SERIALIZER = new GrindingRecipeSerializer();
	public static final IRecipeSerializer<CrushingRecipe> CRUSHING_RECIPE_SERIALIZER = new CrushingRecipeSerializer();
	public static final IRecipeSerializer<OvenRecipe> OVEN_RECIPE_SERIALIZER = new OvenRecipeSerializer();
	public static final IRecipeType<ISmithRecipe> SMITH_TYPE = registerType(ISmithRecipe.RECIPE_TYPE_ID);
	public static final IRecipeType<IAlchemyRecipe> ALCHEMY_TYPE = registerAlchemyType(IAlchemyRecipe.RECIPE_TYPE_ID);
	public static final IRecipeType<IGrindingRecipe> GRINDING_TYPE = registerGrindingType(IGrindingRecipe.RECIPE_TYPE_ID);
	public static final IRecipeType<ICrushingRecipe> CRUSHING_TYPE = registerCrushingType(ICrushingRecipe.RECIPE_TYPE_ID);
	public static final IRecipeType<OvenRecipe> OVEN_TYPE = registerOvenType(OvenRecipe.RECIPE_TYPE_ID);
	
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ProfessionsMod.MODID);
	
	public static final RegistryObject<IRecipeSerializer<?>> SMITH_SERIALIZER = RECIPE_SERIALIZERS.register("smith", () -> SMITH_RECIPE_SERIALIZER);
	public static final RegistryObject<IRecipeSerializer<?>> ALCHEMY_SERIALIZER = RECIPE_SERIALIZERS.register("alchemy", () -> ALCHEMY_RECIPE_SERIALIZER);
	public static final RegistryObject<IRecipeSerializer<?>> ALCHEMY_SHAPELESS_SERIALIZER = RECIPE_SERIALIZERS.register("alchemy_shapeless", () -> ALCHEMY_SHAPELESS_RECIPE_SERIALIZER);
	public static final RegistryObject<IRecipeSerializer<?>> GRINDING_SERIALIZER = RECIPE_SERIALIZERS.register("grinding", () -> GRINDING_RECIPE_SERIALIZER);
	public static final RegistryObject<IRecipeSerializer<?>> CRUSHING_SERIALIZER = RECIPE_SERIALIZERS.register("crushing", () -> CRUSHING_RECIPE_SERIALIZER);
	public static final RegistryObject<IRecipeSerializer<?>> OVEN_SERIALIZER = RECIPE_SERIALIZERS.register("oven", () -> OVEN_RECIPE_SERIALIZER);

	private static IRecipeType<ISmithRecipe> registerType(ResourceLocation recipeTypeId) {
		return Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
	}
	private static IRecipeType<IAlchemyRecipe> registerAlchemyType(ResourceLocation recipeTypeId) {
		return Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
	}
	private static IRecipeType<IGrindingRecipe> registerGrindingType(ResourceLocation recipeTypeId) {
		return Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
	}
	private static IRecipeType<ICrushingRecipe> registerCrushingType(ResourceLocation recipeTypeId) {
		return Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
	}
	private static IRecipeType<OvenRecipe> registerOvenType(ResourceLocation recipeTypeId) {
		return Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
	}
	
	private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
