package com.thoriuslight.professionsmod.profession.capabilities;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityProfession  implements ICapabilitySerializable<CompoundNBT>{
	
    @CapabilityInject(IProfession.class)
    public static final Capability<IProfession> PROFESSION = null;
    private LazyOptional<IProfession> instance = LazyOptional.of(PROFESSION::getDefaultInstance);
    
    public static void register()
    {
        CapabilityManager.INSTANCE.register(IProfession.class, new ProfessionStorage(), Profession::new);
    }

    @Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return PROFESSION.orEmpty(cap, instance);
	}

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) PROFESSION.getStorage().writeNBT(PROFESSION, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
    	PROFESSION.getStorage().readNBT(PROFESSION, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }
}
