package com.thoriuslight.professionsmod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EnergySnack extends Item {
	private Item container;
	public EnergySnack(Properties properties, Item container) {
		super(properties);
		this.container = container;
	}
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isClientSide) {
			EffectInstance effectinstance = new EffectInstance(Effects.MOVEMENT_SPEED, 600, 0, false, false);
			playerIn.addEffect(new EffectInstance(effectinstance));
		}
		ItemStack stack = playerIn.getItemInHand(handIn);
		stack.shrink(1);
		if(container != null) {
			if (stack.isEmpty()) {
				return ActionResult.success(new ItemStack(container));
			}
			if (playerIn != null) {
				playerIn.inventory.add(new ItemStack(container));
			}
		}
		playerIn.getCooldowns().addCooldown(this, 300);
		return ActionResult.success(playerIn.getItemInHand(handIn));
	}
}
