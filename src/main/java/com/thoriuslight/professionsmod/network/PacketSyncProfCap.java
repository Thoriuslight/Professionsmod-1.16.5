package com.thoriuslight.professionsmod.network;

import java.util.function.Supplier;

import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.Profession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketSyncProfCap {
	private final profession prof;
	private final int skill;
	private final int skillTree;
	public PacketSyncProfCap(Profession prof){
		this.prof = prof.getProfession();
		this.skill = prof.getSkill();
		this.skillTree = prof.getSkillTree();
	}
	public PacketSyncProfCap(profession prof, int skill){
		this.prof = prof;
		this.skill = skill;
		this.skillTree = 0;
	}
	public PacketSyncProfCap(profession prof, int skill, int skillTree){
		this.prof = prof;
		this.skill = skill;
		this.skillTree = skillTree;
	}
	public static void encode(PacketSyncProfCap msg, PacketBuffer buf) {
		buf.writeInt(msg.prof.ordinal());
		buf.writeInt(msg.skill);
		if(msg.prof != profession.UNDEFINED) {
			buf.writeInt(msg.skillTree);
		}
	}
	public static PacketSyncProfCap decode(PacketBuffer buf) {
		profession p = profession.values()[buf.readInt()];
		int s = buf.readInt();
		if(p != profession.UNDEFINED) {
			int sT = buf.readInt();
			return new PacketSyncProfCap(p, s, sT);
		} else {
			return new PacketSyncProfCap(p, s);
		}
	}
	public static void handle(PacketSyncProfCap msg, Supplier<NetworkEvent.Context> supplier) {
		Context ctx = supplier.get();
		ServerPlayerEntity player = ctx.getSender();
		if(player != null) {
			ctx.enqueueWork(() -> {
				player.getCapability(CapabilityProfession.PROFESSION, null).ifPresent(iProfession -> {
	                iProfession.setProfession(msg.prof);
					if(msg.prof != profession.UNDEFINED)
						iProfession.setSkillTree(msg.skillTree);
	            });
			});
		} else if (ctx.getDirection().getReceptionSide().isClient()){
			ctx.enqueueWork(() -> {
				ClientPlayerEntity thePlayer = Minecraft.getInstance().player;
				thePlayer.getCapability(CapabilityProfession.PROFESSION, null).ifPresent(iProfession -> {
					if(msg.prof != profession.UNDEFINED) {
						iProfession.setProfession(msg.prof);
						iProfession.setSkillTree(msg.skillTree);
					}
					iProfession.setSkill(msg.skill);
				});
			});
		}
		ctx.setPacketHandled(true);
	}
}
