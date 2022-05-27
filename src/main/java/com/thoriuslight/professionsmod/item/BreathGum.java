package com.thoriuslight.professionsmod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BreathGum extends Item {
	public BreathGum(Properties properties) {
		super(properties);
	}
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(playerIn.isInWater()) {
			if (!worldIn.isClientSide) {
				playerIn.setAirSupply(300);;
			}
			playerIn.getItemInHand(handIn).shrink(1);
			playerIn.getCooldowns().addCooldown(this, 20);
		}
		return ActionResult.pass(playerIn.getItemInHand(handIn));
	}
}
