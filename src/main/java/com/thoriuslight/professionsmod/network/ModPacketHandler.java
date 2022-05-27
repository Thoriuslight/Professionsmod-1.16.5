package com.thoriuslight.professionsmod.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	private static int id = 0;
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("professionsmod", "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	public static void registerPackets(){
		INSTANCE.registerMessage(getId(), PacketSelectCast.class, PacketSelectCast::encode, PacketSelectCast::decode, PacketSelectCast::handle);
		INSTANCE.registerMessage(getId(), PacketSyncProfCap.class, PacketSyncProfCap::encode, PacketSyncProfCap::decode, PacketSyncProfCap::handle);
		INSTANCE.registerMessage(getId(), PacketUpdateInventory.class, PacketUpdateInventory::encode, PacketUpdateInventory::decode, PacketUpdateInventory::handle);
	}
	private static int getId() {
		return id++;
	}
}
