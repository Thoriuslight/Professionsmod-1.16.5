package com.thoriuslight.professionsmod.event.loot;

import java.util.List;
import java.util.function.Predicate;

import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LootConditionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class MobDropModifier implements IGlobalLootModifier {
    protected final ILootCondition[] conditions;
    private final Predicate<LootContext> combinedConditions;
    @SuppressWarnings("unused")
	private final int minItem;
    @SuppressWarnings("unused")
	private final int maxItem;
    private final Item itemReward;
	protected MobDropModifier(ILootCondition[] conditionsIn, int min, int max, Item reward) {
        this.conditions = conditionsIn;
        this.combinedConditions = LootConditionManager.andConditions(conditionsIn);
		minItem = min;
		maxItem = max;
		itemReward = reward;	
	}

	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		//int num = minItem + context.getRandom().nextInt(maxItem - minItem + 1);
		int num = 1;
		generatedLoot.add(new ItemStack(itemReward, num));
        return generatedLoot;
	}
	@Override
	public List<ItemStack> apply(List<ItemStack> generatedLoot, LootContext context) {
		for(ItemStack stack : generatedLoot){
			if(stack.getItem().equals(itemReward))
				return generatedLoot;
		}
        return this.combinedConditions.test(context) ? this.doApply(generatedLoot, context) : generatedLoot;
	}

    public static class Serializer extends GlobalLootModifierSerializer<MobDropModifier> {

        @Override
        public MobDropModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            Item reward = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getAsString(object, "item"))));
            int min = JSONUtils.getAsInt(object, "min");
            int max = JSONUtils.getAsInt(object, "max");
            return new MobDropModifier(conditionsIn, min, max, reward);
        }

		@Override
		public JsonObject write(MobDropModifier instance) {
			return null;
		}
    }

}
