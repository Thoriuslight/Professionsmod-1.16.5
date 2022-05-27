package com.thoriuslight.professionsmod.tileentity;

import java.util.Random;

import com.thoriuslight.professionsmod.block.CastingBasinBlock;
import com.thoriuslight.professionsmod.block.CrucibleBlock;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.item.IMeltable;
import com.thoriuslight.professionsmod.util.MetalItem;
import com.thoriuslight.professionsmod.util.MetalItems;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CrucibleTileEntity extends AbstractHeaterTileEntity{
	//consts
	public static final int CAPACITY = 54;
	
	private ItemStack stack;
	private int fluid_amount; //in nuggets
	private ModItemTier fluid_type;
	private MetalItem metal;
	private int smeltingSlowness;
	private int oxygen;
	private int castCooldown;
	private int last_fluid_amount;
	private long castPos;
	

	//Rendering & sound
	protected int animationTicks = 0;
	protected static Random RANDOM = new Random();
	private int direction;
	private int blockDirection;
	
	public CrucibleTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 55);
		stack = ItemStack.EMPTY;
		fluid_amount = 0;
		smeltingSlowness = 1;
		oxygen = 0;
		castCooldown = 0;
		castPos = 0;
		last_fluid_amount = -1;
		direction = 0;
	}
	public CrucibleTileEntity() {
		this(ModTileEntityTypes.CRUCIBLE.get());
	}
	@Override
	public void tick() {
		++this.animationTicks;
		if(animationTicks > 74) {
			animationTicks = 0;
		}
		if(castCooldown > 0) {
			--castCooldown;
			if(castCooldown == 0) {
				this.last_fluid_amount = -1;
				BlockPos blockPos = BlockPos.of(castPos);
				BlockState state = this.level.getBlockState(blockPos);
				if(state.getBlock() instanceof CastingBasinBlock) {
					CastingBasinBlock block = (CastingBasinBlock)state.getBlock();
					if(this.fluid_type != null)
						block.cast(this.level, blockPos, state, this.fluid_type);
				}
			}
		}
		//Fuel & melting
		if(this.fuel > 0) {
			--this.fuel;
			if(this.fuel == 0) {
				CrucibleBlock.extinguish(this.getBlockState(), this.level, this.worldPosition);
				this.setMaxTemperature(30000);
			}
		} 
		if(oxygen > 0) {
			--this.oxygen;
		}
		//Temperature mechanics
		if(this.hasItem()) {
			this.temp = this.temp + (this.getMaxTemperature() - this.temp + (smeltingSlowness - 1) * Integer.signum(this.getMaxTemperature() - this.temp))/smeltingSlowness;
			if(metal != null) {
				if(this.temp >= this.metal.getMeltingPoint()) {
					if((this.fluid_amount + this.metal.getNuggets() * this.stack.getCount()) <= CAPACITY) {
	            		level.playSound((PlayerEntity)null, worldPosition, SoundEvents.BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
						this.addFluid(this.metal.getNuggets() * this.stack.getCount());
						this.setItem(ItemStack.EMPTY, false);
					}
				}
			}
		}
	}
	//Heat
	@Override
	public int getMaxTemperature() {
		if(this.oxygen > 0 && this.fuel > 0)
			return super.getMaxTemperature() + 20000;
		else return super.getMaxTemperature();
	}
	//Inventory
	public boolean hasItem() {
		return !this.stack.isEmpty();
	}
	public boolean isItemValid(ItemStack stack, boolean isMeltableType) {
		if(this.fluid_type != null && this.fluid_amount != 0) {
			if(isMeltableType) {
				return ((IMeltable)stack.getItem()).getType(stack) == this.fluid_type;
			}
			return MetalItems.getMetal(stack.getItem()).getMaterial() == this.fluid_type;
		}
		else return true;
	}
	public boolean checkFluid(ModItemTier material) {
		return material == this.fluid_type && this.fluid_type != null && this.fluid_amount != 0;
	}
	public void setItem(ItemStack stack, boolean isMeltableType) {
		this.stack = stack;
		this.temp = 30000;

		if(!stack.isEmpty()) {
			if(isMeltableType) {
				IMeltable item = (IMeltable)stack.getItem();
				this.metal = (new MetalItem(item.getNuggetAmount(stack), 2, item.getType(stack)));
			}
			else {
				this.metal = MetalItems.getMetal(stack.getItem());
			}
			this.smeltingSlowness = 50 + 20 * (int) Math.sqrt((this.metal.getNuggets() * this.stack.getCount() * (10 + this.metal.getInpurity()*this.metal.getInpurity()/5)));
		}
		this.setChanged();
	}
	public ItemStack getItem() {
		return this.stack;
	}
	//Fluid
	protected void addFluid(int amount) {
		this.fluid_amount = Math.min(this.fluid_amount + amount, CAPACITY);
		this.fluid_type = metal.getMaterial();
		this.setChanged();
	}
	public int getFluid() {
		return this.fluid_amount;
	}
	public void addArsenic() {
		this.fluid_type = ModItemTier.ARSENICAL_BRONZE;
		this.setChanged();
	}
	public void cast(BlockPos pos, BlockState state) {
		if(castCooldown == 0) {
			int nuggets = CastingBasinBlock.getNuggetAmount(this.level, pos, state, this.fluid_type);
			if(nuggets <= this.fluid_amount && nuggets != 0) {
				if(((CastingBasinBlock)state.getBlock()).lock(state, level, pos, this.worldPosition.asLong())) {
					this.last_fluid_amount = this.fluid_amount;
					castCooldown = 60;
					this.fluid_amount -= nuggets;
					castPos = pos.asLong();
	        		level.playSound((PlayerEntity)null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
	        		this.setChanged();
				}
			}
		}
	}
	public int getCooldown() {
	      return this.castCooldown;
	}
	//Air
	public void addOxygen(int amount) {
		this.oxygen = Math.min(amount + oxygen, 400);
	}
	public int getOxygen() {
		return this.oxygen;
	}
	//Rendering
	@OnlyIn(Dist.CLIENT)
	public int getAnimationFrame() {
	      return this.animationTicks;
	}
	@OnlyIn(Dist.CLIENT)
	public float getDynamicFluidAmount(float partialTicks) {
		if(this.last_fluid_amount != -1) {
			float f = Math.min(1.f, (this.castCooldown + partialTicks)/60.f);
			return (this.fluid_amount*(1.f - f) + this.last_fluid_amount*f);
		}
		return this.fluid_amount;
	}
	@OnlyIn(Dist.CLIENT)
	public float getDirection() {
		return this.direction * -90.f;
	}
	@OnlyIn(Dist.CLIENT)
	public boolean hasFuel() {
		return this.fuel > 0;
	}
	@OnlyIn(Dist.CLIENT)
	public float getBlockDirection() {
		return this.blockDirection * -90.f;
	}
	//Syncing
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put("item", this.stack.save(new CompoundNBT()));
		compound.putInt("fluid", this.fluid_amount);
		if(this.fluid_type != null) {
			compound.putInt("fluid_type", this.fluid_type.ordinal());
		}
		compound.putInt("air", this.oxygen);
		compound.putInt("cooldown", this.castCooldown);
		compound.putInt("prevfluid", this.last_fluid_amount);
		compound.putLong("castpos", this.castPos);
		return compound;
	}
	
	@Override
	public void load(BlockState blockState, CompoundNBT compound) {
		super.load(blockState, compound);
		if(compound.contains("item")) {
			this.stack = (ItemStack.of(compound.getCompound("item")));
			Item item = this.stack.getItem();
			if(item instanceof IMeltable) {
				this.metal = (new MetalItem(((IMeltable)item).getNuggetAmount(stack), 2, ((IMeltable)item).getType(stack)));
			}
			else this.metal = MetalItems.getMetal(item);
		}
		if(compound.contains("fluid")) {
			this.fluid_amount = compound.getInt("fluid");
		}
		if(compound.contains("fluid_type")) {
			this.fluid_type = ModItemTier.values()[compound.getInt("fluid_type")];
		}
		if(this.metal != null) {
			this.smeltingSlowness = 50 + 20 * (int) Math.sqrt((this.metal.getNuggets() * this.stack.getCount() * (10 + 2 * this.metal.getInpurity())));
		}
		if(compound.contains("air")) {
			this.oxygen = compound.getInt("air");
		}
		if(compound.contains("cooldown")) {
			this.castCooldown = compound.getInt("cooldown");
		}
		if(compound.contains("prevfluid")) {
			this.last_fluid_amount = compound.getInt("prevfluid");
		}
		if(compound.contains("castpos")) {
			this.castPos = compound.getLong("castpos");
		}
		if(this.hasLevel()) {
			BlockState state = this.level.getBlockState(this.worldPosition);
			if(state != null){
				if(state.getBlock() instanceof CrucibleBlock) {
					Direction dir = CrucibleBlock.getDrainFacing(state);
					switch(dir) {
					case NORTH:
						direction = 0;
						break;
					case EAST:
						direction = 1;
						break;
					case SOUTH:
						direction = 2;
						break;
					case WEST:
						direction = 3;
						break;
					default:
						direction = 0;
						break;
					}
					dir = CrucibleBlock.getFacing(state);
					switch(dir) {
					case WEST:
						blockDirection = 0;
						break;
					case NORTH:
						blockDirection = 1;
						break;
					case EAST:
						blockDirection = 2;
						break;
					case SOUTH:
						blockDirection = 3;
						break;
					default:
						blockDirection = 0;
						break;
					}
				}
			}
			
		}
	}
}
