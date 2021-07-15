package de.freesoccerhdx.testingstuff.main.utils;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import de.freesoccerhdx.testingstuff.main.TestingStuff;


public class InventoryHelper implements Listener{

	private Inventory targetinv = null;
	
	private HashMap<ClickMatch, Pair<ClickAction, Pair<Boolean,Boolean>>> invclicks = new HashMap<>();
	private HashMap<DragMatch, Pair<DragAction, Pair<Boolean,Boolean>>> invdrags = new HashMap<>();
	
	private List<ClickListener> clicklistener = new ArrayList<>();
	private List<DragListener> draglistener = new ArrayList<>();
	
	
	
	
	
	private ClickAction default_clickaction = null;
	private boolean default_cancelclick = false;
	
	private DragAction default_dragaction = null;
	private boolean default_canceldrag = false;
	
	private CloseListener close_action = null;
	
	
	
	public InventoryHelper(String inventory_name, InventoryType type) {
		this(Bukkit.createInventory(null, type, inventory_name));
	}
	
	public InventoryHelper(String inventory_name, int size) {
		this(Bukkit.createInventory(null, size, inventory_name));
	}
	
	public InventoryHelper(Inventory inv) {
		targetinv = inv;
		Bukkit.getPluginManager().registerEvents(this, TestingStuff.main);
	}
	
	public void openInventory(Player p) {
		p.openInventory(targetinv);
	}
	public Inventory getInventory() {
		return targetinv;
	}
	
	public void addClickListener(ClickListener cl) {
		clicklistener.add(cl);
	}
	public List<ClickListener> getClickListeners(){
		return clicklistener;
	}
	public void addDragListener(DragListener cl) {
		draglistener.add(cl);
	}
	public List<DragListener> getDragListeners(){
		return draglistener;
	}
	
	public void setItem(int slot, ItemStack item) {
		targetinv.setItem(slot, item);
	}
	
	public void setItem(int slot, ItemStack item, ClickAction clickaction, boolean cancelclick, boolean breakafterrun) {
		
		addInvListener(new ClickMatch() {
			@Override
			public boolean matches(Player p, InventoryView view, Inventory clickedinv, ClickType clicktype, int clickedslot,
					ItemStack clickeditem, ItemStack cursoritem) {
				return clickeditem.isSimilar(item);
			}
			
		}, clickaction, cancelclick, breakafterrun);
		
		targetinv.setItem(slot, item);
	}
	
	public void addItem(ItemStack item) {
		targetinv.addItem(item);
	}
	
	public void addItem(ItemStack item, ClickAction clickaction, boolean cancelclick, boolean breakafterrun) {
		
		addInvListener(new ClickMatch() {
			@Override
			public boolean matches(Player p, InventoryView view, Inventory clickedinv, ClickType clicktype, int clickedslot,
					ItemStack clickeditem, ItemStack cursoritem) {
				return clickeditem.isSimilar(item);
			}
			
		}, clickaction, cancelclick, breakafterrun);
		
		targetinv.addItem(item);
	}
	
	
	
	public void addInvListener(ClickMatch match, ClickAction action, boolean cancelclick, boolean breakafterrun) {
		invclicks.put(match, Pair.of(action, Pair.of(cancelclick, breakafterrun)));
	}
	
	public void addDragListener(DragMatch match, DragAction action, boolean cancelclick, boolean breakafterrun) {
		invdrags.put(match, Pair.of(action, Pair.of(cancelclick, breakafterrun)));
	}
	
	
	public void setDefaultCancelClick(boolean b) {
		default_cancelclick = b;
	}
	public boolean isDefaultCancelingClicks() {
		return default_cancelclick;
	}
	public void setDefaultClickAction(ClickAction ca) {
		default_clickaction = ca;
	}
	public boolean hasDefaultClickAction() {
		return default_clickaction != null;
	}
	
	public boolean isSameInventory(Inventory first, Inventory second){
		if(first == null || second == null) return false;
	    return ((CraftInventory) first).getInventory().equals(((CraftInventory) second).getInventory());
	}
	
	public void setCloseAction(CloseListener ca) {
		this.close_action = ca;
	}
	public boolean hasCloseAction() {
		return close_action != null;
	}
	

	
	public void setDefaultDragAction(DragAction da) {
		default_dragaction = da;
	}
	public boolean hasDefaultDragAction() {
		return default_dragaction != null;
	}
	
