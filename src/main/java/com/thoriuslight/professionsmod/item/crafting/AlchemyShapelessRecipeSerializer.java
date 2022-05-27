package com.thoriuslight.professionsmod.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.thoriuslight.professionsmod.init.SkillInit;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class AlchemyShapelessRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlchemyShapelessRecipe>{

	@Override
	public AlchemyShapelessRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		int skills = AlchemyShapelessRecipeSerializer.deserializeSkills(json);
        NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));
        if (nonnulllist.isEmpty()) {
        	throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (nonnulllist.size() > 9) {
        	throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 9);
        } else {
            ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new AlchemyShapelessRecipe(recipeId, nonnulllist, itemstack, skills);
         }
	}
	private static int deserializeSkills(JsonObject json) {
		String skill = JSONUtils.getAsString(json, "requirement");
		return (1 << SkillInit.ALCHEMY_SKILLS.getSkill(skill).getId());
	}
    private static NonNullList<Ingredient> readIngredients(JsonArray p_199568_0_) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for(int i = 0; i < p_199568_0_.size(); ++i) {
           Ingredient ingredient = Ingredient.fromJson(p_199568_0_.get(i));
           if (!ingredient.isEmpty()) {
              nonnulllist.add(ingredient);
           }
        }

        return nonnulllist;
     }
	@Override
    public AlchemyShapelessRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
		int i = buffer.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
        for(int j = 0; j < nonnulllist.size(); ++j) {
        	nonnulllist.set(j, Ingredient.fromNetwork(buffer));
        }
        int skills = buffer.readInt();
        ItemStack itemstack = buffer.readItem();
        return new AlchemyShapelessRecipe(recipeId, nonnulllist, itemstack, skills);
     }
	
	@Override
	public void toNetwork(PacketBuffer buffer, AlchemyShapelessRecipe recipe) {
		buffer.writeVarInt(recipe.getIngredients().size());
        for(Ingredient ingredient : recipe.getIngredients()) {
           ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.getResultItem());
        buffer.writeInt(recipe.getRequirements());
     }



}
