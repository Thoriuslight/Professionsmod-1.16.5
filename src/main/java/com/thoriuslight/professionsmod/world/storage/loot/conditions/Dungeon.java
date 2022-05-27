package com.thoriuslight.professionsmod.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.thoriuslight.professionsmod.init.ModLootConditionTypes;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class Dungeon implements ILootCondition {
	private static final Dungeon INSTANCE = new Dungeon();
	@Override
	public boolean test(LootContext context) {
		BlockPos pos = new BlockPos(context.getParamOrNull(LootParameters.ORIGIN));
		TileEntity tile = pos == null ? null : context.getLevel().getBlockEntity(pos);
		return tile instanceof LockableLootTileEntity;
	}
	
	public static class Serializer implements ILootSerializer<Dungeon> {
		public void serialize(JsonObject json, Dungeon value, JsonSerializationContext context) {}

		public Dungeon deserialize(JsonObject json, JsonDeserializationContext context) {
			return Dungeon.INSTANCE;
		}
	}

	@Override
	public LootConditionType getType() {
		return ModLootConditionTypes.BLOCK_INVENTORY_LOOT;
	}
}
