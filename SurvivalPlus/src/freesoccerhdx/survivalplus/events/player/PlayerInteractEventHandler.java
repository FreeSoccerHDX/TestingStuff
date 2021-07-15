package freesoccerhdx.survivalplus.events.player;

import java.io.File;


import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import de.tr7zw.nbtapi.NBTItem;
import freesoccerhdx.survivalplus.haupt.BuildersWand;
import freesoccerhdx.survivalplus.haupt.Sorter;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.SpawnerInfo;
import freesoccerhdx.survivalplus.haupt.main;
import freesoccerhdx.survivalplus.haupt.signgui.SignGUI;
import freesoccerhdx.survivalplus.haupt.signgui.SignGUIListener;
import freesoccerhdx.survivalplus.npc.NPCHandler;
import freesoccerhdx.survivalplus.npc.NPCPlayer;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.NextTickListEntry;

public class PlayerInteractEventHandler implements Listener {
	
	
	@EventHandler
	public void PlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(Methoden.isItemStackValid(p.getInventory().getItemInMainHand())) {
				ItemStack hand = p.getInventory().getItemInMainHand();
				ItemMeta meta = hand.getItemMeta();
				if(meta.getDisplayName() != null) {
					NBTItem nbitem = new NBTItem(hand);
					if(nbitem.hasKey("npcegg")) {
						e.setCancelled(true);
						int count = hand.getAmount();
						count--;
						
						if(p.getGameMode() == GameMode.SURVIVAL) hand.setAmount(count);
						if(count <= 0) hand.setType(Material.AIR);
						
						p.getInventory().setItemInMainHand(hand);
						NPCPlayer npc = new NPCPlayer("Namenslos", "", e.getClickedBlock().getRelative(e.getBlockFace()).getLocation());
						npc.spawn(false);
						npc.addPlayer(p);
						NPCHandler.register(npc);
					}
				}
			}
		}
		if(e.getAction() == Action.LEFT_CLICK_BLOCK && p.getGameMode() == GameMode.SURVIVAL) {
			if(p.isSneaking()) {
				Block b = e.getClickedBlock();
				if(b.getState() instanceof Chest) {
					Chest chest = (Chest) b.getState();
					Inventory inv = chest.getInventory();
					ItemStack[] itemstacks = inv.getStorageContents();
					
					
					if(itemstacks.length > 0) {
						List<ItemStack> newitems = Sorter.sortItems(itemstacks);
						
						inv.clear();
						for(ItemStack items : newitems) {
							inv.addItem(items);
						}
						
						p.sendMessage("§aDie Kiste wurde sortiert!");
					}else {
						p.sendMessage("§cNix zum sortieren da!");
					}
				}
			}
		}
		
		
		
		if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(e.getHand() == EquipmentSlot.HAND) {
				if(p.isSneaking() && Tag.SIGNS.isTagged(p.getInventory().getItemInMainHand().getType())) {
					Block bl = e.getClickedBlock();
					if(Tag.SIGNS.isTagged(bl.getType())) {
						Sign sign = (Sign) bl.getState();
						BlockPosition bp = new BlockPosition(bl.getX(), bl.getY(), bl.getZ());
						String[] lines = sign.getLines();
						for(int i = 0; i < 4; i++) {
							lines[i] = lines[i].replace("§", "&");
						}
						SignGUI.openGUI(p, lines, new SignGUIListener() {
							@Override
							protected void listen(Player p, BlockPosition bp, String[] lines) {
								SignChangeEvent sce = new SignChangeEvent(bl, p, lines);
								Bukkit.getScheduler().runTask(main.m, ()->Bukkit.getPluginManager().callEvent(sce));
								
							}
						});
						e.setCancelled(true);
						
					}
				}
			}
		}
		
		
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getHand() == EquipmentSlot.HAND) {
				
				if(e.getClickedBlock() != null) {
					ItemStack hand = e.getItem();
					if(hand != null) {
						File file = new File("plugins/redstonetimer/");
						file.mkdirs();
						Block b = e.getClickedBlock();
						String safestring = b.getX()+"+"+b.getY()+"+"+b.getZ()+".yml";
						
						File cfgfile = new File("plugins/redstonetimer/",safestring);
						if(cfgfile.exists()) {
							YamlConfiguration cfg = YamlConfiguration.loadConfiguration(cfgfile);
							int sec = hand.getAmount();
							cfg.set("timer", Math.max(1, sec));
							try {
								cfg.save(cfgfile);
								p.sendMessage("§aDer Redstonetimer wurde auf "+sec+" Sekunden gestellt.");
							}catch(Exception ex) {
								p.sendMessage("§cnoob");
							}
						}
						
					}
				}
				
				if(BuildersWand.isWand(p.getInventory().getItemInMainHand())) {
					if(BuildersWand.hasTargetBlocks(p)) {
						e.setCancelled(true);
						BuildersWand.placeBlocks(p);
					}
				}
			}
		}
		
		
		if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
			main.blockface.put(p.getName(), e.getBlockFace());
			
			
		}
		if(e.getClickedBlock() != null) {
			Material mat = e.getClickedBlock().getType();
			
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND) {
				if(mat == Material.SPAWNER) {
					if(p.getInventory().getItemInMainHand() != null) {
						
						ItemStack hand = p.getInventory().getItemInMainHand();
						
						if(hand.getType() != null && hand.getType() == Material.NETHER_STAR) {
							
							if(SpawnerInfo.improveSpawner(p, e.getClickedBlock())) {
								hand.setAmount(hand.getAmount()-1);
								p.getInventory().setItemInMainHand(hand);
								return;
							}
						}else {
							SpawnerInfo.printSpawnerInfo(p,e.getClickedBlock(),false);
						}
					}
				}
			}
							
			if(mat == Material.SMITHING_TABLE) {
				ItemStack is = p.getInventory().getItemInMainHand();
				
				if(is != null && is.getType() != Material.AIR && EnchantmentTarget.BREAKABLE.includes(is.getType())) {
					Methoden.openDisenchantInv(p, is);
					e.setCancelled(true);
				}else {
					//p.sendMessage("§cDu musst ein Item in deiner Hand halten!");
				}
			}
		}
		
		
		if(e.getHand() == EquipmentSlot.HAND) {
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if(p.getInventory().getItemInMainHand() != null) {
					ItemStack is = p.getInventory().getItemInMainHand();
					if(is.getType() == Material.LEAD) {
						if(is.getItemMeta() != null) {
							if(is.getItemMeta().getDisplayName() != null) {
								if(is.getItemMeta().getDisplayName().equals("§6§l§k#§f§4§lMobfänger§6§l§k#")) {
									if(main.holdingentity.containsKey(p.getName())) {
										Entity pass = main.holdingentity.get(p.getName());									
										pass.setVelocity(p.getLocation().getDirection().multiply(1.3 *(p.isSneaking() ? 2 : 1)));
										main.holdingentity.remove(p.getName());
										pass.setGravity(true);
										e.setCancelled(true);
									}
								}
							}
						}
					}
				}
			}
		}
	
		if(e.getClickedBlock() != null) {
			Block b = e.getClickedBlock();
			if(b.getType() == Material.SMITHING_TABLE && p.getInventory().getItemInMainHand() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(p.getInventory().getItemInMainHand().getType() != Material.AIR) {
					if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchants() || Methoden.hasCustomEnch(p.getInventory().getItemInMainHand())) {
						Methoden.openDisenchantInv(p, p.getInventory().getItemInMainHand());
						Methoden.calcDisenchanter(p, p.getOpenInventory().getTopInventory());
						e.setCancelled(true);
						
					}
				}
			}
		}
		
		if(e.getItem() != null) {
			ItemStack is = e.getItem();
			NBTItem nbis = new NBTItem(is);
			if(nbis.hasKey("magnet")) {
				e.setCancelled(true);
			}
			if(nbis.hasKey("dsu")) {
				e.setCancelled(true);
				
				if(e.getHand() == EquipmentSlot.HAND) {
					ItemStack targetitem = Methoden.getDSUItem(nbis, 0);
					ItemStack targetitem1 = Methoden.getDSUItem(nbis, 1);
					ItemStack targetitem2 = Methoden.getDSUItem(nbis, 2);
					ItemStack targetitem3 = Methoden.getDSUItem(nbis, 3);
					ItemStack targetitem4 = Methoden.getDSUItem(nbis, 4);
					
					if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
						for(int i = 0; i < p.getInventory().getSize(); i ++) {
							ItemStack invis = p.getInventory().getItem(i);
							if(invis != null && invis.getType() != Material.AIR) {
								if(invis.getType() == targetitem.getType()) {
									nbis.setInteger("counter0", nbis.getInteger("counter0")+invis.getAmount());
									p.getInventory().setItem(i, new ItemStack(Material.AIR));
									p.playSound(p.getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 0.1f);
								}
								if(invis.getType() == targetitem1.getType()) {
									nbis.setInteger("counter1", nbis.getInteger("counter1")+invis.getAmount());
									p.getInventory().setItem(i, new ItemStack(Material.AIR));
									p.playSound(p.getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 0.1f);
								}
								if(invis.getType() == targetitem2.getType()) {
									nbis.setInteger("counter2", nbis.getInteger("counter2")+invis.getAmount());
									p.getInventory().setItem(i, new ItemStack(Material.AIR));
									p.playSound(p.getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 0.1f);
								}
								if(invis.getType() == targetitem3.getType()) {
									nbis.setInteger("counter3", nbis.getInteger("counter3")+invis.getAmount());
									p.getInventory().setItem(i, new ItemStack(Material.AIR));
									p.playSound(p.getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 0.1f);
								}
								if(invis.getType() == targetitem4.getType()) {
									nbis.setInteger("counter4", nbis.getInteger("counter4")+invis.getAmount());
									p.getInventory().setItem(i, new ItemStack(Material.AIR));
									p.playSound(p.getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 0.1f);
								}
							}
						}
						p.getInventory().setItemInMainHand(nbis.getItem());
						
					}else {
						Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§2Deep Storage Backpack");
						
						inv.setItem(0, targetitem);
						inv.setItem(1, targetitem1);
						inv.setItem(2, targetitem2);
						inv.setItem(3, targetitem3);
						inv.setItem(4, targetitem4);
						
						p.openInventory(inv);
					}
				}
				
				
			}
		}
		
		if(e.getItem() != null) {
			ItemStack is = e.getItem();
			
			if(Tag.SHULKER_BOXES.isTagged(is.getType())) {
				if(is.getItemMeta() != null) {
					ItemMeta meta = is.getItemMeta();
					if(meta.hasDisplayName()) {
						if(meta.getDisplayName().contains("Rucksack") && meta.getDisplayName().contains("§a§l")) {
							if(meta.getLore() != null) {
								e.setCancelled(true);
								
								
								int size = 0;
								for(String s : meta.getLore()) {
									if(s.startsWith("§7§lSlots: ")) {
										size = Integer.parseInt(s.split(": ")[1]);
									}
								}
								if(size > 0) {
									Inventory inv = Bukkit.createInventory(p, size, meta.getDisplayName());
									p.openInventory(inv);
									
									NBTItem nbti = new NBTItem(is);
									if(nbti.hasKey("invcontent")) {
										String invdata = nbti.getString("invcontent");
										Inventory items = Methoden.fromBase64(invdata);
										inv.setContents(items.getContents());
									}
									main.backpack.put(p.getName(), inv);
									
									
								}
							
							}
						}
					}
				}
			}
		}
		if(e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			
			if(b.getType() == Material.NETHER_WART || b.getType() == Material.WHEAT || b.getType() == Material.BEETROOTS || b.getType() == Material.CARROTS || b.getType() == Material.POTATOES) {
				BlockData data = b.getBlockData();
				Ageable age = (Ageable) data;
				if(age.getMaximumAge() == age.getAge()) {
					if(b.getType() == Material.WHEAT) {
						((Ageable) data).setAge(0);
						b.setBlockData(data);
						b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.WHEAT));
						if(Methoden.chance(50)) {
							b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.WHEAT_SEEDS));
						}
						p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 0.5f, 1f);
					}
					if(b.getType() == Material.CARROTS) {
						((Ageable) data).setAge(0);
						b.setBlockData(data);
						b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.CARROT));
						if(Methoden.chance(50)) {
							b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.CARROT));
						}
						if(Methoden.chance(50)) {
							b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.CARROT));
						}
						p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 0.5f, 1f);
					}
					if(b.getType() == Material.POTATOES) {
						((Ageable) data).setAge(0);
						b.setBlockData(data);
						b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.POTATO));
						if(Methoden.chance(50)) {
							b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.POTATO));
						}
						if(Methoden.chance(50)) {
							b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.POTATO));
						}
						if(Methoden.chance(5)) {
							b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.POISONOUS_POTATO));
						}
						p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 0.5f, 1f);
					}
					if(b.getType() == Material.NETHER_WART) {
						((Ageable) data).setAge(0);
						b.setBlockData(data);
						b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.NETHER_WART));
						if(Methoden.chance(50)) {
							b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.NETHER_WART));
						}
						if(Methoden.chance(50)) {
							b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.NETHER_WART));
						}
						p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 0.5f, 1f);
					}
					if(b.getType() == Material.BEETROOTS) {
						((Ageable) data).setAge(0);
						b.setBlockData(data);
						b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.BEETROOT));
						if(Methoden.chance(50)) {
							b.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.BEETROOT_SEEDS));
						}
						p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 0.5f, 1f);
					}
				}
				
				if(p.getItemInHand() != null) {
					Material mat = p.getItemInHand().getType();
					
					if(mat == Material.DIAMOND_HOE || mat == Material.GOLDEN_HOE || mat == Material.IRON_HOE || mat == Material.STONE_HOE || mat == Material.WOODEN_HOE) {
						for(BlockFace neighborFace: main.NEIGHBORS) {
							Block block = b.getRelative(neighborFace);
							if(!main.scheduledBlocks.contains(block)) {
								if(block.getType() == Material.NETHER_WART || block.getType() == Material.WHEAT || block.getType() == Material.BEETROOTS || block.getType() == Material.CARROTS || block.getType() == Material.POTATOES) {	
									if(((Ageable) block.getBlockData()).getMaximumAge() == ((Ageable) block.getBlockData()).getAge()) {
										main.scheduledBlocks.add(block);
										Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> recallInteract(p, block), 1);
									}
								}
							}
						}
						
					}
				}
				
				
				  
			}
			
		
		}
	}
	public static void recallInteract(Player p, Block b) {
		PlayerInteractEvent e = new PlayerInteractEvent(p, Action.RIGHT_CLICK_BLOCK, p.getItemInHand(), b, BlockFace.UP, EquipmentSlot.HAND);
		Bukkit.getPluginManager().callEvent(e);
		main.scheduledBlocks.remove(b);
	}
}
