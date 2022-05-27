package com.thoriuslight.professionsmod.item;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap.Builder;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class ColdWorkedAxe extends ToolCoreItem{
	protected final float efficiency;
	protected final int hardness;
	private static final Set<Material> DIGGABLE_MATERIALS = Sets.newHashSet(Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE);
	private static final Set<Block> effectiveBlocks = Sets.newHashSet(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.BOOKSHELF, Blocks.OAK_WOOD, Blocks.SPRUCE_WOOD, Blocks.BIRCH_WOOD, Blocks.JUNGLE_WOOD, Blocks.ACACIA_WOOD, Blocks.DARK_OAK_WOOD, Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG, Blocks.CHEST, Blocks.PUMPKIN, Blocks.CARVED_PUMPKIN, Blocks.JACK_O_LANTERN, Blocks.MELON, Blocks.LADDER, Blocks.SCAFFOLDING, Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON, Blocks.BIRCH_BUTTON, Blocks.JUNGLE_BUTTON, Blocks.DARK_OAK_BUTTON, Blocks.ACACIA_BUTTON, Blocks.OAK_PRESSURE_PLATE, Blocks.SPRUCE_PRESSURE_PLATE, Blocks.BIRCH_PRESSURE_PLATE, Blocks.JUNGLE_PRESSURE_PLATE, Blocks.DARK_OAK_PRESSURE_PLATE, Blocks.ACACIA_PRESSURE_PLATE);
	protected static final Map<Block, Block> BLOCK_STRIPPING_MAP = (new Builder<Block, Block>()).put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).build();
	
	public ColdWorkedAxe(ModItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builder) {
		super(tier, attackDamageIn, attackSpeedIn, builder.defaultDurability(tier.getUses()).addToolType(ToolType.AXE, 0), Tool.AXE);
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
			stack.hurtAndBreak(i, entityLiving, (p_220038_0_) -> {
				p_220038_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
	            if(p_220038_0_ instanceof PlayerEntity) {
	            	 ItemStack itemStack = new ItemStack(ItemInit.BROKEN_AXE.get());
	            	 BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(stack));
	            	 ((PlayerEntity)p_220038_0_).setItemInHand(Hand.MAIN_HAND, itemStack);
	            }
			});
		}
		return true;
	}

	/**
	 * Called when this item is used when targetting a Block
	 */
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(blockpos);
		Block block = BLOCK_STRIPPING_MAP.get(blockstate.getBlock());
		if (block != null) {
			PlayerEntity playerentity = context.getPlayer();
			world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!world.isClientSide) {
				world.setBlock(blockpos, block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockstate.getValue(RotatedPillarBlock.AXIS)), 11);
				if (playerentity != null) {
					context.getItemInHand().hurtAndBreak(1, playerentity, (p_220040_1_) -> {
						p_220040_1_.broadcastBreakEvent(context.getHand());
			            if(p_220040_1_ instanceof PlayerEntity) {
			            	 ItemStack itemStack = new ItemStack(ItemInit.BROKEN_AXE.get());
			            	 BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(context.getItemInHand()));
			            	 ((PlayerEntity)p_220040_1_).setItemInHand(Hand.MAIN_HAND, itemStack);
			            }
					});
				}
			}
			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.PASS;
		}
	}
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !DIGGABLE_MATERIALS.contains(material) ? getDestroySpeed2(stack, state) : (this.efficiency + this.getHardness(stack) * 0.2f);
	}
	public float getDestroySpeed2(ItemStack stack, BlockState state) {
		if (getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e))) return (this.efficiency + this.getHardness(stack) * 0.2f);
		return ColdWorkedAxe.effectiveBlocks.contains(state.getBlock()) ? (this.efficiency + this.getHardness(stack) * 0.2f) : 1.0F;
	}
	   
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
  	  	int i = 3;
  	  	if(this.getHardness(stack) > (hardness/2)) {
  		  --i;
  	  	}
		stack.hurtAndBreak(i, attacker, (p_220039_0_) -> {
			p_220039_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            if(p_220039_0_ instanceof PlayerEntity) {
            	ItemStack itemStack = new ItemStack(ItemInit.BROKEN_AXE.get());
           	 	BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(stack));
           	 	if(!((PlayerEntity)p_220039_0_).inventory.add(itemStack)) {
           	 		((PlayerEntity) p_220039_0_).drop(itemStack, false);
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
