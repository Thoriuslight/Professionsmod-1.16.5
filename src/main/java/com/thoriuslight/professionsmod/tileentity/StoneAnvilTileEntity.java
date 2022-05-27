package com.thoriuslight.professionsmod.tileentity;

import com.thoriuslight.professionsmod.block.StoneAnvilBlock;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.item.IForgeable;
import com.thoriuslight.professionsmod.item.crafting.ICrushingRecipe;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

public class StoneAnvilTileEntity extends TileEntity{
	private ItemStack item;
	private ItemStack output;
	private int resilience;
	//------------------------Constructors---------------------------------------------
	public StoneAnvilTileEntity(final TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		item = ItemStack.EMPTY;
		output = ItemStack.EMPTY;
		resilience = 0;
	}
	public StoneAnvilTileEntity() {
		this(ModTileEntityTypes.STONEANVIL.get());
	}
	//------------------------Add Item with Recipe---------------------------------------------
	public void addItem(ItemStack item) {
		item.setCount(1);
		this.item = item;
		this.resilience = ((IForgeable)item.getItem()).resilience(item);
		this.setChanged();
	}
	public void addItem(ItemStack item, ICrushingRecipe recipe) {
		this.item = item;
		this.output = recipe.getResultItem().copy();
		this.resilience = recipe.getHardness();
		this.setChanged();
	}
	//------------------------Hammering---------------------------------------------

	public boolean hammer(int power) {
		//--------------Class defined Items--------------
		if(item.getItem() instanceof IForgeable) {
			IForgeable itemForged = (IForgeable)item.getItem();
			boolean skillUp = itemForged.hammer(item);
			System.out.println(resilience);
			if(resilience > -1) {
				resilience = Math.max(resilience - power, 0);
			} else {
				if(!itemForged.forged(item).isEmpty()) {
					if(itemForged.dropItem()) {
						((StoneAnvilBlock)this.getBlockState().getBlock()).dropItem(this.getLevel(), this.worldPosition, itemForged.forged(item));
						this.setItem(ItemStack.EMPTY);
					}else {
						if(itemForged.forged(item).getItem() instanceof IForgeable) {
							this.addItem(itemForged.forged(item));
						}else {
							this.setItem(ItemStack.EMPTY);
						}
					}
				}
			}
			return skillUp;
		}
		//--------------Recipe defined Items--------------
		else {
			if(output.isEmpty()) {
				return false;
			}
			System.out.println(resilience);
			resilience = resilience - power;
			if(resilience <= 0) {
				((StoneAnvilBlock)this.getBlockState().getBlock()).dropItem(this.getLevel(), this.worldPosition, this.output.copy());
				this.setItem(ItemStack.EMPTY);
				this.output = ItemStack.EMPTY;
			}
			return true;
		}
	}
	//------------------------Item management---------------------------------------------
	public void dropItem() {
		((StoneAnvilBlock)this.getBlockState().getBlock()).dropItem(this.getLevel(), this.worldPosition, item);
		this.item = ItemStack.EMPTY;
		this.setChanged();
	}
	public ItemStack getItem() {
		return item;
	}
	private void setItem(ItemStack stack) {
		this.item = stack;
		this.setChanged();
	}
	public boolean isfull() {
		return !item.isEmpty();
	}
	//------------------------Data management---------------------------------------------
	@Override
	public void setChanged() {
		super.setChanged();
		this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(),
				Constants.BlockFlags.BLOCK_UPDATE);
	}
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put("item", this.item.save(new CompoundNBT()));
		compound.put("output", this.output.save(new CompoundNBT()));
		compound.putInt("strikes", this.resilience);
		return compound;
	}
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if(compound.contains("item")) {
			this.item = (ItemStack.of(compound.getCompound("item")));
		}
		if(compound.contains("output")) {
			this.output = (ItemStack.of(compound.getCompound("output")));
		}
		if(compound.contains("strikes")) {
			this.resilience = compound.getInt("strikes");
		}
	}
	//------------------------Synchronization---------------------------------------------
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.save(nbt);

		return new SUpdateTileEntityPacket(this.getBlockPos(), 1, nbt);
	}
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(this.level.getBlockState(this.worldPosition), pkt.getTag());
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
