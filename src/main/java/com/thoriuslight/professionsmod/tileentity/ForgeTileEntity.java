package com.thoriuslight.professionsmod.tileentity;

import com.thoriuslight.professionsmod.block.ForgeBlock;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.item.IHeatable;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class ForgeTileEntity extends AbstractHeaterTileEntity{
	private int timer;
	private ItemStack stack;
	public ForgeTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 50);
		timer = 1000;
		stack = ItemStack.EMPTY;
	}
	public ForgeTileEntity() {
		this(ModTileEntityTypes.FORGE.get());
	}
	@Override
	public void tick() {
		//Fuel & annealing
		if(this.fuel > 0) {
			--this.fuel;
			if(this.hasItem()) {
				if(this.timer > 0 && this.temp > 64300) {
					this.timer -= (this.temp - 63300)/1000;
					if(this.timer <= 0) {
						((IHeatable)stack.getItem()).heat(stack);
					}
				}
			}
			if(this.fuel == 0) {
				ForgeBlock.extinguish(this.getBlockState(), this.level, this.worldPosition);
				this.timer = 1000;
				this.setMaxTemperature(30000);
			}
		} 
		//Temperature mechanics
		if(this.hasItem()) {
			this.temp = this.temp + (this.getMaxTemperature() - this.temp + 499 * Integer.signum(this.getMaxTemperature() - this.temp))/500;
		}
	}
	public boolean hasItem() {
		return !this.stack.isEmpty();
	}
	public void setItem(ItemStack stack) {
		this.stack = stack;
		this.timer = 1000;
		this.temp = 30000;
		this.setChanged();
	}
	public ItemStack getItem() {
		return this.stack;
	}
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put("item", this.stack.save(new CompoundNBT()));
		compound.putInt("timer", this.timer);
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if(compound.contains("item")) {
			this.stack = (ItemStack.of(compound.getCompound("item")));
		}
		if(compound.contains("timer")) {
			this.timer = compound.getInt("timer");
		}
	}
}
