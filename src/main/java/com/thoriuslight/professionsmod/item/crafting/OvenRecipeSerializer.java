package com.thoriuslight.professionsmod.item.crafting;

import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class OvenRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<OvenRecipe>{

	@Override
	public OvenRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		int resultCount = JSONUtils.getAsInt(json, "count");
		int inputCount = JSONUtils.getAsInt(json, "incount");
		boolean type = JSONUtils.getAsBoolean(json, "isexothermic");
	    JsonElement jsonelement = (JsonElement)(JSONUtils.isArrayNode(json, "ingredient") ? JSONUtils.getAsJsonArray(json, "ingredient") : JSONUtils.getAsJsonObject(json, "ingredient"));
	    Ingredient ingredient = Ingredient.fromJson(jsonelement);
        if (ingredient == null) {
        	throw new JsonParseException("No ingredients for oven recipe");
        } 
        else {
        	ItemStack bonusItem = ItemStack.EMPTY;
        	float chance = 0.f;
        	FluidStack fluidStack = FluidStack.EMPTY;
            ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            itemstack.setCount(resultCount);
        	if(json.has("bonus")) {
        		JsonObject bonus = JSONUtils.getAsJsonObject(json, "bonus");
        		if(bonus.has("item")) {
                    bonusItem = ShapedRecipe.itemFromJson(bonus);
                    chance = JSONUtils.getAsFloat(bonus, "chance");
        		}
        		if(bonus.has("fluid")) {
        			Fluid fluid = this.getFluidFromJson(bonus);
        			int mb = JSONUtils.getAsInt(bonus, "mb");
        			fluidStack = new FluidStack(fluid, mb);
        		}
        	}

            return new OvenRecipe(recipeId, ingredient, itemstack, bonusItem, type, inputCount, chance, fluidStack);
         }
	}
	@SuppressWarnings("unused")
	private Ingredient deserializeInput(JsonObject json) {
		ItemStack stack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "ingredient"));
		return Ingredient.fromValues(Stream.of(new Ingredient.SingleItemList(stack)));
	}
	@Override
    public OvenRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack itemstack = buffer.readItem();
		boolean type = buffer.readBoolean();
		int inCount = buffer.readInt();
        ItemStack bonus = buffer.readItem();
        float chance = buffer.readFloat();
        FluidStack fluid = buffer.readFluidStack();
        return new OvenRecipe(recipeId, ingredient, itemstack, bonus, type, inCount, chance, fluid);
     }
	
	@Override
	public void toNetwork(PacketBuffer buffer, OvenRecipe recipe) {
        recipe.getIngredients().get(0).toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem());
        buffer.writeBoolean(recipe.isExothermic());
        buffer.writeInt(recipe.getInputCount());
        buffer.writeItem(recipe.getBonus());
        buffer.writeFloat(recipe.getChance());
        buffer.writeFluidStack(recipe.getFluid());
     }
	@SuppressWarnings("deprecation")
	private Fluid getFluidFromJson(JsonObject json) {
	    String s = JSONUtils.getAsString(json, "fluid");
		Fluid fluid = Registry.FLUID.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
			return new JsonSyntaxException("Unknown fluid '" + s + "'");
		});
		return fluid;
	}


}
