package com.thoriuslight.professionsmod.tileentity;


import java.util.Random;

import javax.annotation.Nullable;

import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.init.RecipeSerializerInit;
import com.thoriuslight.professionsmod.inventory.container.OvenContainer;
import com.thoriuslight.professionsmod.item.crafting.OvenRecipe;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class OvenControllerTileEntity extends TileEntity implements INamedContainerProvider, IInventory, IRecipeHolder, ITickableTileEntity {
	private ITextComponent customName = new TranslationTextComponent("container.professionsmod.oven");
	protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
	private int direction;
	private int burnTime;
	private int recipesUsed;
	private int cookTime;
	private int cookTimeTotal;
	private BlockPos[] extractorPositions = new BlockPos[6];
	protected final IIntArray furnaceData = new IIntArray() {
		@Override
		public int get(int index) {
			switch(index) {
			case 0:
	            return OvenControllerTileEntity.this.burnTime;
	         case 1:
	            return OvenControllerTileEntity.this.recipesUsed;
	         case 2:
	            return OvenControllerTileEntity.this.cookTime;
	         case 3:
	            return OvenControllerTileEntity.this.cookTimeTotal;
	         default:
	            return 0;
			}
		}
		@Override
		public void set(int index, int value) {
			switch(index) {
			case 0:
				OvenControllerTileEntity.this.burnTime = value;
	            break;
	         case 1:
	        	 OvenControllerTileEntity.this.recipesUsed = value;
	            break;
	         case 2:
	        	 OvenControllerTileEntity.this.cookTime = value;
	            break;
	         case 3:
	        	 OvenControllerTileEntity.this.cookTimeTotal = value;
			}

		}
		@Override
		public int getCount() {
			return 4;
		}
	};
	
	public OvenControllerTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		this.direction = 0;
		for(int i = 0; i < 6; ++i) {
			this.extractorPositions[i] = null;
		}
	}
	public OvenControllerTileEntity() {
		this(ModTileEntityTypes.OVEN_CONTROLLER.get());
	}
	public void setDirection(int dir) {
		this.direction = dir;
	}
	public float getDirection() {
		return this.direction * 90.f;
	}
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(worldPosition.offset(-1, -1, -1), worldPosition.offset(2, 2, 2));
	}
	
	public void destroyMultiblock(BlockPos pos, World world, BlockPos destroyPos) {
		//dropItems
        InventoryHelper.dropContents(world, destroyPos, this);
		//convertMultiBlock
		pos = pos.offset(-1, -1, -1);
		BlockState state = BlockInit.INSULATION_BRICK_BLOCK.get().defaultBlockState();
		for(int x = 0; x < 3; ++x) {
			for(int y = 0; y < 3; ++y) {
				for(int z = 0; z < 3; ++z) {
					BlockPos relPos = pos.offset(x, y, z);
					if(world.getBlockState(relPos).getBlock().equals(BlockInit.OVEN_BRICK_BLOCK.get()) || world.getBlockState(relPos).getBlock().equals(BlockInit.OVEN_CONTROLLER_BLOCK.get())) {
						world.setBlock(relPos, state, 2);
					}
				}
			}
		}
	}
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity p_createMenu_3_) {
		return new OvenContainer(windowId, playerInv, this, this.furnaceData);
	}
	@Override
	public ITextComponent getDisplayName() {
		return customName;
	}
	private boolean isBurning() {
		return this.burnTime > 0;
	}
	@Override
	public void tick() {
		boolean lastState = this.isBurning();
	    boolean isDirty = false;
		if (this.isBurning()) {
			--this.burnTime;
		}
		if (!this.level.isClientSide) {
			ItemStack fuelStack = this.items.get(1);
            IRecipe<?> irecipe = this.level.getRecipeManager().getRecipeFor(RecipeSerializerInit.OVEN_TYPE, this, this.level).orElse(null);
            boolean exotherm = false;
            if(irecipe != null) {
            	exotherm = ((OvenRecipe)irecipe).isExothermic();
            }
			if (exotherm || this.isBurning() || !fuelStack.isEmpty() && !this.items.get(0).isEmpty()) {
	            boolean isSpecial = true;
	            int inCount;
	            if(irecipe == null) {
		            irecipe = this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, this.level).orElse(null);
		            isSpecial = false;
		            inCount = 8;
	            } else {
	            	inCount = ((OvenRecipe)irecipe).getInputCount();
	            }
	            if(this.canSmelt(irecipe, isSpecial, inCount)) {
	            	if(exotherm) {
		            	this.burnTime = 1;
		            	this.recipesUsed = this.burnTime;
	            	}
	            	else if (!this.isBurning()) {
		            	this.burnTime = net.minecraftforge.common.ForgeHooks.getBurnTime(fuelStack, null)/2;
		            	this.recipesUsed = this.burnTime;
						if (this.isBurning()) {
							isDirty = true;
							if (fuelStack.hasContainerItem())
								this.items.set(1, fuelStack.getContainerItem());
							else if (!fuelStack.isEmpty()) {
								fuelStack.shrink(1);
								if (fuelStack.isEmpty()) {
									this.items.set(1, fuelStack.getContainerItem());
								}
							}
						}
					}
		            if ((this.isBurning() || exotherm)) {
		                ++this.cookTime;
		                if (this.cookTime == this.cookTimeTotal) {
		                    this.cookTime = 0;
		                    this.cookTimeTotal = this.getCookTime();
		                    this.smelt(irecipe, isSpecial, inCount);
		                    isDirty = true;
		                 }
		            }
	            }
	            else {
	                this.cookTime = 0;
	            }
            }
			if (lastState != this.isBurning()) {
				isDirty = true;
				this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isBurning())), 3);
			}
		}
		if (isDirty) {
			this.setChanged();
		}
	}
	protected boolean canSmelt(@Nullable IRecipe<?> recipeIn, boolean flag, int count) {
		if (!this.items.get(0).isEmpty() && recipeIn != null) {
			if(this.getCookTime() > 200) {
				return false;
			}
			ItemStack itemstack = recipeIn.getResultItem();
			ItemStack itemstack2 = this.items.get(0);
			if (itemstack.isEmpty()) {
				return false;
			} 
			else if(flag && (itemstack2.getCount() < count)){
				return false;
			}
			else {
			
				ItemStack itemstack1 = this.items.get(2);
				if (itemstack1.isEmpty()) {
					return true;
				} else if (!itemstack1.sameItem(itemstack)) {
					return false;
	            } else if (itemstack1.getCount() + itemstack.getCount() <= this.getMaxStackSize() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
	                return true;
	            } else {
	            	return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
	            }
			}
		} else {
			return false;
		}
	}
	private void smelt(@Nullable IRecipe<?> recipe, boolean flag, int count) {
		if (recipe != null && this.canSmelt(recipe, flag, count)) {
			ItemStack itemstack = this.items.get(0);
			ItemStack itemstack1 = recipe.getResultItem();
			ItemStack itemstack2 = this.items.get(2);
			int i = Math.min(itemstack.getCount(), count);
			int k = i;
			if(flag) {
				OvenRecipe oRecipe = (OvenRecipe)recipe;
				k = 1;
				if(!oRecipe.getBonus().isEmpty()) {
					Random generator = new Random();
					float r = generator.nextFloat();
					if(r < oRecipe.getChance()) {
						for(int t = 0; t < 6; ++t) {
							if(this.extractorPositions[t] != null) {
								TileEntity tile = this.level.getBlockEntity(this.extractorPositions[t]);
								if(tile instanceof ExtractorTileEntity) {
									if(((ExtractorTileEntity)tile).addItem(oRecipe.getBonus().copy())) {
										break;
									}
								}
								else {
									this.extractorPositions[t] = null;
									this.setChanged();
								}
							}
						}
					}
				}
				if(!oRecipe.getFluid().isEmpty()) {
					FluidStack fluid = oRecipe.getFluid().copy();
					for(int t = 0; t < 6; ++t) {
						if(this.extractorPositions[t] != null) {
							TileEntity tile = this.level.getBlockEntity(this.extractorPositions[t]);
							if(tile instanceof ExtractorTileEntity) {
								int rem = ((ExtractorTileEntity)tile).fill(fluid, FluidAction.EXECUTE);
								BlockState state = this.level.getBlockState(this.extractorPositions[t]);
						        this.level.sendBlockUpdated(this.extractorPositions[t], state, state, Constants.BlockFlags.BLOCK_UPDATE);
								if(rem == 0) {
									break;
								}
								fluid.setAmount(rem);
							}
							else {
								this.extractorPositions[t] = null;
								this.setChanged();
							}
						}
					}
				}
			}
			if (itemstack2.isEmpty()) {
				ItemStack output = itemstack1.copy();
				if(!flag)
					output.setCount(itemstack1.getCount() * k);
				this.items.set(2, output);
			} 
			else if (itemstack2.getItem() == itemstack1.getItem()) {
				if(!flag) {
					int x = (Math.min(this.getMaxStackSize(), itemstack1.getMaxStackSize()) - itemstack2.getCount()) / itemstack1.getCount();
					i = Math.min(i, x);
				}
				itemstack2.grow(itemstack1.getCount() * k);
	        }
			itemstack.shrink(i);
		}
	}
	protected int getCookTime() {
		return this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
	}
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
	    this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
	    ItemStackHelper.loadAllItems(compound, this.items);
	    this.burnTime = compound.getInt("BurnTime");
	    this.cookTime = compound.getInt("CookTime");
	    this.cookTimeTotal = compound.getInt("CookTimeTotal");
	    this.recipesUsed = net.minecraftforge.common.ForgeHooks.getBurnTime(this.items.get(1), null);
	    this.direction = compound.getInt("direction");
		for(int i = 0; i < 6; ++i) {
			long l = compound.getLong("extr_" + i);
			if(l != 0) {
				this.extractorPositions[i] = BlockPos.of(l);
			}
		}
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("BurnTime", this.burnTime);
		compound.putInt("CookTime", this.cookTime);
		compound.putInt("CookTimeTotal", this.cookTimeTotal);
	    ItemStackHelper.saveAllItems(compound, this.items);
	    compound.putInt("direction", this.direction);
	    //save extractor positions
		for(int i = 0; i < 6; ++i) {
			if(this.extractorPositions[i] != null) {
				compound.putLong("extr_" + i, this.extractorPositions[i].asLong());
			}
		}
		return compound;
	}
	
	//----------------------------------------------------Inventory----------------------------------------------------------------------------------
	@Override
	public void clearContent() {
		this.items.clear();
	}
	@Override
	public int getContainerSize() {
		return this.items.size();
	}
	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	@Override
	public ItemStack getItem(int index) {
		return this.items.get(index);
	}
	@Override
	public ItemStack removeItem(int index, int count) {
		return ItemStackHelper.removeItem(this.items, index, count);
	}
	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ItemStackHelper.takeItem(this.items, index);
	}
	@Override
	public void setItem(int index, ItemStack stack) {
		ItemStack itemstack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.tagMatches(stack, itemstack);
		this.items.set(index, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		if (index == 0 && !flag) {
			this.cookTimeTotal = this.getCookTime();
			this.cookTime = 0;
			this.setChanged();
		}
	}
	@Override
	public boolean stillValid(PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}
	//-----------------------------------------------------Synchronization-----------------------------------------------------
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.save(nbt);

		return new SUpdateTileEntityPacket(this.getBlockPos(), 1, nbt);
	}
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(this.level.getBlockState(this.getBlockPos()), pkt.getTag());
	}
	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.load(state, tag);
	}
	//Recipe
	@Override
	public void setRecipeUsed(IRecipe<?> recipe) {}
	@Override
	public IRecipe<?> getRecipeUsed() {
		return null;
	}
	
	public boolean addExtractor(BlockPos pos) {
		for(int i = 0; i < 6; ++i) {
			if(this.extractorPositions[i] == null) {
				this.extractorPositions[i] = pos;
				this.setChanged();
				return true;
			}
		}
		return false;
	}
	public void removeExtractor(BlockPos pos) {
		for(int i = 0; i < 6; ++i) {
			if(this.extractorPositions[i] != null) {
				if(this.extractorPositions[i].equals(pos)) {
					this.extractorPositions[i] = null;
					this.setChanged();
					return;
				}
			}
		}
	}
}
