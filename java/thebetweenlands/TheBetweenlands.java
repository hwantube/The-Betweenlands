package thebetweenlands;

import net.minecraftforge.common.DimensionManager;
import thebetweenlands.blocks.BLBlockRegistry;
import thebetweenlands.entities.BLEntityRegistry;
import thebetweenlands.items.BLItemRegistry;
import thebetweenlands.lib.ModInfo;
import thebetweenlands.proxy.CommonProxy;
import thebetweenlands.world.WorldProviderBetweenlands;
import thebetweenlands.world.feature.structure.WorlGenDruidCircle;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION)
public class TheBetweenlands
{
	@SidedProxy(modId = ModInfo.ID, clientSide = ModInfo.CLIENTPROXY_LOCATION, serverSide = ModInfo.COMMONPROXY_LOCATION)
	public static CommonProxy proxy;

	@Instance(ModInfo.ID)
	public static TheBetweenlands instance;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {

		//BL Registry
		BLItemRegistry.init();
		BLBlockRegistry.init();
		BLEntityRegistry.init();
		
		GameRegistry.registerWorldGenerator(new WorlGenDruidCircle(), 0);
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		// ConfigHandler.init(event.getSuggestedConfigurationFile()); -- Leave those there, we may need them.
		
		
		//TODO: Just temporary to test some stuff
		DimensionManager.registerProviderType(ModInfo.DIMENSION_ID, WorldProviderBetweenlands.class, true);
		DimensionManager.registerDimension(ModInfo.DIMENSION_ID, ModInfo.DIMENSION_ID);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		//BL Registry
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
		//Reciepes.init();
		// For ores GameRegistry.registerWorldGenerator(new WORLDGENNAMEGOESHERE());

		// DimensionManager.registerProviderType(Universe.dimensionId, WorldProviderUniverse.class, false);
		// DimensionManager.registerDimension(Universe.dimensionId, Universe.dimensionId);
	}
}
