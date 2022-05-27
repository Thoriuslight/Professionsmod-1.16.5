package com.thoriuslight.professionsmod.item;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class ColdWorkedShovel extends ToolCoreItem{
	protected final float efficiency;
	protected final int hardness;
	private static final Set<Block> DIGGABLES = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.FARMLAND, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.RED_SAND, Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.SOUL_SOIL);/** Map used to lookup shovel right click interactions */
	protected static final Map<Block, BlockState> FLATTENABLES = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.GRASS_PATH.defaultBlockState()));

	public ColdWorkedShovel(ModItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builder) {
		super(tier, attackDamageIn, attackSpeedIn, builder.defaultDurability(tier.getUses()).addToolType(ToolType.SHOVEL, 0), Tool.SHOVEL);
	    this.hardness = tier.getHardness();
	    this.efficiency = tier.getSpeed() - 0.2f * hardness;
	}
	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
	*/
	@Override
	public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (!worldIn.isClientSide && state.getDestroySpeed(worldIn, pos) != 0.0F) {
		    int i = 2;
		    if(this.getHardness(stack) > (hardness/2)) {
		    	--i;
		    }
		    stack.hurtAndBreak(i, entityLiving, (player) -> {
		    	player.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
		    	if(player instanceof PlayerEntity) {
		    		ItemStack itemStack = new ItemStack(ItemInit.BROKEN_SHOVEL.get());
		    		BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(stack));
		    		((PlayerEntity)player).setItemInHand(Hand.MAIN_HAND, itemStack);
		    	}
	    	});
		}
		return true;
	}
	
	/**
	 * Check whether this Item can harvest the given Block
	 */
	@Override
	public boolean isCorrectToolForDrops(BlockState blockIn) {
	   Block block = blockIn.getBlock();
	   return block == Blocks.SNOW || block == Blocks.SNOW_BLOCK;
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
	/**
	 * Called when this item is used when targetting a Block
	 */
	@Override
	public ActionResultType useOn(ItemUseContext context) {
	   World world = context.getLevel();
	   BlockPos blockpos = context.getClickedPos();
	   BlockState blockstate = world.getBlockState(blockpos);
	   if (context.getClickedFace() == Direction.DOWN) {
	      return ActionResultType.PASS;
	   } else {
	      PlayerEntity playerentity = context.getPlayer();
	      BlockState blockstate1 = FLATTENABLES.get(blockstate.getBlock());
	      BlockState blockstate2 = null;
	      if (blockstate1 != null && world.isEmptyBlock(blockpos.above())) {
	         world.playSound(playerentity, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
	         blockstate2 = blockstate1;
	      } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
	         world.levelEvent((PlayerEntity)null, 1009, blockpos, 0);
	         blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
	      }

	      if (blockstate2 != null) {
	         if (!world.isClientSide) {
	            world.setBlock(blockpos, blockstate2, 11);
	            if (playerentity != null) {
	               context.getItemInHand().hurtAndBreak(1, playerentity, (player) -> {
	            	   player.broadcastBreakEvent(context.getHand());
	                  if(player instanceof PlayerEntity) {
	                  	ItemStack itemStack = new ItemStack(ItemInit.BROKEN_SHOVEL.get());
	                 	BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(context.getItemInHand()));
	                 	((PlayerEntity)player).setItemInHand(Hand.MAIN_HAND, itemStack);
	                  }
	               });
	            }
	         }

	         return ActionResultType.SUCCESS;
	      } else {
	         return ActionResultType.PASS;
	      }
	   }
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e))) return (this.efficiency + this.getHardness(stack) * 0.2f);
		return ColdWorkedShovel.DIGGABLES.contains(state.getBlock()) ? (this.efficiency + this.getHardness(stack) * 0.2f) : 1.0F;
	}
	   
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
  	  	int i = 3;
  	  	if(this.getHardness(stack) > (hardness/2)) {
  		  --i;
  	  	}
		stack.hurtAndBreak(i, attacker, (player) -> {
			player.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            if(player instanceof PlayerEntity) {
            	ItemStack itemStack = new ItemStack(ItemInit.BROKEN_SHOVEL.get());
           	 	BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(stack));
           	 	if(!((PlayerEntity)player).inventory.add(itemStack)) {
           	 		((PlayerEntity) player).drop(itemStack, false);
           	 	};
            }
		});
		return true;
	}
	
	@Override
	public boolean hammer(ItemStack stack) {
		System.out.println("hammer");
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
