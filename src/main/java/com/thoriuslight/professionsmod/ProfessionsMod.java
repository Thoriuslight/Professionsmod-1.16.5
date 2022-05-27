package com.thoriuslight.professionsmod;

import com.thoriuslight.professionsmod.command.CommandProfession;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.DataInit;
import com.thoriuslight.professionsmod.init.FluidInit;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ModContainerTypes;
import com.thoriuslight.professionsmod.init.ModLootConditionTypes;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.init.RecipeSerializerInit;
import com.thoriuslight.professionsmod.item.ExtractorBlockItem;
import com.thoriuslight.professionsmod.network.ModPacketHandler;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.world.gen.ModOreGen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("professionsmod")
@Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.MOD)
public class ProfessionsMod{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "professionsmod";
    public static ProfessionsMod instance;
    //Registering
    //-------------------------------------------------------------------------------------------------------------------------------------------------
    public ProfessionsMod() {
    	final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	modEventBus.addListener(this::setup);
    	ItemInit.ITEMS.register(modEventBus);
    	ItemInit.VANILLA_ITEMS.register(modEventBus);
    	BlockInit.BLOCKS.register(modEventBus);
    	ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
    	ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
    	RecipeSerializerInit.RECIPE_SERIALIZERS.register(modEventBus);
    	FluidInit.FLUIDS.register(modEventBus);
    	
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ModOreGen::generateOreEvent);
    }
    
    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
    	event.getRegistry().register(new BlockItem(BlockInit.COPPER_ORE.get(), new Item.Properties().tab(ProfCommonItemGroup.instance).stacksTo(1)).setRegistryName("copper_ore"));
    	event.getRegistry().register(new BlockItem(BlockInit.SILVER_ORE.get(), new Item.Properties().tab(ProfCommonItemGroup.instance).stacksTo(1)).setRegistryName("silver_ore"));
    	event.getRegistry().register(new BlockItem(BlockInit.INSULATION_BRICK_BLOCK.get(), new Item.Properties().tab(ProfCommonItemGroup.instance)).setRegistryName("insulation_brick_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.SMITHCRAFTINGTABLE_BLOCK.get(), new Item.Properties().tab(BlacksmithItemGroup.instance)).setRegistryName("smithcraftingtable_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.STONEANVIL_BLOCK.get(), new Item.Properties().tab(BlacksmithItemGroup.instance)).setRegistryName("stoneanvil_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.CASTINGBASIN_BLOCK.get(), new Item.Properties().tab(BlacksmithItemGroup.instance)).setRegistryName("castingbasin_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.STONECASTINGBASIN_BLOCK.get(), new Item.Properties().tab(BlacksmithItemGroup.instance)).setRegistryName("stonecastingbasin_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.INSPECTIONTABLE_BLOCK.get(), new Item.Properties().tab(BlacksmithItemGroup.instance)).setRegistryName("inspectiontable_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.FORGE_BLOCK.get(), new Item.Properties().tab(BlacksmithItemGroup.instance)).setRegistryName("forge_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.CRUCIBLE_BLOCK.get(), new Item.Properties().tab(BlacksmithItemGroup.instance)).setRegistryName("crucible_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.WOODENHOPPER_BLOCK.get(), new Item.Properties().tab(EngineeringItemGroup.instance)).setRegistryName("woodenhopper_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.ALCHEMISTCRAFTINGTABLE_BLOCK.get(), new Item.Properties().tab(AlchemyItemGroup.instance)).setRegistryName("alchemistcraftingtable_block"));
    	event.getRegistry().register(new BlockItem(BlockInit.GLASS_JAR.get(), new Item.Properties().tab(AlchemyItemGroup.instance)).setRegistryName("glass_jar"));
    	event.getRegistry().register(new ExtractorBlockItem(BlockInit.EXTRACTOR_CONTROLLER_BLOCK.get(), new Item.Properties().tab(AlchemyItemGroup.instance)).setRegistryName("extractor_controller_block"));
    	LOGGER.debug("Registered BlockItems");
    }

    private void setup(final FMLCommonSetupEvent event){
    	ModPacketHandler.registerPackets();
    	DataInit.initData();
    	CapabilityProfession.register();
    	event.enqueueWork(() -> {
			ModLootConditionTypes.init();
		});
    }
    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
    	CommandProfession.register(event.getDispatcher());
    }
  
    //ItemGroups
    //-------------------------------------------------------------------------------------------------------------------------------------------------
    public static class ProfCommonItemGroup extends ItemGroup{
    	public static final ProfCommonItemGroup instance = new ProfCommonItemGroup(ItemGroup.TABS.length, "profcommontab");
		private ProfCommonItemGroup(int index, String label) {
			super(index, label);
		}

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(BlockInit.COPPER_ORE.get().asItem());
		}
    	
    }
    public static class BlacksmithItemGroup extends ItemGroup{
    	public static final BlacksmithItemGroup instance = new BlacksmithItemGroup(ItemGroup.TABS.length, "blacksmithtab");
		private BlacksmithItemGroup(int index, String label) {
			super(index, label);
		}

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.STONEHAMMER_ITEM.get());
		}
    	
    }
    public static class EngineeringItemGroup extends ItemGroup{
    	public static final EngineeringItemGroup instance = new EngineeringItemGroup(ItemGroup.TABS.length, "engineertab");
		private EngineeringItemGroup(int index, String label) {
			super(index, label);
		}

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(BlockInit.WOODENHOPPER_BLOCK.get().asItem());
		}
    	
    }
    public static class AlchemyItemGroup extends ItemGroup{
    	public static final AlchemyItemGroup instance = new AlchemyItemGroup(ItemGroup.TABS.length, "alchemytab");
		private AlchemyItemGroup(int index, String label) {
			super(index, label);
		}

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.MORTARANDPESTLE.get().asItem());
		}
    	
    }
    //Capabilities
    //-------------------------------------------------------------------------------------------------------------------------------------------------
    @SubscribeEvent
    public void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof PlayerEntity)
        	event.addCapability(new ResourceLocation(MODID, "profession"), new CapabilityProfession());
    }  
}
