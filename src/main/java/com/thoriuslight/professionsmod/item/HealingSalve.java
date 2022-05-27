package com.thoriuslight.professionsmod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class HealingSalve extends Item {
	private Item container;
	public HealingSalve(Properties properties, Item container) {
		super(properties);
		this.container = container;
	}
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(playerIn.getMaxHealth() > playerIn.getHealth()) {
			if (!worldIn.isClientSide) {
				playerIn.heal(2.0f);
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
			playerIn.getCooldowns().addCooldown(this, 10);
			return ActionResult.success(playerIn.getItemInHand(handIn));
		}
		return ActionResult.pass(playerIn.getItemInHand(handIn));
	}
}
