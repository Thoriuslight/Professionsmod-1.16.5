package com.thoriuslight.professionsmod.block;

import java.util.Random;
import java.util.stream.Stream;

import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class GlassJarBlock extends Block{
	private enum Types {
		EMPTY,
		SOURDOUGH,
		ANTIDOTE_MIX,
		ANTIDOTE
	}
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 9);
	public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3);
	private static final VoxelShape SHAPE = Stream.of(
			Block.box(5, 0, 5, 11, 10, 11),
			Block.box(6, 10, 6, 10, 11, 10),
			Block.box(5, 11, 5, 11, 12, 11)
			).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
	
	public GlassJarBlock(Properties properties) {
		super(properties.randomTicks());
	    this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(TYPE, Integer.valueOf(Types.EMPTY.ordinal())));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
	      super.tick(state, worldIn, pos, rand);
	      if (!worldIn.isAreaLoaded(pos, 1)) return;
	      switch(state.getValue(TYPE)) {
	      case 0: return;
	      case 1:
		      int i = this.getAge(state);
	          if (i < this.getMaxAge()) {
	              worldIn.setBlock(pos, GlassJarBlock.withAge(state, i + 1), 2);
	          }
	    	  break;
	      case 2:
		      int x = this.getAge(state);
	          if (x < 5) {
	              worldIn.setBlock(pos, GlassJarBlock.withAge(state, x + 1), 2);
	          }
	          else
	        	  worldIn.setBlock(pos, state.setValue(TYPE, Integer.valueOf(Types.ANTIDOTE.ordinal())), 2);
              break;
	      default:
	    	  return;
	      }
	}
	public static BlockState withAge(BlockState state, int age) {
		return state.setValue(AGE, Integer.valueOf(age));
	}
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if(iProfession.getProfession() == profession.ALCHEMIST) {
			ItemStack stack = player.getMainHandItem();
			//add Item
			if(state.getValue(TYPE) == 0) {
				if(stack.getItem() == ItemInit.SOURDOUGH.get()) {
					if (worldIn.isClientSide) {
						return ActionResultType.SUCCESS;
			        } else {
			            worldIn.playSound((PlayerEntity)null, pos, SoundEvents.SNOW_PLACE, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
			            BlockState blockstate1;
			            if(stack.getDamageValue() == 0)
			            blockstate1 = withAge(state, 1);
			            else
			            blockstate1 = withAge(state, ((25 - stack.getDamageValue())/3)+1);
			            blockstate1 = blockstate1.setValue(TYPE, Integer.valueOf(Types.SOURDOUGH.ordinal()));
			            worldIn.setBlock(pos, blockstate1, 2);
			            player.getMainHandItem().shrink(1);
			        	return ActionResultType.SUCCESS;
			        }
				}
				else if(stack.getItem() == ItemInit.ANTIDOTE_MIXTURE.get()) {
					if (worldIn.isClientSide) {
						return ActionResultType.SUCCESS;
			        } else {
			            worldIn.playSound((PlayerEntity)null, pos, SoundEvents.SNOW_PLACE, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
			            BlockState newState = state.setValue(AGE, 0).setValue(TYPE, Integer.valueOf(Types.ANTIDOTE_MIX.ordinal()));
			            worldIn.setBlock(pos, newState, 2);
			            player.getMainHandItem().shrink(1);
			        	return ActionResultType.SUCCESS;
			        }
				}
		    	return ActionResultType.PASS;
			}
			//remove Item
			else {
				if (worldIn.isClientSide) {
					return ActionResultType.SUCCESS;
		        } else {
		            worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BARREL_OPEN, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
	            	int age = this.getAge(state);
	            	BlockState blockstate1 = withAge(state, 0).setValue(TYPE, Integer.valueOf(Types.EMPTY.ordinal()));
	            	worldIn.setBlock(pos, blockstate1, 2);
	            	ItemStack itemstack = ItemStack.EMPTY;
	            	switch(state.getValue(TYPE)) {
	            	case 1:
	            		itemstack = new ItemStack(ItemInit.SOURDOUGH.get());
	            		if(age == 1)
	            			itemstack.setDamageValue(0);
	            		else
	            			itemstack.setDamageValue(25 - ((age-1) * 3));
	            		break;
	            	case 2:
	            		itemstack = new ItemStack(ItemInit.ANTIDOTE_MIXTURE.get());
	            		break;
	            	case 3:
	            		itemstack = new ItemStack(ItemInit.ANTIDOTE.get());
	            		break;
	            	}
	                if (!player.inventory.add(itemstack.copy())) {
	                    player.drop(itemstack, false);
	                }
		        	return ActionResultType.SUCCESS;
		        }
			}
		}
    	return ActionResultType.PASS;
	}
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	//@Override
	//@OnlyIn(Dist.CLIENT)
	//public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
	//	return 1.0F;
	//}
	public int getMaxAge() {
		return 9;
	}
	protected int getAge(BlockState state) {
		return state.getValue(AGE);
	}
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
	   return true;
	}
	/*@Override
	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
	   return false;
	}
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
	   return false;
	}
	@Override
	public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
	   return false;
	}
	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}*/
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE).add(TYPE);
	}
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(AGE, 0).setValue(TYPE, Integer.valueOf(Types.EMPTY.ordinal()));
	}
	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if(!worldIn.isClientSide) {
        	ItemStack itemstack = ItemStack.EMPTY;
        	int age = this.getAge(state);
        	switch(state.getValue(TYPE)) {
        	case 1:
        		itemstack = new ItemStack(ItemInit.SOURDOUGH.get());
        		if(age == 1)
        			itemstack.setDamageValue(0);
        		else
        			itemstack.setDamageValue(25 - ((age-1) * 3));
        		break;
        	case 2:
        		itemstack = new ItemStack(ItemInit.ANTIDOTE_MIXTURE.get());
        		break;
        	case 3:
        		itemstack = new ItemStack(ItemInit.ANTIDOTE.get());
        		break;
        	}
			if (!itemstack.isEmpty()) {
				worldIn.levelEvent(1010, pos, 0);
		        double d0 = (double)(worldIn.random.nextFloat() * 0.7F) + (double)0.15F;
		        double d1 = (double)(worldIn.random.nextFloat() * 0.7F) + (double)0.060000002F + 0.6D;
		        double d2 = (double)(worldIn.random.nextFloat() * 0.7F) + (double)0.15F;
		        ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack);
		        itementity.setDefaultPickUpDelay();
		        worldIn.addFreshEntity(itementity);
			}
		}
		super.playerWillDestroy(worldIn, pos, state, player);
	}
}
