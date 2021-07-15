package de.freesoccerhdx.testingstuff.main.utils;

import java.util.HashMap;

import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.freesoccerhdx.testingstuff.main.utils.InventoryHelper.ClickAction;
import de.freesoccerhdx.testingstuff.main.utils.InventoryHelper.ClickMatch;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerAccess;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Containers;
import net.minecraft.world.inventory.ICrafting;
import net.minecraft.world.level.World;

public class AnvilGUI {

	private static HashMap<String, AnvilGUI> playerguis = new HashMap<>();
	
	public static boolean removePlayer(Player p, boolean closeInventory) {
		if(hasPlayer(p)) {
			playerguis.remove(p.getName());
			if(closeInventory) {
				p.closeInventory();
			}
			return true;
		}
		return false;
	}
	public static boolean hasPlayer(Player p) {
		return playerguis.containsKey(p.getName());
	}
	
	public static AnvilGUI getPlayerGUI(Player p) {
		
		if(hasPlayer(p)) {
			return playerguis.get(p.getName());
		}
		
		return null;
	}
	private static void addPlayer(Player p, AnvilGUI gui) {
		playerguis.put(p.getName(), gui);
	}
	
	
	
	private ItemStack nameitem = new ItemStack(Material.PAPER);
	private ClickAction clickaction = null;
	private AnvilInput ai = null;
	private String title = "";
	
	
	public AnvilGUI() {
		this(null,"");
	}
	public AnvilGUI(String name) {
		this(null,name);
	}
	
	public AnvilGUI(ItemStack nameitem, String name) {
		if(nameitem == null) {
			nameitem = new ItemStack(Material.PAPER);
			ItemMeta meta = nameitem.getItemMeta();
			meta.setDisplayName("");
			nameitem.setItemMeta(meta);
		}
		
		this.title = name;
		this.nameitem = nameitem;
		
	}
	
	public void setAnvilInput(AnvilInput ai) {
		this.ai = ai;
	}
	public boolean hasAnvilInput() {
		return ai != null;
	}
	
	public void setAnvilAction(AnvilAction aa) {
		this.clickaction = aa;
	}
	public boolean hasAnvilAction() {
		return clickaction != null;
	}
	
	
	public void open(Player p) {
		EntityPlayer ep = ((CraftPlayer)p).getHandle();
		
		Container anvilcontainer = new AnvilContainer(p, title);
		//ep.activeContainer = anvilcontainer;
		ep.bV = anvilcontainer;
		//Inventory inv = ((Container) ep.activeContainer).getBukkitView().getTopInventory();
		Inventory inv = ((Container) ep.bV).getBukkitView().getTopInventory();

		int windowid = ((AnvilContainer) anvilcontainer).getContainerId();
		
		//((Container) ep.activeContainer).addSlotListener(ep);
		ep.initMenu(anvilcontainer);
		//((Container) ep.bV).addSlotListener(ep.bU);
		
		
		InventoryHelper invhelp = new InventoryHelper(inv);
		invhelp.setDefaultCancelClick(true);
		invhelp.setDefaultCancelDrag(true);
		
		invhelp.addInvListener(new ClickMatch() {			
			@Override
			public boolean matches(Player p, InventoryView view, Inventory clickedinv, ClickType clicktype, int clickedslot,
					ItemStack clickeditem, ItemStack cursoritem) {
				
				if(clickedinv == null) return false;
				
				if(clickedinv.getType() == InventoryType.ANVIL) {
					if(clickedslot == 0 || clickedslot == 1 || clickedslot == 2) {
						return true;
							
					}
				}
				return false;
			}
		}, clickaction, true, true);
		
		//PacketPlayOutOpenWindow ppoow = new PacketPlayOutOpenWindow(windowid, Containers.ANVIL, new ChatComponentText(title));
		PacketPlayOutOpenWindow ppoow = new PacketPlayOutOpenWindow(windowid, Containers.h, new ChatComponentText(title));
		ep.b.sendPacket(ppoow);
		
		inv.setItem(0, nameitem);
		
		
		removePlayer(p, true);
		addPlayer(p, this);
		
	}
	
	
	public ItemStack getNameItem() {
		return this.nameitem;
	}

	
	public static abstract class AnvilInput{
		public AnvilInput() {
		}
		
		public abstract void onUpdateAnvilName(Player p, String currentname);
		
	}
	
	public static abstract class AnvilAction extends ClickAction{
		
		public AnvilAction() {
			
		}
		
		@Override
		public void run(Player p, InventoryView view,Inventory clickedinv, ClickType clicktype, int clickedslot, ItemStack clickeditem, ItemStack cursoritem) {
			
			AnvilSlot as = null;
			
			if(clickeditem == null) return;
			if(clickeditem.getType() == Material.AIR) return; 
			
			if(clickedslot == 0) as = AnvilSlot.INPUT_LEFT;
			if(clickedslot == 1) as = AnvilSlot.INPUT_RIGHT;
			if(clickedslot == 2) as = AnvilSlot.OUTPUT;
			
			AnvilInventory ai = (AnvilInventory) view.getTopInventory();
			
			run(p, as, ai.getRenameText());
		}
		
		public abstract void run(Player player, AnvilSlot anvilslot, String renamename);
		
		
		//abstract void run(Player player,boolean affecttopinv,boolean affectbotinv,InventoryView view,ItemStack oldcursor,ItemStack newcursor, DragType type,Set<Integer> rawslots,Map<Integer, ItemStack> addeditems);
		
	}
	public static enum AnvilSlot{
		INPUT_LEFT, INPUT_RIGHT, OUTPUT;
	}
	
	private static class AnvilContainer extends ContainerAnvil {
		
		private static int getRealNextContainerId(Player player) {
			EntityPlayer ep= ((CraftPlayer)player).getHandle();
			return ep.nextContainerCounter();
		}

        public AnvilContainer(Player player, String guiTitle) {
            super(getRealNextContainerId(player), ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(0, 0, 0)));
            this.checkReachable = false;
            setTitle(new ChatMessage(guiTitle));
            
        }

        @Override
        public void e() {
            super.e();
            
            //this.levelCost.set(0);
            this.w.set(0);
            
        }

        @Override
        public void b(EntityHuman entityhuman) {
        }


        public int getContainerId() {
            //return windowId;
        	return j;
        }

    }

	public void onAnvilInput(Player p, String b) {
		if(this.hasAnvilInput()) {
			ai.onUpdateAnvilName(p, b);
		}
	}
	
}
