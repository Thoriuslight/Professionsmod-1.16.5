package com.thoriuslight.professionsmod.item;

import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class MetalMortar extends Mortar implements IMeltable{
	private final ModItemTier tier;
	public MetalMortar(Properties properties, int useDuration, int capacity, ModItemTier tier) {
		super(properties, useDuration, capacity);
		this.tier = tier;
	}
	
	@Override
	protected void onBreak(LivingEntity entity, ItemStack stack) {
		if(entity instanceof PlayerEntity) {
			ItemStack itemStack = new ItemStack(ItemInit.BROKEN_MORTAR.get());
         	BrokenTool.init(itemStack, this.getType(stack), this.getNuggetAmount(stack));
         	((PlayerEntity)entity).setItemInHand(Hand.OFF_HAND, itemStack);
		}
	}

	@Override
	public int getNuggetAmount(ItemStack stack) {
		return (Tool.MORTAR.getNuggetAmount() * (100 - (50*this.getDamage(stack))/this.getMaxDamage(stack)))/100;
	}

	@Override
	public ModItemTier getType(ItemStack stack) {
		return this.tier;
	}
}
