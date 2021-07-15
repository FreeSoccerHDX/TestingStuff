package freesoccerhdx.survivalplus.events.player;

import java.io.IOException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.PlayerPacketEvent;
import freesoccerhdx.survivalplus.haupt.main;
import freesoccerhdx.survivalplus.haupt.signgui.SignGUI;
import freesoccerhdx.survivalplus.npc.NPCHandler;
import freesoccerhdx.survivalplus.npc.NPCMethods;
import freesoccerhdx.survivalplus.npc.NPCPlayer;
import freesoccerhdx.survivalplus.npc.NPCSkins;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcher.Item;
import net.minecraft.server.v1_16_R3.PacketPlayInUseEntity.EnumEntityUseAction;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityPose;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_16_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutAbilities;
import net.minecraft.server.v1_16_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R3.PacketPlayOutUpdateAttributes;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.minecraft.server.v1_16_R3.WorldServer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntitySound;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;

public class PlayerPacketEventHandler {

	public static PacketDataSerializer serializer(Packet<?> packet) {
		ByteBuf bb = Unpooled.buffer();
		PacketDataSerializer pds = new PacketDataSerializer(bb);
		try {
			packet.b(pds);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return pds;
	}
	
	public static PlayerPacketEvent onPlayerPacket(PlayerPacketEvent e) {
		Player p = e.getPlayer();
		Object packet = e.getPacket();
		boolean playin = e.isPacketPlayIn();
		boolean playout = e.isPacketPlayOut();
		
		if(playin) {
			if(packet instanceof PacketPlayInUpdateSign) {
	    		PacketPlayInUpdateSign ppius = (PacketPlayInUpdateSign) packet;
	    		String[] lines = ppius.c();
	    		if(SignGUI.hasGUI(p)) {  			
	    			SignGUI.closeGUI(p, lines);
	    		}
	    	}
			if(packet instanceof PacketPlayInUseEntity) {
				PacketPlayInUseEntity ppiue = (PacketPlayInUseEntity) packet;
				EnumEntityUseAction eeua = ppiue.b();
				EnumHand hand = ppiue.c();
				int entityid = (int) NPCMethods.getValue(ppiue, "a");
				
				//Bukkit.broadcastMessage("hit " + eeua + " , " + hand + ", " +entityid);
				if(entityid != 0) {
					for(NPCPlayer npc : NPCHandler.getRegistered()) {
						if(npc.getEntityPlayer().getId() == entityid) {
							if(eeua == EnumEntityUseAction.ATTACK) {
								if(npc.hasKlon()) {
									//p.damage(arg0, npc.getKlon());
									NPCMethods.setValue(ppiue, "a", ((CraftEntity) npc.getKlon()).getHandle().getId());
									e.setNewPacket(packet);
								}
							}else {
								if(hand != null) {
									if(hand == EnumHand.MAIN_HAND) {
										if(p.getInventory().getItemInMainHand().getType() == Material.AIR) {
											Inventory inv = Bukkit.createInventory(p, 54, "NPC-Editor");
											
											Bukkit.getScheduler().runTask(main.m, ()->p.openInventory(inv));
											Bukkit.getScheduler().runTask(main.m, ()->NPCHandler.setEditInventory(p,npc,inv));
											
											
											int i = 45;
											for(EnumItemSlot eis : EnumItemSlot.values()) {
												ItemStack item = Methoden.item(Material.RED_STAINED_GLASS_PANE, 1, 0, "§cKein Item", new String[] {});
												if(npc.getEquipment().containsKey(eis)) {
													item = NPCMethods.getBukkitItemStack(npc.getEquipment().get(eis));
												}
												inv.setItem(i, item);
												inv.setItem(i+1, Methoden.item(Material.WHITE_STAINED_GLASS_PANE, 1, 0, "§7"+eis.toString(), new String[] {}));
												i -= 9;
											}
											// TODO: add edits: name, move/attack/defend or just static
											inv.addItem(Methoden.item(Material.NAME_TAG, 1, 0, "§6Setze den Name fest", new String[] {"§7Aktuell: "+npc.getName()}));
											inv.addItem(Methoden.item(Material.DIAMOND_SWORD, 1, 0, "§6Soll der NPC sich bewegen ?", new String[] {"§7Aktuell: "+npc.hasKlon()}));
											//inv.addItem(Methoden.item(Material.NAME_TAG, 1, 0, "§6Setze den Name fest", new String[] {}));
											
											i = 1;
											int slot = 20;
											for(NPCSkins skins : NPCSkins.values()) {
												inv.setItem(slot,Methoden.item(Material.PLAYER_HEAD,1, 0, skins.getName() , new String[] {"§bSkin-Preview Nr.: "+i}));
												slot++;
												while(inv.getItem(slot)!=null && inv.getItem(slot).getType() != Material.AIR && slot < 54) slot++;
												i++;
											}
											
											
										}else if(npc.hasKlon()) {
											NPCMethods.setValue(ppiue, "a", ((CraftEntity) npc.getKlon()).getHandle().getId());
											e.setNewPacket(packet);
										}
									}
								}
							}
							break;
						}
					}
				}				
			}
		}
		
		if(playout) {
			if(packet instanceof PacketPlayOutNamedEntitySpawn 
					|| packet instanceof PacketPlayOutSpawnEntityLiving 
					) {
				int entid = (int) NPCMethods.getValue(packet, "a");
				
				for(NPCPlayer npc : NPCHandler.getRegistered()) {
					if(npc.hasKlon()) {
						if(((CraftEntity) npc.getKlon()).getHandle().getId() == entid) {
							e.setNewPacket(null);
							break;
						}
					}
				}
				
			}
		}
		
		/*
		if(playout) {
			if(packet instanceof PacketPlayOutEntityMetadata) {
				PacketPlayOutEntityMetadata ppoem = (PacketPlayOutEntityMetadata) packet;
				ByteBuf bb = Unpooled.buffer();
        		PacketDataSerializer pds = new PacketDataSerializer(bb);
        		try {
        			ppoem.b(pds);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        		//List<DataWatcher.Item<?>> b = DataWatcher.a(pds);
        		
        		int entityid = pds.i();
        		WorldServer nmsWorld = ((CraftWorld) p.getWorld()).getHandle(); //NMS World Server
        		Entity be =  nmsWorld.getEntity(entityid);
        		
        		if(be != null) {
        			DataWatcher dw = new DataWatcher(be);
        			dw.set(new DataWatcherObject<>(6, DataWatcherRegistry.s), EntityPose.CROUCHING);
        			ppoem = new PacketPlayOutEntityMetadata(entityid, dw, true);
        			
        			e.setNewPacket(ppoem);
        		}
        		
			}
			
			if(packet instanceof PacketPlayOutNamedEntitySpawn){
				PacketPlayOutNamedEntitySpawn ppones = (PacketPlayOutNamedEntitySpawn) packet;
				ByteBuf bb = Unpooled.buffer();
        		PacketDataSerializer pds = new PacketDataSerializer(bb);
        		try {
        			ppones.b(pds);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        		
        		int a = pds.i();
        		UUID b = pds.k();
        		double c = pds.readDouble()+5;
        		double d = pds.readDouble();
        		double e0 = pds.readDouble()+5;
        		byte f = pds.readByte();
        		byte g = pds.readByte();
			}
			
			//System.out.println(packet);
			if(packet instanceof PacketPlayOutRelEntityMove) {
	
				PacketPlayOutRelEntityMove ppop = (PacketPlayOutRelEntityMove) packet;
				
        		ByteBuf bb = Unpooled.buffer();
        		PacketDataSerializer pds = new PacketDataSerializer(bb);
        		try {
        			ppop.b(pds);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        		
        		pds.setDouble(0, pds.getDouble(0)+5);
        		pds.setDouble(1, pds.getDouble(1)+5);
        		pds.setDouble(2, pds.getDouble(2)+5);
        		try {
					ppop.a(pds);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        		e.setNewPacket(ppop);
			}
			
		}
		
		
		if(playout) {
			if(packet instanceof PacketPlayOutEntityVelocity) {
        		PacketPlayOutEntityVelocity ppoev = (PacketPlayOutEntityVelocity) packet;
        		
        		ByteBuf bb = Unpooled.buffer();
        		PacketDataSerializer pds = new PacketDataSerializer(bb);
        		try {
					ppoev.b(pds);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        		double x = pds.getShort(0)/8000.0;
        		double y = pds.getShort(0)/8000.0;
        		double z = pds.getShort(0)/8000.0;
        		
        		if(y < 0) {
        			
        			if(pds.i() == ((CraftPlayer) p).getHandle().getId()) {
	        			p.sendMessage("runter");
	        			PacketPlayOutEntityVelocity newppoev = new PacketPlayOutEntityVelocity(pds.i(), new Vec3D(x,-y,z));
	        			e.setNewPacket(newppoev);
	        			p.sendMessage("set");
        			}
        		}
        		
        		
        		
			}
		}
		
		*/
		return e;
		
	}
	
}
