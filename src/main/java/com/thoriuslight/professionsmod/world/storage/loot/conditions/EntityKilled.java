package com.thoriuslight.professionsmod.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.thoriuslight.professionsmod.init.ModLootConditionTypes;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.math.vector.Vector3d;

public class EntityKilled implements ILootCondition {
	private final EntityPredicate predicate;
	
	private EntityKilled(EntityPredicate predicateIn) {
		this.predicate = predicateIn;
	}
	
	@Override
	public boolean test(LootContext context) {
		Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
	    Vector3d blockpos = context.getParamOrNull(LootParameters.ORIGIN);
	    return this.predicate.matches(context.getLevel(), blockpos != null ? blockpos : null, entity);
	}
	
	public static class Serializer implements ILootSerializer<EntityKilled> {
		@Override
		public void serialize(JsonObject json, EntityKilled value, JsonSerializationContext context) {
			json.add("predicate", value.predicate.serializeToJson());
		}
		@Override
		public EntityKilled deserialize(JsonObject json, JsonDeserializationContext context) {
			EntityPredicate entitypredicate = EntityPredicate.fromJson(json.get("predicate"));
			return new EntityKilled(entitypredicate);
		}
	}

	@Override
	public LootConditionType getType() {
		return ModLootConditionTypes.ENTITY_KILLED;
	}
}
