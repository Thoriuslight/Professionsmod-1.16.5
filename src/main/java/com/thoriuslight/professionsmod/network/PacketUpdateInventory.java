package com.thoriuslight.professionsmod.network;

import java.util.function.Supplier;

import com.thoriuslight.professionsmod.tileentity.ExtractorTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketUpdateInventory {
	private final BlockPos pos;
	private final ItemStack input;
	public PacketUpdateInventory(BlockPos pos, ItemStack input){
		this.pos = pos;
		this.input = input;
	}
	public static void encode(PacketUpdateInventory msg, PacketBuffer buf) {
		buf.writeBlockPos(msg.pos);
		buf.writeItemStack(msg.input, false);
	}
	public static PacketUpdateInventory decode(PacketBuffer buf) {
		BlockPos p = buf.readBlockPos();
		ItemStack i = buf.readItem();
		return new PacketUpdateInventory(p, i);
	}
	public static void handle(PacketUpdateInventory msg, Supplier<NetworkEvent.Context> supplier) {
		Context ctx = supplier.get();
		if (ctx.getDirection().getReceptionSide().isClient()){
			ctx.enqueueWork(() -> {
				ClientPlayerEntity player = Minecraft.getInstance().player;
				TileEntity tile = player.level.getBlockEntity(msg.pos);
				if(tile instanceof ExtractorTileEntity) {
					((ExtractorTileEntity)tile).setItem(9, msg.input);
				}
			});
		}
		ctx.setPacketHandled(true);
	}
}
