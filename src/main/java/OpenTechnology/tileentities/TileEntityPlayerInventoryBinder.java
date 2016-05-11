package OpenTechnology.tileentities;

import OpenTechnology.utils.Utils;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Analyzable;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.SimpleComponent;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

/**
 * Created by Avaja on 11.05.2016.
 */
public class TileEntityPlayerInventoryBinder extends TileEntityEnvironment implements SimpleComponent, Analyzable {

    public static ArrayList<TileEntityPlayerInventoryBinder> binders = new ArrayList<TileEntityPlayerInventoryBinder>();

    private String player_uuid;
    private EntityPlayer player;

    public TileEntityPlayerInventoryBinder() {
        node = Network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector().create();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    @Override
    public Node[] onAnalyze(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return new Node[]{node};
    }

    @Override
    public String getComponentName() {
        return "inventory_binder";
    }

    public void setPlayer(EntityPlayer player){
        this.player = player;
    }

    public boolean addPlayer(EntityPlayer player){
        if (player_uuid.equals(player.getUniqueID().toString())){
            this.player = player;
            return true;
        }
        return false;
    }

    public boolean isConnected(){
        return player != null || player_uuid != null && player_uuid.isEmpty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("player_uuid")){
            player_uuid = nbt.getString("player_uuid");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (player != null){
            nbt.setString("player_uuid", player.getUniqueID().toString());
        }
    }

    @Callback(doc="")
    public Object[] getStackInSlot(Context context, Arguments arguments) throws Exception{
        if (!isConnected())
            return new Object[]{false, "player not connected"};

        int slot = arguments.checkInteger(0);
        if (player != null){
            if (slot < 0 || slot > player.inventory.getSizeInventory())
                return new Object[]{false, "invalid slot"};
            ItemStack stack = player.inventory.getStackInSlot(slot);
            if(stack == null)
                return new Object[]{"null"};
            return new Object[]{Utils.createStringTable("unlocalizedname", stack.getUnlocalizedName(), "name", stack.getDisplayName(), "id", Item.getIdFromItem(stack.getItem()), "stacksize", stack.stackSize, "damage", stack.getItemDamage())};
        }
        return new Object[]{false};
    }
}