	public void setDefaultCancelDrag(boolean b) {
		default_canceldrag = b;
	}
	public boolean isDefaultCancelingDrags() {
		return default_canceldrag;
	}
	
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		ItemStack cursoritem = e.getCursor() == null ? new ItemStack(Material.AIR) : e.getCursor();
		ItemStack oldcursoritem = e.getOldCursor() == null ? new ItemStack(Material.AIR) : e.getOldCursor();
		Inventory inv = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		Set<Integer> rawslots = e.getRawSlots();
		InventoryView view = e.getView();	
		Map<Integer, ItemStack> addedItems = e.getNewItems();
		DragType type = e.getType();
		
		
		
		if(isSameInventory(view.getTopInventory(), targetinv)) {
			boolean affecttop = false;
			boolean affectbot = false;
			
			int invsize = view.getTopInventory().getSize();
			for(int i : rawslots) {
				
				if(i < invsize) {
					affecttop = true;
				}else {
					affectbot = true;
				}
				if(affecttop && affectbot) break;
				 
			}
			boolean didmatch = false;
			
			for(DragListener dm : this.draglistener) {
				boolean check = dm.check(p, affecttop, affectbot, view, oldcursoritem, cursoritem, type, rawslots, addedItems);
				
				if(check) {
					didmatch = true;
					dm.run(p, affecttop, affectbot, view, oldcursoritem, cursoritem, type, rawslots, addedItems);
					
					boolean iscancel = dm.isCancel(p, affecttop, affectbot, view, oldcursoritem, cursoritem, type, rawslots, addedItems); 
					e.setCancelled(iscancel);
					
					boolean isstoprun = dm.breakAfterRun(p, affecttop, affectbot, view, oldcursoritem, cursoritem, type, rawslots, addedItems);
					if(isstoprun) return;
					
				}
				
			}
			
			for(DragMatch st : invdrags.keySet()) {
				boolean matches = st.matches(p, affecttop, affectbot, view, oldcursoritem, cursoritem, type, rawslots, addedItems);
				
				if(matches) {
					didmatch = true;
					Pair<DragAction,Pair<Boolean,Boolean>> pair = invdrags.get(st);
					DragAction ca = pair.getLeft();
					boolean cancelclick = pair.getRight().getLeft();
					boolean breakafterrun = pair.getRight().getRight();
					
					e.setCancelled(cancelclick);
					ca.run(p, affecttop, affectbot, view, oldcursoritem, cursoritem, type, rawslots, addedItems);
					
					if(breakafterrun) return;
				}
				
			}
			
			if(!didmatch) {
				e.setCancelled(default_canceldrag);
				if(hasDefaultDragAction()) {
					default_dragaction.run(p, affecttop, affectbot, view, oldcursoritem, cursoritem, type, rawslots, addedItems);
				}
			}
			
			
		}
		
	}
	
	@EventHandler
	private void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		InventoryView view = e.getView();
		Inventory inv = e.getInventory();
		
		if(isSameInventory(view.getTopInventory(), targetinv)) {
			if(hasCloseAction()) {
				close_action.run(p, inv);
			}
		}
		
	}
	
	@EventHandler
	private void onInv(InventoryClickEvent e) {
		ClickType clicktype = e.getClick();
		Inventory clickedInv = e.getClickedInventory();
		ItemStack currentitem = e.getCurrentItem() == null ? new ItemStack(Material.AIR) : e.getCurrentItem();
		ItemStack cursorItem = e.getCursor() == null ? new ItemStack(Material.AIR) : e.getCursor();
		Inventory inventory = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		InventoryView invView = e.getView();
		int slotid = e.getSlot();
		SlotType slottype = e.getSlotType();
		Inventory clickedinv = e.getClickedInventory();
		
		
		
		if(invView == null) return;
		if(invView.getTopInventory() == null) return;
	
		
		if(isSameInventory(invView.getTopInventory(), targetinv)) {

			
			boolean didmatch = false;
			
			for(ClickListener cl : this.clicklistener) {
				boolean check = cl.check(p, invView,clickedinv, clicktype, slotid, currentitem, cursorItem);
				
				if(check) {
					didmatch = true;
					cl.run(p, invView,clickedinv, clicktype, slotid, currentitem, cursorItem);
					
					boolean iscancel = cl.isCancel(p, invView,clickedinv, clicktype, slotid, currentitem, cursorItem); 
					e.setCancelled(iscancel);
					
					boolean isstoprun = cl.breakAfterRun(p, invView,clickedinv, clicktype, slotid, currentitem, cursorItem);
					if(isstoprun) return;
					
				}
				
			}
			
			for(ClickMatch st : invclicks.keySet()) {
				
				boolean matches = st.matches(p, invView,clickedinv, clicktype, slotid, currentitem, cursorItem);
				
				if(matches) {
					didmatch = true;
					Pair<ClickAction,Pair<Boolean, Boolean>> pair = invclicks.get(st);
					ClickAction ca = pair.getLeft();
					boolean cancelclick = pair.getRight().getLeft();
					boolean breakafterrun = pair.getRight().getRight();
					
					e.setCancelled(cancelclick);
					ca.run(p, invView,clickedinv, clicktype, slotid, currentitem, cursorItem);
					
					if(breakafterrun) return;
				}
				
			}
			
			if(!didmatch) {
				e.setCancelled(default_cancelclick);
				if(hasDefaultClickAction()) {
					default_clickaction.run(p, invView,clickedinv, clicktype, slotid, currentitem, cursorItem);
				}
			}
			
		}
		
	}
	
	
	public static abstract class DragAction {
		
		public DragAction() {
		}
		
		public abstract void run(Player player,boolean affecttopinv,boolean affectbotinv,InventoryView view,ItemStack oldcursor,ItemStack newcursor, DragType type,Set<Integer> rawslots,Map<Integer, ItemStack> addeditems);
		
	}
	public static abstract class DragMatch {
		public DragMatch() {
		}
		
		public abstract boolean matches(Player player,boolean affecttopinv,boolean affectbotinv,InventoryView view,ItemStack oldcursor,ItemStack newcursor, DragType type,Set<Integer> rawslots,Map<Integer, ItemStack> addeditems);
		
	}
	
	public static abstract class ClickMatch {
		
		public ClickMatch() {	
		}
		
		public abstract boolean matches(Player p, InventoryView view,Inventory clickedinv, ClickType clicktype, int clickedslot, ItemStack clickeditem, ItemStack cursoritem);
		
	}
	public static abstract class ClickAction {
		
		public ClickAction() {
		}
		
		public abstract void run(Player p, InventoryView view,Inventory clickedinv, ClickType clicktype, int clickedslot, ItemStack clickeditem, ItemStack cursoritem);
		
	}
	
	
	public static abstract class CloseListener {
		
		public CloseListener() {
		}
		
		public abstract void run(Player p, Inventory inv);
	}
	
	

	public static abstract class ClickListener {
		
		public ClickListener() {	
		}
		
		abstract boolean check(Player p, InventoryView view,Inventory clickedinv, ClickType clicktype, int clickedslot, ItemStack clickeditem, ItemStack cursoritem);
		abstract boolean isCancel(Player p, InventoryView view,Inventory clickedinv,  ClickType clicktype, int clickedslot, ItemStack clickeditem, ItemStack cursoritem);
		abstract void run(Player p, InventoryView view,Inventory clickedinv,  ClickType clicktype, int clickedslot, ItemStack clickeditem, ItemStack cursoritem);
		abstract boolean breakAfterRun(Player p, InventoryView view,Inventory clickedinv,  ClickType clicktype, int clickedslot, ItemStack clickeditem, ItemStack cursoritem);
	}

	public static abstract class DragListener {
		
		public DragListener() {	
		}
		
		abstract boolean check(Player player,boolean affecttopinv,boolean affectbotinv,InventoryView view,ItemStack oldcursor,ItemStack newcursor, DragType type,Set<Integer> rawslots,Map<Integer, ItemStack> addeditems);
		abstract boolean isCancel(Player player,boolean affecttopinv,boolean affectbotinv,InventoryView view,ItemStack oldcursor,ItemStack newcursor, DragType type,Set<Integer> rawslots,Map<Integer, ItemStack> addeditems);
		abstract void run(Player player,boolean affecttopinv,boolean affectbotinv,InventoryView view,ItemStack oldcursor,ItemStack newcursor, DragType type,Set<Integer> rawslots,Map<Integer, ItemStack> addeditems);
		abstract boolean breakAfterRun(Player player,boolean affecttopinv,boolean affectbotinv,InventoryView view,ItemStack oldcursor,ItemStack newcursor, DragType type,Set<Integer> rawslots,Map<Integer, ItemStack> addeditems);
	}
	
}















