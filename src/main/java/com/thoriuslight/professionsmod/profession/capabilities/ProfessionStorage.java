package com.thoriuslight.professionsmod.profession.capabilities;

import javax.annotation.Nullable;

import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ProfessionStorage implements IStorage<IProfession>{
    @Nullable
	@Override
	public INBT writeNBT(Capability<IProfession> capability, IProfession instance, Direction side) {
		CompoundNBT data = new CompoundNBT();
		data.putInt("skill", instance.getSkill());
		data.putInt("prof", instance.getProfession().ordinal());
		data.putInt("skillTree", instance.getSkillTree());
		return data;
	}

	@Override
	public void readNBT(Capability<IProfession> capability, IProfession instance, Direction side, INBT nbt) {
		CompoundNBT data = (CompoundNBT)nbt;
		if(data.contains("prof")) {
			instance.setProfession(profession.values()[data.getInt("prof")]);
		}
		if(data.contains("skill")) {
			instance.setSkill(data.getInt("skill"));;
		}
		if(data.contains("skillTree")) {
			instance.setSkillTree(data.getInt("skillTree"));;
		}
	}

}
