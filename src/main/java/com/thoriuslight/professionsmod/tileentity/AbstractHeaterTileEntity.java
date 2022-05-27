package com.thoriuslight.professionsmod.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

public abstract class AbstractHeaterTileEntity extends TileEntity implements ITickableTileEntity{
	protected final int isolation;
	protected int fuel;
	protected float fuelRatio;
	private int maxTemp;
	protected int temp;
	
	public AbstractHeaterTileEntity(TileEntityType<?> tileEntityTypeIn, int isolation) {
		super(tileEntityTypeIn);
		this.isolation = isolation;
		fuel = 0;
		fuelRatio = 0.f;
		//Temperature in Kelvin *100
		maxTemp = 30000;
		temp = 30000;
	}
	//fuel
	public boolean canAddFuel() {
		if(fuel==0) return true;
		return (this.fuel/this.fuelRatio) < 80;
	}
	public void addFuel(int fuel) {
		int newFuel = (int) (Math.sqrt(fuel) * 40);
		if(this.fuel == 0) {
			this.fuelRatio = newFuel/10.f;
		} 
		else {
			this.fuelRatio = (this.fuel + newFuel)/((this.fuel/this.fuelRatio)+10.f);
		}
		this.fuel += newFuel;
		maxTemp = 30000 + 140 * this.isolation * (int) Math.sqrt(this.fuelRatio);
		this.setChanged();
	}
	//heat
	public int getTemperature() {
		return this.temp/100;
	}
	public int getMaxTemperature() {
		return this.maxTemp;
	}
	public void setMaxTemperature(int temp) {
		this.maxTemp = temp;
	}
	//Syncing
	//----------------------------------------------------------------------------------
	@Override
	public void setChanged() {
		super.setChanged();
		this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(),
				Constants.BlockFlags.BLOCK_UPDATE);
	}
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("fuel", this.fuel);
		compound.putFloat("fuelratio", this.fuelRatio);
		compound.putInt("temp", this.temp);
		compound.putInt("maxtemp", this.maxTemp);
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if(compound.contains("fuel")) {
			this.fuel = compound.getInt("fuel");
		}
		if(compound.contains("fuelratio")) {
			this.fuelRatio = compound.getFloat("fuelratio");
		}
		if(compound.contains("temp")) {
			this.temp = compound.getInt("temp");
		}
		if(compound.contains("maxtemp")) {
			this.maxTemp = compound.getInt("maxtemp");
		}
	}
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
