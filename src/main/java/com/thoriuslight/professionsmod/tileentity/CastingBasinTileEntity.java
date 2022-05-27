package com.thoriuslight.professionsmod.tileentity;

import com.thoriuslight.professionsmod.init.DataInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;

import com.thoriuslight.professionsmod.block.CastingBasinBlock;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class CastingBasinTileEntity extends TileEntity{
	private ModItemTier liquified_metal;
	private int liquidQuantiy;
	private Tool tool;
	private ItemStack storedItem;
	private long lockerPos;

	public CastingBasinTileEntity(final TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		liquified_metal = ModItemTier.COPPER;
		liquidQuantiy = 0;
		tool = Tool.NOTHING;
		storedItem = ItemStack.EMPTY;
		lockerPos = 0;
	}

	public CastingBasinTileEntity() {
		this(ModTileEntityTypes.CASTINGBASIN.get());
	}
	
	public boolean addMetal(ModItemTier metal) {
		if(liquidQuantiy >= tool.getNuggetAmount() && tool != Tool.NOTHING) {
			liquidQuantiy -= tool.getNuggetAmount();
			this.dropItem(new ItemStack(DataInit.getToolHead(tool, liquified_metal)));
			return false;
		} else if(liquidQuantiy == 0) {
			this.liquified_metal = metal;			
		} 
		if(metal == this.liquified_metal){
			liquidQuantiy += 8;
			if(liquidQuantiy >= tool.getNuggetAmount()  && tool != Tool.NOTHING) {
				liquidQuantiy -= tool.getNuggetAmount();
				this.dropItem(new ItemStack(DataInit.getToolHead(tool, liquified_metal)));
			}
		} else {
			return false;
		}
		this.setChanged();
		return true;
	}
	
	public boolean canAddMetal(ModItemTier metal) {
		if(liquidQuantiy >= tool.getNuggetAmount() && tool != Tool.NOTHING) {
			return false;
		}
		if(liquidQuantiy == 0 || this.liquified_metal.equals(metal)) {
			return true;
		}
		return false;
	}
	public void cast(ModItemTier metal) {
		liquidQuantiy = 0;
		this.storedItem = new ItemStack(DataInit.getToolHead(tool, metal));
		this.setChanged();
	}
	public boolean hasItem() {
		return !this.storedItem.isEmpty();
	}
	public ItemStack getItem() {
		return this.storedItem;
	}
	public void setItem(ItemStack stack) {
		this.storedItem = stack;
		this.setChanged();
	}
	public void setTool(Tool tool) {
		this.tool = tool;
	}
	public int getRequiredNuggets() {
		return this.tool.getNuggetAmount() - this.liquidQuantiy;
	}
	
	public void dropItem(ItemStack item) {
		((CastingBasinBlock)this.getBlockState().getBlock()).dropItem(this.getLevel(), this.worldPosition, item);
		this.liquidQuantiy = 0;
		this.setChanged();
	}
	public void setLocker(long pos) {
		this.lockerPos = pos;
		this.setChanged();
	}
	public boolean isLocked() {
		TileEntity tile = this.getLevel().getBlockEntity(BlockPos.of(lockerPos));
		if(tile instanceof CrucibleTileEntity) {
			return ((CrucibleTileEntity)tile).getCooldown() != 0;
		}
		return false;
	}
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putString("metal", this.liquified_metal.toString());
		compound.putInt("metalAmount", this.liquidQuantiy);
		compound.putString("tool", this.tool.toString());
		compound.putLong("locker", this.lockerPos);
		compound.put("item", this.storedItem.save(new CompoundNBT()));
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if(compound.contains("metal")) {
			this.liquified_metal = ModItemTier.valueOf(compound.getString("metal"));
		}
		if(compound.contains("metalAmount")) {
			this.liquidQuantiy = compound.getInt("metalAmount");
		}
		if(compound.contains("tool")) {
			this.tool = Tool.valueOf(compound.getString("tool"));
		}
		if(compound.contains("locker")) {
			this.lockerPos = compound.getLong("locker");
		}
		if(compound.contains("item")) {
			this.storedItem = (ItemStack.of(compound.getCompound("item")));
		}
	}
	//Sync
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
}
