package com.thoriuslight.professionsmod.block;

import java.util.Optional;

import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.init.RecipeSerializerInit;
import com.thoriuslight.professionsmod.item.BrokenTool;
import com.thoriuslight.professionsmod.item.IForgeable;
import com.thoriuslight.professionsmod.item.MetalHammerItem;
import com.thoriuslight.professionsmod.item.crafting.ICrushingRecipe;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.tileentity.StoneAnvilTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class StoneAnvilBlock extends Block{
	ResourceLocation copperTag = new ResourceLocation("forge:ores/copper");
	ResourceLocation silverTag = new ResourceLocation("forge:ores/silver");
	public StoneAnvilBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.STONEANVIL.get().create();
	}
	public boolean hammer(World world, BlockPos pos, PlayerEntity player, int power) {
		if (!world.isClientSide) {
			IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
			TileEntity tileentity = world.getBlockEntity(pos);
			if(tileentity instanceof StoneAnvilTileEntity) {
				StoneAnvilTileEntity anvil = (StoneAnvilTileEntity)tileentity;
				if(anvil.isfull()) {
           		 	if(anvil.hammer(power)) {
           		 		iProfession.addSkill(1, player);
           		 	}
           		 	ItemStack stack = player.getMainHandItem();
           		 	stack.hurtAndBreak(1, player, (p_220039_0_) -> {
           		 		p_220039_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
           		 		if(player.getMainHandItem().getItem() instanceof MetalHammerItem) {
           		 			MetalHammerItem item = (MetalHammerItem) stack.getItem();
           		    		ItemStack itemStack = new ItemStack(ItemInit.BROKEN_HAMMER.get());
           		    		BrokenTool.init(itemStack, item.getType(stack), item.getNuggetAmount(stack));
           		    		((PlayerEntity)player).setItemInHand(Hand.MAIN_HAND, itemStack);
           		 		}
           		 	});
           		 	return true;
				}
			}
		}
		return false;
	};
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
	      ItemStack itemstack = player.getItemInHand(handIn);
	      //---------------------------------Remove Item---------------------------------------
	      if (itemstack.isEmpty() && player.getMainHandItem().isEmpty()) {
	    	  TileEntity tileentity = worldIn.getBlockEntity(pos);
	    	  if(tileentity instanceof StoneAnvilTileEntity) {
	    		  if(!worldIn.isClientSide) {
	    			  if(((StoneAnvilTileEntity)tileentity).isfull()) {
	    				  ((StoneAnvilTileEntity)tileentity).dropItem();
	    			      return ActionResultType.SUCCESS;
	    			  }
	    		  }
	    	  }
	         return ActionResultType.PASS;
	      } 
		  //---------------------------------Add Item---------------------------------------
	      else {
	         Item item = itemstack.getItem();
	         //Class defined items
	         if(item instanceof IForgeable) {
	             TileEntity tileentity = worldIn.getBlockEntity(pos);
	             if(tileentity instanceof StoneAnvilTileEntity) {
	            	if(!worldIn.isClientSide) {
	            		if(!((StoneAnvilTileEntity)tileentity).isfull()) {
	            			((StoneAnvilTileEntity)tileentity).addItem(itemstack.copy());
	            			itemstack.shrink(1);
	            		}
	        	 	}
		         	return ActionResultType.SUCCESS;
	             }
	         }
	         //Recipe defined Items
	         else {
	        	 Optional<ICrushingRecipe> optional;
	        	 if(!worldIn.isClientSide) {
	        		 optional = worldIn.getServer().getRecipeManager().getRecipeFor(RecipeSerializerInit.CRUSHING_TYPE, new Inventory(itemstack), worldIn);
	        	 } else {
	        		 optional = worldIn.getRecipeManager().getRecipeFor(RecipeSerializerInit.CRUSHING_TYPE, new Inventory(itemstack), worldIn);
	        	 }
	        	 if (optional.isPresent()) {
	        		 TileEntity tileentity = worldIn.getBlockEntity(pos);
	        		 if(tileentity instanceof StoneAnvilTileEntity) {
	 	            	if(!((StoneAnvilTileEntity)tileentity).isfull()) {
	 	            		if(!worldIn.isClientSide) {
	 	            			ICrushingRecipe recipe = optional.get();
	 	            			ItemStack stack = itemstack.copy();
	 	            			int i = 1;
	 	            			for(ItemStack iStack : recipe.getIngredients().get(0).getItems()) {
	 	            				if(iStack.getItem().equals(stack.getItem())) {
	 	            					i = iStack.getCount();
	 	            					break;
	 	            				}
	 	            			}
	 	            			stack.setCount(i);
		            			((StoneAnvilTileEntity)tileentity).addItem(stack, recipe);
		            			itemstack.shrink(i);
		            		}
	 	            		return ActionResultType.SUCCESS;
	        			 }
	        		 }
	        	 }
	         }
	         return ActionResultType.PASS;
	      }
	}
	
	public void dropItem(World worldIn, BlockPos pos, ItemStack itemstack) {
		if (!itemstack.isEmpty()) {
			worldIn.levelEvent(1010, pos, 0);
			double d0 = (double)(worldIn.random.nextFloat() * 0.7F) + 0.15D;
			double d1 = (double)(worldIn.random.nextFloat() * 0.3F) + 1.0D;
			double d2 = (double)(worldIn.random.nextFloat() * 0.7F) + 0.15D;
			ItemStack itemstack1 = itemstack.copy();
			ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
			itementity.setDeltaMovement(worldIn.random.nextDouble() * 0.1D - 0.05D, 0.1D, worldIn.random.nextDouble() * 0.1D - 0.05D);
			itementity.setPickUpDelay(5);
			worldIn.addFreshEntity(itementity);
		}      
	}
	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        if(tileentity instanceof StoneAnvilTileEntity) {
        	if(!worldIn.isClientSide) {
        		((StoneAnvilTileEntity)tileentity).dropItem();
        	}
        }
		super.playerWillDestroy(worldIn, pos, state, player);
	}
}
