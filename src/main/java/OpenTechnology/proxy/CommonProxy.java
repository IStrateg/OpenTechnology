package OpenTechnology.proxy;

import OpenTechnology.OpenTechnology;
import OpenTechnology.blocks.Blocks;
import OpenTechnology.events.Events;
import OpenTechnology.gui.GuiHandler;
import OpenTechnology.item.Items;
import OpenTechnology.item.recipes.Recipes;
import OpenTechnology.packet.PacketPlayerPosition;
import OpenTechnology.tileentities.TileEntityChatBox;
import OpenTechnology.tileentities.TileEntityAdminChatBox;
import OpenTechnology.tileentities.TileEntityPlayerInventoryBinder;
import OpenTechnology.tileentities.TileEntityTeleporter;
import OpenTechnology.driver.Drivers;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public static SimpleNetworkWrapper wrapper;

	public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("OpenTechnology");
        NetworkRegistry.INSTANCE.registerGuiHandler(OpenTechnology.instance, new GuiHandler());

        MinecraftForge.EVENT_BUS.register(new Events());

        Blocks.init();
        Items.init();
        Drivers.init();
        Recipes.init();

        GameRegistry.registerTileEntity(TileEntityTeleporter.class, "TileEntityTeleporter");
        GameRegistry.registerTileEntity(TileEntityAdminChatBox.class, "TileEntityAdminChatBox");
        GameRegistry.registerTileEntity(TileEntityChatBox.class, "TileEntityChatBox");
        GameRegistry.registerTileEntity(TileEntityPlayerInventoryBinder.class, "TileEntityPlayerInventoryBinder");

        wrapper.registerMessage(PacketPlayerPosition.Handler.class, PacketPlayerPosition.class, 0, Side.CLIENT);
    }

    public void postInit(FMLPostInitializationEvent e) {
    }
}