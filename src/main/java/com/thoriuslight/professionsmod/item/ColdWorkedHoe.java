package com.thoriuslight.professionsmod.item;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public class ColdWorkedHoe extends ToolCoreItem{
	protected static final Map<Block, BlockState> HOE_LOOKUP = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.defaultBlockState(), Blocks.GRASS_PATH, Blocks.FARMLAND.defaultBlockState(), Blocks.DIRT, Blocks.FARMLAND.defaultBlockState(), Blocks.COARSE_DIRT, Blocks.DIRT.defaultBlockState()));
	protected final int hardness;
	private final float speed;
	protected static final Set<Block> effectiveBlocks = ImmutableSet.of(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.POWERED_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.GRANITE, Blocks.POLISHED_GRANITE, Blocks.DIORITE, Blocks.POLISHED_DIORITE, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, Blocks.STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SANDSTONE_SLAB, Blocks.PETRIFIED_OAK_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.BRICK_SLAB, Blocks.STONE_BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, Blocks.QUARTZ_SLAB, Blocks.RED_SANDSTONE_SLAB, Blocks.PURPUR_SLAB, Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_RED_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_STONE, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE, Blocks.POLISHED_GRANITE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_DIORITE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.SMOOTH_QUARTZ_SLAB, Blocks.GRANITE_SLAB, Blocks.ANDESITE_SLAB, Blocks.RED_NETHER_BRICK_SLAB, Blocks.POLISHED_ANDESITE_SLAB, Blocks.DIORITE_SLAB, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX);
	public ColdWorkedHoe(ModItemTier tier, float attackSpeedIn, Properties builder) {
		super(tier, 0.0f, attackSpeedIn, builder.defaultDurability(tier.getUses()).addToolType(ToolType.HOE, tier.getLevel()), Tool.HOE);
	    this.hardness = tier.getHardness();
	    this.speed = attackSpeedIn;
	}
	   /**
	    * Called when this item is used when targetting a Block
	    */
	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
	    BlockPos blockpos = context.getClickedPos();
	    int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
	    if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
	    	if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
	    		BlockState blockstate = HOE_LOOKUP.get(world.getBlockState(blockpos).getBlock());
	    		if (blockstate != null) {
	    			PlayerEntity playerentity = context.getPlayer();
	    			world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
	    			if (!world.isClientSide) {
	    				world.setBlock(blockpos, blockstate, 11);
	    				if (playerentity != null) {
	    					ItemStack Item = context.getItemInHand();
	    			 	  	int i = 2;
	    			  	  	if(this.getHardness(Item) > (hardness/2)) {
	    			  		  --i;
	    			  	  	}
	    					Item.hurtAndBreak(i, playerentity, (p_220043_1_) -> {
	    						p_220043_1_.broadcastBreakEvent(context.getHand());
	    			            if(p_220043_1_ instanceof PlayerEntity) {
	    			            	ItemStack itemStack = new ItemStack(ItemInit.BROKEN_HOE.get());
	    			           	 	BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(context.getItemInHand()));
	    			            	((PlayerEntity)p_220043_1_).setItemInHand(Hand.MAIN_HAND, itemStack);
	    			            }
	    					});
	    				}
	    			}
	    			return ActionResultType.SUCCESS;
	    		}
	    	}
	    	return ActionResultType.PASS;
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
            	ItemStack itemStack = new ItemStack(ItemInit.BROKEN_HOE.get());
           	 	BrokenTool.init(itemStack, this.getTier(), this.getNuggetAmount(stack));
           	 	if(!((PlayerEntity)player).inventory.add(itemStack)) {
           	 		((PlayerEntity) player).drop(itemStack, false);
           	 	};
            }
		});
		return true;
	}
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 0.0D, AttributeModifier.Operation.ADDITION));
		    multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)this.speed, AttributeModifier.Operation.ADDITION));
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
