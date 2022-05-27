package com.thoriuslight.professionsmod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class Antidote extends Item {
	public Antidote(Properties properties) {
		super(properties);
	}
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(playerIn.removeEffect(Effects.POISON)) {
			ItemStack stack = playerIn.getItemInHand(handIn);
			stack.shrink(1);
			if (stack.isEmpty()) {
				return ActionResult.success(new ItemStack(Items.GLASS_BOTTLE));
			}
			if (playerIn != null) {
				playerIn.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
			}
			playerIn.getCooldowns().addCooldown(this, 40);
			return ActionResult.success(playerIn.getItemInHand(handIn));
		}
		return ActionResult.pass(playerIn.getItemInHand(handIn));
	}
}
