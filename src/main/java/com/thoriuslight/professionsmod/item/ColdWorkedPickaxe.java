package com.thoriuslight.professionsmod.item;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class ColdWorkedPickaxe extends ToolCoreItem{
	protected final float efficiency;
	protected final int hardness;
	   private static final Set<Block> DIGGABLES = ImmutableSet.of(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.POWERED_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.NETHER_GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.GRANITE, Blocks.POLISHED_GRANITE, Blocks.DIORITE, Blocks.POLISHED_DIORITE, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, Blocks.STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SANDSTONE_SLAB, Blocks.PETRIFIED_OAK_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.BRICK_SLAB, Blocks.STONE_BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, Blocks.QUARTZ_SLAB, Blocks.RED_SANDSTONE_SLAB, Blocks.PURPUR_SLAB, Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_RED_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_STONE, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE, Blocks.POLISHED_GRANITE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_DIORITE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.SMOOTH_QUARTZ_SLAB, Blocks.GRANITE_SLAB, Blocks.ANDESITE_SLAB, Blocks.RED_NETHER_BRICK_SLAB, Blocks.POLISHED_ANDESITE_SLAB, Blocks.DIORITE_SLAB, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.PISTON_HEAD);	public ColdWorkedPickaxe(ModItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
		super(tier, attackDamageIn, attackSpeedIn, builder.defaultDurability(tier.getUses()).addToolType(ToolType.PICKAXE, tier.getLevel()), Tool.PICKAXE);
	    this.hardness = tier.getHardness();
	    this.efficiency = tier.getSpeed() - 0.2f * hardness;
	}
	


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
	            	 ItemStack itemStack = new ItemStack(ItemInit.BROKEN_PICKAXE.get());
	            	 BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(stack));
	            	 ((PlayerEntity)player).setItemInHand(Hand.MAIN_HAND, itemStack);
	             }
	          });
	       }
	       return true;
	}
	@Override
	public int getHarvestLevel(ItemStack stack, ToolType tool, PlayerEntity player, BlockState blockState) {
		int k = super.getHarvestLevel(stack, tool, player, blockState);
		int i = this.getTier().getLevel();
  	  	if(this.getHardness(stack) < (hardness/2) && i != 0) {
  	  		--i;
  	  	}
		return Math.min(k, i);
	}

	@Override
	public boolean canHarvestBlock(ItemStack stack, BlockState blockIn) {
		int i = this.getTier().getLevel();
  	  	if(this.getHardness(stack) < (hardness/2) && i != 0) {
  	  		--i;
  	  	}
		if (blockIn.getHarvestTool() == net.minecraftforge.common.ToolType.PICKAXE) {
			return i >= blockIn.getHarvestLevel();
		}
		Material material = blockIn.getMaterial();
		return material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return material != Material.METAL && material != Material.HEAVY_METAL && material != Material.STONE ? getDestroySpeed2(stack, state) : (this.efficiency + this.getHardness(stack) * 0.2f);
	}

	public float getDestroySpeed2(ItemStack stack, BlockState state) {
		if (getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e))) return (this.efficiency + this.getHardness(stack) * 0.2f);
		return ColdWorkedPickaxe.DIGGABLES.contains(state.getBlock()) ? (this.efficiency + this.getHardness(stack) * 0.2f) : 1.0F;
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
            	ItemStack itemStack = new ItemStack(ItemInit.BROKEN_PICKAXE.get());
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
