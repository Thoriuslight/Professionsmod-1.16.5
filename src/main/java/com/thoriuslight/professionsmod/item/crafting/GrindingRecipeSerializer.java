package com.thoriuslight.professionsmod.item.crafting;

import java.util.stream.Stream;

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

public class GrindingRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GrindingRecipe>{

	@Override
	public GrindingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		int skills = GrindingRecipeSerializer.deserializeSkills(json);
	   // JsonElement jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
	    Ingredient ingredient = deserializeInput(json);//Ingredient.fromItemListStream(Stream.of(new Ingredient.SingleItemList(new ItemStack(Items.BEEF))));//
        if (ingredient == null) {
        	throw new JsonParseException("No ingredients for grinding recipe");
        } 
        else {
            ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new GrindingRecipe(recipeId, ingredient, itemstack, skills);
         }
	}
	private static int deserializeSkills(JsonObject json) {
		String skill = JSONUtils.getAsString(json, "requirement");
		return (1 << SkillInit.ALCHEMY_SKILLS.getSkill(skill).getId());
	}
	private Ingredient deserializeInput(JsonObject json) {
		ItemStack stack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "ingredient"));
		return Ingredient.fromValues(Stream.of(new Ingredient.SingleItemList(stack)));
	}
	@Override
    public GrindingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        int skills = buffer.readInt();
        ItemStack itemstack = buffer.readItem();
        return new GrindingRecipe(recipeId, ingredient, itemstack, skills);
     }
	
	@Override
	public void toNetwork(PacketBuffer buffer, GrindingRecipe recipe) {
        recipe.getIngredients().get(0).toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem());
        buffer.writeInt(recipe.getRequirements());
     }
}
