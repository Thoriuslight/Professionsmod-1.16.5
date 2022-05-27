package com.thoriuslight.professionsmod.tileentity;

import com.thoriuslight.professionsmod.init.FluidInit;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.inventory.container.ExtractorContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class ExtractorTileEntity extends TileEntity  implements INamedContainerProvider, IInventory, IFluidTank{
	private static ITextComponent customName = new TranslationTextComponent("container.professionsmod.extractor");
	protected NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);
	protected FluidStack fluid = FluidStack.EMPTY;
	public ExtractorTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		//temp 
		this.fluid = new FluidStack(FluidInit.CREOSOTE_FLUID.get(), 3000);
		//
	}
	public ExtractorTileEntity() {
		this(ModTileEntityTypes.EXTRACTOR.get());
	}
	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		return new ExtractorContainer(p_createMenu_1_, p_createMenu_2_, this, this.extractorData);
	}
	@Override
	public ITextComponent getDisplayName() {
		return customName;
	}
	//-----------------------------------------------------Data management-----------------------------------------------------
	@Override
	public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
		super.load(p_230337_1_, p_230337_2_);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(p_230337_2_, this.items);
		this.fluid = FluidStack.loadFluidStackFromNBT(p_230337_2_);
	}	
	@Override
	public CompoundNBT save(CompoundNBT p_189515_1_) {
		super.save(p_189515_1_);
		ItemStackHelper.saveAllItems(p_189515_1_, this.items);
		this.fluid.writeToNBT(p_189515_1_);
		return p_189515_1_;
	}
	protected final IIntArray extractorData = new IIntArray() {
		@Override
		public int get(int index) {
			switch(index) {
			case 0:
	            return ExtractorTileEntity.this.getFluidAmount();
	         default:
	            return 0;
			}
		}
		@Override
		public void set(int index, int value) {
			switch(index) {
			case 0:
				ExtractorTileEntity.this.fluid.setAmount(value);
	            break;
			}

		}
		@Override
		public int getCount() {
			return 1;
		}
	};
	//-----------------------------------------------------Inventory-----------------------------------------------------
	public boolean addItem(ItemStack stack) {
		for(int i = 0; i < 9; ++i){
			if(this.getItem(i).isEmpty()) {
				this.setItem(i, stack);
				return true;
			}
		}
		return false;
	}
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
		this.items.set(index, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		if(index == 9 || index == 10) {
			ItemStack input = this.items.get(9);
			int in = input.getCount();
			if(in > 0) {
				ItemStack output = this.items.get(10);
				int out = output.getCount();
				Item item;
				int toDrain;
				if(input.getItem().equals(Items.BUCKET)) { 
					item = ItemInit.CREOSOTE_BUCKET.get();
					toDrain = 1000;
				}
				else {
					item = ItemInit.CREOSOTE_GLASS.get();
					toDrain = 250;
				}
				if(output.getItem().equals(item) || output.isEmpty()) {
					if(this.fluid.getAmount() >= toDrain) {
						ItemStack filledItem = new ItemStack(item);
						int amount = Math.min(filledItem.getMaxStackSize() - out, in);
						amount = Math.min(amount, this.fluid.getAmount()/toDrain);
						this.drain(amount * toDrain, FluidAction.EXECUTE);
						input.shrink(amount);
						filledItem.setCount(out + amount);
						items.set(10, filledItem);
						this.setChanged();
					}
				}
				
			}
		}
		this.setChanged();
	}
	@Override
	public boolean stillValid(PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}
	//-----------------------------------------------------Fluid-----------------------------------------------------
	@Override
	public FluidStack getFluid() {
		return this.fluid;
	}
	@Override
	public int getFluidAmount() {
		return this.fluid.getAmount();
	}
	@Override
	public int getCapacity() {
		return 4000;
	}
	@Override
	public boolean isFluidValid(FluidStack stack) {
		return false;
	}
	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if(this.fluid.isEmpty() && action.execute()) {
			this.fluid = resource.copy();
			this.setChanged();
			return Math.max(this.fluid.getAmount() - this.getCapacity(), 0);
		}
		int stored = this.fluid.getAmount();
		int fill = Math.min(resource.getAmount(), this.getCapacity() - stored);
		if(action.execute()) {
			this.setChanged();
			this.fluid.setAmount(stored + fill);
		}
		return resource.getAmount() - fill;
	}
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		int level = this.fluid.getAmount();
		int drain = Math.min(maxDrain, level);
		if(action.execute()) {
			this.fluid.setAmount(level - drain);
		}
		if(drain != 0) return new FluidStack(this.fluid.getFluid(), drain);
		return FluidStack.EMPTY;
	}
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		return null;
	}
	//-----------------------------------------------------Rendering-----------------------------------------------------
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(worldPosition.offset(-1, 0, -1), worldPosition.offset(2, 2, 2));
	}
	//-----------------------------------------------------Synchronization-----------------------------------------------------
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getBlockPos(), 1, getUpdateTag());
	}
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(this.getBlockState(), pkt.getTag());
	}
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT compound = new CompoundNBT();
		ItemStackHelper.saveAllItems(compound, this.items);
		this.fluid.writeToNBT(compound);
		return this.save(compound);
	}
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(tag, this.items);
		this.fluid = FluidStack.loadFluidStackFromNBT(tag);
	}
}
