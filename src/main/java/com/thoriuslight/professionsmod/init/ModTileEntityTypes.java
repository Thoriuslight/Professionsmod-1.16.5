package com.thoriuslight.professionsmod.init;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.tileentity.CastingBasinTileEntity;
import com.thoriuslight.professionsmod.tileentity.CrucibleTileEntity;
import com.thoriuslight.professionsmod.tileentity.ForgeTileEntity;
import com.thoriuslight.professionsmod.tileentity.OvenControllerTileEntity;
import com.thoriuslight.professionsmod.tileentity.StoneAnvilTileEntity;
import com.thoriuslight.professionsmod.tileentity.ExtractorTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityTypes {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ProfessionsMod.MODID);
	//Smithing
	public static final RegistryObject<TileEntityType<StoneAnvilTileEntity>> STONEANVIL = TILE_ENTITY_TYPES.register("stoneanvil", () -> TileEntityType.Builder.of(StoneAnvilTileEntity::new, BlockInit.STONEANVIL_BLOCK.get()).build(null));
	public static final RegistryObject<TileEntityType<CastingBasinTileEntity>> CASTINGBASIN = TILE_ENTITY_TYPES.register("castingbasin", () -> TileEntityType.Builder.of(CastingBasinTileEntity::new, BlockInit.CASTINGBASIN_BLOCK.get(), BlockInit.STONECASTINGBASIN_BLOCK.get()).build(null));
	public static final RegistryObject<TileEntityType<ForgeTileEntity>> FORGE = TILE_ENTITY_TYPES.register("forge", () -> TileEntityType.Builder.of(ForgeTileEntity::new, BlockInit.FORGE_BLOCK.get()).build(null));
	public static final RegistryObject<TileEntityType<CrucibleTileEntity>> CRUCIBLE = TILE_ENTITY_TYPES.register("crucible", () -> TileEntityType.Builder.of(CrucibleTileEntity::new, BlockInit.CRUCIBLE_BLOCK.get()).build(null));
	//Engineering
	//public static final RegistryObject<TileEntityType<WoodenHopperTileEntity>> WOODEN_HOPPER = TILE_ENTITY_TYPES.register("wooden_hopper", () -> TileEntityType.Builder.of(WoodenHopperTileEntity::new, BlockInit.WOODENHOPPER_BLOCK.get()).build(null));
	//Alchemy
	public static final RegistryObject<TileEntityType<OvenControllerTileEntity>> OVEN_CONTROLLER = TILE_ENTITY_TYPES.register("oven_controller", () -> TileEntityType.Builder.of(OvenControllerTileEntity::new, BlockInit.OVEN_CONTROLLER_BLOCK.get()).build(null));
	public static final RegistryObject<TileEntityType<ExtractorTileEntity>> EXTRACTOR = TILE_ENTITY_TYPES.register("extractor", () -> TileEntityType.Builder.of(ExtractorTileEntity::new, BlockInit.EXTRACTOR_CONTROLLER_BLOCK.get()).build(null));
}
