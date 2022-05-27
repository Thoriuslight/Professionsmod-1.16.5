package com.thoriuslight.professionsmod.item.crafting;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.item.ButtedChainArmorItem;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SmithRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmithRecipe>{

	@Override
	public SmithRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		int skills = SmithRecipeSerializer.deserializeSkills(json);
        Map<String, Ingredient> map = SmithRecipeSerializer.deserializeKey(JSONUtils.getAsJsonObject(json, "key"));
        String[] astring = SmithRecipeSerializer.shrink(SmithRecipeSerializer.patternFromJson(JSONUtils.getAsJsonArray(json, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
		NonNullList<Ingredient> input = deserializeIngredients(astring, map, i, j);
		ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);
		if(json.has("mode")) {
			if(JSONUtils.getAsString(json, "mode").equals("1")) {
				output.setDamageValue(output.getMaxDamage()-1);
				if(output.getItem() instanceof ButtedChainArmorItem) {
					((ButtedChainArmorItem)output.getItem()).crafted(output);
				}
			}
		}
		return new SmithRecipe(recipeId, i, j, input, output, skills);
	}
	private static int deserializeSkills(JsonObject json) {
		String skill = JSONUtils.getAsString(json, "requirement");
		return (1 << SkillInit.SMITH_SKILLS.getSkill(skill).getId());
	}
	private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
		NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
		Set<String> set = Sets.newHashSet(keys.keySet());
		set.remove(" ");
		for(int i = 0; i < pattern.length; ++i) {
			for(int j = 0; j < pattern[i].length(); ++j) {
				String s = pattern[i].substring(j, j + 1);
				Ingredient ingredient = keys.get(s);
				if (ingredient == null) {
					throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
				}
				set.remove(s);
				nonnulllist.set(j + patternWidth * i, ingredient);
			}
		}
		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		} else {
			return nonnulllist;
		}
	}
	
	private static Map<String, Ingredient> deserializeKey(JsonObject json) {
		Map<String, Ingredient> map = Maps.newHashMap();
		for(Entry<String, JsonElement> entry : json.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}
			if (" ".equals(entry.getKey())) {
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}
			map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
		}
		map.put(" ", Ingredient.EMPTY);
		return map;
	}
	
	@VisibleForTesting
	static String[] shrink(String... toShrink) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;
		for(int i1 = 0; i1 < toShrink.length; ++i1) {
			String s = toShrink[i1];
			i = Math.min(i, firstNonSpace(s));
			int j1 = lastNonSpace(s);
			j = Math.max(j, j1);
			if (j1 < 0) {
				if (k == i1) {
					++k;
				}
				++l;
			} else {
				l = 0;
			}
		}
		if (toShrink.length == l) {
			return new String[0];
		} else {
			String[] astring = new String[toShrink.length - l - k];
			for(int k1 = 0; k1 < astring.length; ++k1) {
				astring[k1] = toShrink[k1 + k].substring(i, j + 1);
			}
	        return astring;
		}
	}
	
	private static int firstNonSpace(String str) {
		int i;
		for(i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {
			;
		}
		return i;
	}

	private static int lastNonSpace(String str) {
		int i;
		for(i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
			;
		}
		return i;
	}
	
	private static String[] patternFromJson(JsonArray jsonArr) {
		String[] astring = new String[jsonArr.size()];
		if (astring.length > 3) {
			throw new JsonSyntaxException("Invalid pattern: too many rows, " + 3 + " is maximum");
		} else if (astring.length == 0) {
			throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
		} else {
			for(int i = 0; i < astring.length; ++i) {
				String s = JSONUtils.convertToString(jsonArr.get(i), "pattern[" + i + "]");
				if (s.length() > 3) {
					throw new JsonSyntaxException("Invalid pattern: too many columns, " + 3 + " is maximum");
				}
				if (i > 0 && astring[0].length() != s.length()) {
					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				}
				astring[i] = s;
			}
			return astring;
		}
	}
	
	@Override
	public SmithRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
		int i = buffer.readVarInt();
        int j = buffer.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
        for(int k = 0; k < nonnulllist.size(); ++k) {
           nonnulllist.set(k, Ingredient.fromNetwork(buffer));
        }
        ItemStack itemstack = buffer.readItem();
        int skills = buffer.readInt();
        return new SmithRecipe(recipeId, i, j, nonnulllist, itemstack, skills);
	}

	@Override
	public void toNetwork(PacketBuffer buffer, SmithRecipe recipe) {
        buffer.writeVarInt(recipe.getRecipeWidth());
        buffer.writeVarInt(recipe.getRecipeHeight());

        for(Ingredient ingredient : recipe.getIngredients()) {
        	ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.getResultItem());
        buffer.writeInt(recipe.getRequirements());
	}

}
