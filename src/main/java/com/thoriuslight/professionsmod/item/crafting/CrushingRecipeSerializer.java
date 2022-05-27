package com.thoriuslight.professionsmod.item.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.thoriuslight.professionsmod.init.SkillInit;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CrushingRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrushingRecipe> {

	@Override
	public CrushingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		int skills = CrushingRecipeSerializer.deserializeSkills(json);
		int hardness = JSONUtils.getAsInt(json, "hardness");
		JsonElement jsonelement = (JsonElement)(JSONUtils.isArrayNode(json, "ingredient") ? JSONUtils.getAsJsonArray(json, "ingredient") : JSONUtils.getAsJsonObject(json, "ingredient"));
		Ingredient ingredient = Ingredient.fromJson(jsonelement);
		if (ingredient == null) {
			throw new JsonParseException("No ingredients for grinding recipe");
		} 
		else {
			ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
			return new CrushingRecipe(recipeId, ingredient, itemstack, skills, hardness);
		}
	}
	private static int deserializeSkills(JsonObject json) {
		String skill = JSONUtils.getAsString(json, "requirement");
		return (1 << SkillInit.SMITH_SKILLS.getSkill(skill).getId());
	}
	
	@Override
	public CrushingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        int skills = buffer.readInt();
        int hardness = buffer.readInt();
        ItemStack itemstack = buffer.readItem();
        return new CrushingRecipe(recipeId, ingredient, itemstack, skills, hardness);
	}

	@Override
	public void toNetwork(PacketBuffer buffer, CrushingRecipe recipe) {
        recipe.getIngredients().get(0).toNetwork(buffer);
        buffer.writeInt(recipe.getRequirements());
        buffer.writeInt(recipe.getHardness());
        buffer.writeItem(recipe.getResultItem());
	}
}
