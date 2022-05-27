package com.thoriuslight.professionsmod.tileentity;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.inventory.container.WoodenHopperContainer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class WoodenHopperTileEntity extends LockableLootTileEntity implements ITickableTileEntity{

	protected WoodenHopperTileEntity(TileEntityType<?> p_i48284_1_) {
		super(p_i48284_1_);
		// TODO Auto-generated constructor stub
	}
	/*
	private NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
	//private IItemHandlerModifiable items = createHandler();
	//private LazyOptional<IItemHandlerModifiable> ItemHandler = LazyOptional.of(() -> items);
	private int transferCooldown = -1;
	private long tickedGameTime;
	
	public WoodenHopperTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
	public WoodenHopperTileEntity() {
		super(ModTileEntityTypes.WOODEN_HOPPER.get());
		
	}
	
	public void read(CompoundNBT compound) {
		super.read(compound);
	    this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
	    if (!this.checkLootAndRead(compound)) {
	       ItemStackHelper.loadAllItems(compound, this.inventory);
	    }
	    this.transferCooldown = compound.getInt("TransferCooldown");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		if (!this.checkLootAndWrite(compound)) {
			ItemStackHelper.saveAllItems(compound, this.inventory);
	    }

	    compound.putInt("TransferCooldown", this.transferCooldown);
	    return compound;
	}
	

	@Override
	public ItemStack decrStackSize(int index, int count) {
		this.fillWithLoot((PlayerEntity)null);
		return ItemStackHelper.getAndSplit(this.getItems(), index, count);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.fillWithLoot((PlayerEntity)null);
		this.getItems().set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}
	
	@Override
	public void tick() {
		if (this.world != null && !this.world.isRemote) {
			--this.transferCooldown;
			this.tickedGameTime = this.world.getGameTime();
			if (!this.isOnTransferCooldown()) {
				this.setTransferCooldown(0);
				this.updateHopper(() -> {
					return false;
				});
			}
		}
	}
	private boolean updateHopper(Supplier<Boolean> bool) {
		if (this.world != null && !this.world.isRemote) {
	        if (!this.isOnTransferCooldown()){
	        	boolean flag = false;
	        	if(!this.isEmpty()){
	        		flag = this.transferItemsOut();
	        	}
	        	if(flag) {
					this.setTransferCooldown(20);
					this.markDirty();
					return true;
	        	}
	        }
			return false;
		}
	    else {
	    	return false;
		}
	}
	private boolean transferItemsOut() {
		if(insertHook()) return true;
	    IInventory iinventory = getInventoryAtPosition(this.getWorld(), this.pos.offset(Direction.DOWN));
	    if (iinventory == null) {
	    	return false;
	    } else {
	    	if (this.isInventoryFull(iinventory, Direction.UP)) {
	    		return false;
	        }else {
	        	if (!this.getStackInSlot(0).isEmpty()) {
	        		ItemStack itemstack = this.getStackInSlot(0).copy();
	        		ItemStack itemstack1 = putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(0, 1), Direction.UP);
	        		if (itemstack1.isEmpty()) {
	        			iinventory.markDirty();
	        			return true;
	        		}
	        		this.setInventorySlotContents(0, itemstack);
	        	}
	        }
	    	return false;
	    }
	}
	
	public static ItemStack putStackInInventoryAllSlots(@Nullable IInventory source, IInventory destination, ItemStack stack, @Nullable Direction direction) {
		      if (destination instanceof ISidedInventory && direction != null) {
		         ISidedInventory isidedinventory = (ISidedInventory)destination;
		         int[] aint = isidedinventory.getSlotsForFace(direction);

		         for(int k = 0; k < aint.length && !stack.isEmpty(); ++k) {
		            stack = insertStack(source, destination, stack, aint[k], direction);
		         }
		      } else {
		         int i = destination.getSizeInventory();

		         for(int j = 0; j < i && !stack.isEmpty(); ++j) {
		            stack = insertStack(source, destination, stack, j, direction);
		         }
		      }

		      return stack;
		   }
	private boolean isInventoryFull(IInventory iinventory, Direction side) {
	      return func_213972_a(iinventory, side).allMatch((p_213970_1_) -> {
	          ItemStack itemstack = iinventory.getStackInSlot(p_213970_1_);
	          return itemstack.getCount() >= itemstack.getMaxStackSize();
	       });
	}
	private static IntStream func_213972_a(IInventory p_213972_0_, Direction p_213972_1_) {
		return p_213972_0_ instanceof ISidedInventory ? IntStream.of(((ISidedInventory)p_213972_0_).getSlotsForFace(p_213972_1_)) : IntStream.range(0, p_213972_0_.getSizeInventory());
	}

	@Nullable
	private static IInventory getInventoryAtPosition(World world, BlockPos offset) {
	      return getInventoryAtPosition(world, (double)offset.getX() + 0.5D, (double)offset.getY() + 0.5D, (double)offset.getZ() + 0.5D);
	}
	@Nullable
	public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z) {
	      IInventory iinventory = null;
	      BlockPos blockpos = new BlockPos(x, y, z);
	      BlockState blockstate = worldIn.getBlockState(blockpos);
	      Block block = blockstate.getBlock();
	      if (block instanceof ISidedInventoryProvider) {
	         iinventory = ((ISidedInventoryProvider)block).createInventory(blockstate, worldIn, blockpos);
	      } else if (blockstate.hasTileEntity()) {
	         TileEntity tileentity = worldIn.getTileEntity(blockpos);
	         if (tileentity instanceof IInventory) {
	            iinventory = (IInventory)tileentity;
	            if (iinventory instanceof ChestTileEntity && block instanceof ChestBlock) {
	               iinventory = ChestBlock.func_226916_a_((ChestBlock)block, blockstate, worldIn, blockpos, true);
	            }
	         }
	      }

	      if (iinventory == null) {
	         List<Entity> list = worldIn.getEntitiesInAABBexcluding((Entity)null, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntityPredicates.HAS_INVENTORY);
	         if (!list.isEmpty()) {
	            iinventory = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
	         }
	      }

	      return iinventory;
	}

	private boolean insertHook() {
		return false;
	}
	   private static ItemStack insertStack(@Nullable IInventory source, IInventory destination, ItemStack stack, int index, @Nullable Direction direction) {
		      ItemStack itemstack = destination.getStackInSlot(index);
		      if (canInsertItemInSlot(destination, stack, index, direction)) {
		         boolean flag = false;
		         boolean flag1 = destination.isEmpty();
		         if (itemstack.isEmpty()) {
		            destination.setInventorySlotContents(index, stack);
		            stack = ItemStack.EMPTY;
		            flag = true;
		         } else if (canCombine(itemstack, stack)) {
		            int i = stack.getMaxStackSize() - itemstack.getCount();
		            int j = Math.min(stack.getCount(), i);
		            stack.shrink(j);
		            itemstack.grow(j);
		            flag = j > 0;
		         }

		         if (flag) {
		            if (flag1 && destination instanceof WoodenHopperTileEntity) {
		               WoodenHopperTileEntity hoppertileentity1 = (WoodenHopperTileEntity)destination;
		               if (!hoppertileentity1.mayTransfer()) {
		                  int k = 0;
		                  if (source instanceof WoodenHopperTileEntity) {
		                     WoodenHopperTileEntity hoppertileentity = (WoodenHopperTileEntity)source;
		                     if (hoppertileentity1.tickedGameTime >= hoppertileentity.tickedGameTime) {
		                        k = 1;
		                     }
		                  }

		                  hoppertileentity1.setTransferCooldown(20 - k);
		               }
		            }

		            destination.markDirty();
		         }
		      }

		      return stack;
		   }
	   private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
		      if (stack1.getItem() != stack2.getItem()) {
		         return false;
		      } else if (stack1.getDamage() != stack2.getDamage()) {
		         return false;
		      } else if (stack1.getCount() > stack1.getMaxStackSize()) {
		         return false;
		      } else {
		         return ItemStack.areItemStackTagsEqual(stack1, stack2);
		      }
		   }
	   private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, @Nullable Direction side) {
		      if (!inventoryIn.isItemValidForSlot(index, stack)) {
		         return false;
		      } else {
		         return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
		      }
		   }
	public boolean mayTransfer() {
		return this.transferCooldown > 20;
	}
	public void setTransferCooldown(int ticks) {
		this.transferCooldown = ticks;
	}

	private boolean isOnTransferCooldown() {
		return this.transferCooldown > 0;
	}
			   
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {
		this.inventory = itemsIn;
		
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.wooden_hopper");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new WoodenHopperContainer(id, player, this);
	}

*/

	@Override
	public int getContainerSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> p_199721_1_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ITextComponent getDefaultName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
		// TODO Auto-generated method stub
		return null;
	}
}
