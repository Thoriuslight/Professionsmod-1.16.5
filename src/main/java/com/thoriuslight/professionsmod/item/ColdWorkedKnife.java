package com.thoriuslight.professionsmod.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColdWorkedKnife extends ToolCoreItem{

	private final float attackSpeed;
	protected final int hardness;
	public ColdWorkedKnife(ModItemTier tier, float attackDamageIn, float attackSpeedIn, Properties properties) {
		super(tier, attackDamageIn - 1.0f, attackSpeedIn, properties.defaultDurability(tier.getUses()), Tool.KNIFE);
	    this.hardness = tier.getHardness();
		this.attackSpeed = attackSpeedIn;
	}
	@Override
	public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (state.getDestroySpeed(worldIn, pos) != 0.0F) {
			int i = 3;
			if(this.getHardness(stack) > (hardness/2)) {
				--i;
	    	}
			stack.hurtAndBreak(i, entityLiving, (p_220044_0_) -> {
				p_220044_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
	            if(p_220044_0_ instanceof PlayerEntity) {
	            	ItemStack itemStack = new ItemStack(ItemInit.BROKEN_KNIFE.get());
	           	 	BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(stack));
	           	 	((PlayerEntity)p_220044_0_).setItemInHand(Hand.MAIN_HAND, itemStack);
	            }
			});
		}

		return true;
	}
	
	@Override
	public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return !player.isCreative();
	}
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Block block = state.getBlock();
		if (block == Blocks.COBWEB) {
			return 15.0F;
		} else {
			Material material = state.getMaterial();
			return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
		}
	}
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		int i = 2;
		if(this.getHardness(stack) > (hardness/2)) {
			--i;
    	}
		stack.hurtAndBreak(i, attacker, (player) -> {
			player.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            if(player instanceof PlayerEntity) {
            	ItemStack itemStack = new ItemStack(ItemInit.BROKEN_KNIFE.get());
           	 	BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(stack));
           	 	if(!((PlayerEntity)player).inventory.add(itemStack)) {
           	 		((PlayerEntity) player).drop(itemStack, false);
           	 	};
            }
		});
		return true;
	}
	
	@Override
	public boolean isCorrectToolForDrops(BlockState blockIn) {
		return blockIn.getBlock() == Blocks.COBWEB;
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage + this.getHardness(stack) * (1.d / hardness), AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)this.attackSpeed, AttributeModifier.Operation.ADDITION));
		}
		return multimap;
	}
	
	@Override
	public boolean hammer(ItemStack stack) {
		int j = this.getHardness(stack);
		boolean b = repair(stack, this.hardness);
		if(j < this.hardness) {
			++j;
			this.setHardness(stack, j);
			return true;
		}
		return b;
	}
	
	@Override
	public boolean dropItem() {
		return false;
	}
}
