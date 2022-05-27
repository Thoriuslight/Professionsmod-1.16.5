package com.thoriuslight.professionsmod.profession.capabilities;

import java.util.List;

import com.thoriuslight.professionsmod.network.ModPacketHandler;
import com.thoriuslight.professionsmod.network.PacketSyncProfCap;
import com.thoriuslight.professionsmod.profession.P_Consts;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.profession.skill.Skill;
import com.thoriuslight.professionsmod.profession.skill.SkillContainer;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class Profession implements IProfession{
	
	private profession Prof = profession.NOTHING;
	private int skillPoints = 0;
	private SkillContainer skillTree = new SkillContainer();
	private int subtractedSkillPoints = 0;
	
	@Override
	public profession getProfession() {
		return Prof;
	}

	@Override
	public int getSkill() {
		return skillPoints;
	}

	@Override
	public void setProfession(profession Prof) {
		this.Prof = Prof;
	}

	@Override
	public void addSkill(int x, PlayerEntity player) {
		this.skillPoints = Math.min(this.skillPoints + x, P_Consts.MAX_SKILL);
		if(player instanceof ServerPlayerEntity)
			ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> {return (ServerPlayerEntity) player;}), new PacketSyncProfCap(profession.UNDEFINED, this.skillPoints));
	}

	@Override
	public void setSkill(int x) {
		this.skillPoints = x;
	}

	@Override
	public boolean getSkillTalent(int id) {
		return skillTree.getSkill(id);	
	}

	@Override
	public boolean setSkillTalent(int id, boolean state, PlayerEntity player) {
		boolean lastState = skillTree.getSkill(id);
		if(state != lastState) {
			if(this.Prof.getSkillList() != null) {
				int reqPoints = this.Prof.getSkillList().getSkills().get(id).getRequiredPoints();
				if(reqPoints <= this.getSkillPoints()) {
					//Check skill requirements
					List<Skill> skills = this.Prof.getSkillList().getSkills().get(id).getRequirement();
					if(skills.size() != 0) {
						for(int i=0; i < skills.size(); ++i) {
							if(!getSkillTalent(skills.get(i).getId())) {
								return false;
							}
						}
					}
					//Update professions state
					skillTree.setSkill(id, state);
					this.subtractedSkillPoints += reqPoints;
					if(player instanceof ClientPlayerEntity)
						ModPacketHandler.INSTANCE.sendToServer(new PacketSyncProfCap(this.Prof, this.skillPoints, this.skillTree.getSkillList()));
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getSkillTree() {
		return skillTree.getSkillList();
	}

	@Override
	public void setSkillTree(int x) {
		if(Prof.getSkillList() != null) {
			skillTree.setSkillList(x);
			this.subtractedSkillPoints = 0;
			int i = 0;
			while(x != 0) {
				if(1 == (x & 1)) {
					this.subtractedSkillPoints += Prof.getSkillList().getSkills().get(i).getRequiredPoints();
				}
				++i;
				x = x >> 1;
			}
		}
		else {
			skillTree.setSkillList(0);
			this.subtractedSkillPoints = 0;
		}
	}

	@Override
	public int getSkillPoints() {
		return skillPoints / P_Consts.SKILL_PER_POINT - this.subtractedSkillPoints;
	}

}
