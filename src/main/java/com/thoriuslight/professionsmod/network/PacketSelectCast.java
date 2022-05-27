package com.thoriuslight.professionsmod.network;

import java.util.function.Supplier;

import com.thoriuslight.professionsmod.block.CastingBasinBlock;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketSelectCast {
	private final Tool cast;
	private final BlockPos pos;
	public PacketSelectCast(Tool tool, BlockPos pos){
		cast = tool;
		this.pos = pos;
	}
	public static void encode(PacketSelectCast msg, PacketBuffer buf) {
		buf.writeInt(msg.cast.ordinal());
		buf.writeBlockPos(msg.pos);
	}
	public static PacketSelectCast decode(PacketBuffer buf) {
		Tool t = Tool.values()[buf.readInt()];
		BlockPos p = buf.readBlockPos();
		return new PacketSelectCast(t, p);
	}
	public static void handle(PacketSelectCast msg, Supplier<NetworkEvent.Context> supplier) {
		Context ctx = supplier.get();
		ServerPlayerEntity player = ctx.getSender();
		if(player != null) {
			ctx.enqueueWork(() -> {
				CastingBasinBlock.setTool(msg.cast, player.level, msg.pos);
			});
		}
	}
}
