package freesoccerhdx.survivalplus.haupt;

import java.awt.TextComponent;
import java.io.ByteArrayInputStream;





import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import de.tr7zw.nbtapi.NBTItem;
import freesoccerhdx.survivalplus.enchants.EnchantLimits;
import freesoccerhdx.survivalplus.npc.NPCHandler;
import freesoccerhdx.survivalplus.npc.NPCPlayer;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;

public class Methoden {
	
	
	
	public static boolean isItemStackValid(ItemStack is) {
		
		if(is != null) {
			if(is.getType() != Material.AIR) {
				if(is.getAmount() > 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static ItemStack getDSUItem(NBTItem nbitem,int i) {
		ItemStack targetitem = Methoden.item(Material.BEDROCK, 1, 0, "§cKein Item", new String[] {"§aAnzahl: 0"});
		
		String targetmat = nbitem.getString("material"+i);
		if(!targetmat.equals("null") && !targetmat.equals("")) {
			int targetcount = nbitem.getInteger("counter"+i);
			Material mat = Material.valueOf(targetmat);
			targetitem = Methoden.item(mat, 1, 0, null, new String[] {"§aAnzahl: "+targetcount});
		}
		
		return targetitem;
	}
	
	// TODO: dont know if this function is needed
	public static boolean isUsableToolss(ItemStack tool, Material block) {
	    net.minecraft.server.v1_16_R3.Block nmsBlock = org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers.getBlock(block);
	    if (nmsBlock == null) {
	        return false;
	    }
	    net.minecraft.server.v1_16_R3.IBlockData data = nmsBlock.getBlockData();
	    
	  
	    
	    return tool != null && tool.getType() != Material.AIR
	            && org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers.getItem(tool.getType()).canDestroySpecialBlock(data);
	}
	
	public static int minmax(int min, int max) {
		int r = 0;
		r = (int) (Math.random() * (max-min));
		return min + r;
	}
	
	
	public static List<String> randomNewWeaponEnchant() {
		List<String> lore = new ArrayList<>();
		Random r = new Random();
		
		
		
		if(Methoden.chance(33)) {
			lore.add("§7Wegschleudern "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Wegschleudern"))));
		}else if(Methoden.chance(66)) {
			lore.add("§7Heranziehen "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Heranziehen"))));
		}else {
			lore.add("§7Festhalten "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Festhalten"))));
		}
		
		if(r.nextBoolean()) {
			lore.add("§7Ausbesserung "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Ausbesserung"))));
		}
		if(r.nextBoolean()) {
			lore.add("§7Rüstungsdurchdringung "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Rüstungsdurchdringung"))));
		}
		if(r.nextBoolean()) {
			lore.add("§7Lebensraub "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Lebensraub"))));
		}
		if(r.nextBoolean()) {
			lore.add("§7Verlangsamung "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Verlangsamung"))));
		}
		if(r.nextBoolean()) {
			lore.add("§7Wither "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Wither"))));
		}
		if(r.nextBoolean()) {
			lore.add("§7Vergiftung "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Vergiftung"))));
		}
		if(r.nextBoolean()) {
			lore.add("§7Riesenschlag "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Riesenschlag"))));
		}
		if(r.nextBoolean()) {
			lore.add("§7Erstschlag "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Erstschlag"))));
		}
		if(r.nextBoolean()) {
			lore.add("§7Sturmschlag "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Sturmschlag"))));
		}
		
		return Sorter.sortLoreEnchants(lore);
	}

	public static Map<Enchantment, Integer> randomOldWeaponEnchant(Material ironSword) {
		HashMap<Enchantment, Integer> enchs = new HashMap<>();
		Random r = new Random();
		
		if(r.nextBoolean()) {
			enchs.put(Enchantment.DAMAGE_ALL,minmax(1, 5));
		}
		if(r.nextBoolean()) {
			enchs.put(Enchantment.FIRE_ASPECT,minmax(1, 5));
		}
		if(r.nextBoolean()) {
			enchs.put(Enchantment.LOOT_BONUS_MOBS,minmax(1, 5));
		}
		if(r.nextBoolean()) {
			enchs.put(Enchantment.MENDING,1);
		}
		
		return enchs;
	}
	
	public static Map<Enchantment, Integer> randomOldBowEnchant(Material mat) {
		HashMap<Enchantment, Integer> enchs = new HashMap<>();
		Random r = new Random();
		if(mat == Material.BOW) {
			if(r.nextBoolean()) {
				enchs.put(Enchantment.ARROW_DAMAGE,minmax(1, 5));
			}
			if(r.nextBoolean()) {
				enchs.put(Enchantment.ARROW_FIRE,minmax(1, 5));
			}
			if(r.nextBoolean()) {
				enchs.put(Enchantment.MENDING,1);
			}
		}else if(mat == Material.CROSSBOW) {
			if(r.nextBoolean()) {
				enchs.put(Enchantment.PIERCING,minmax(3, 5));
			}
			if(r.nextBoolean()) {
				enchs.put(Enchantment.QUICK_CHARGE,minmax(3, 5));
			}
			if(r.nextBoolean()) {
				enchs.put(Enchantment.MULTISHOT,1);
			}
			
		}
		
		return enchs;
	}

	
	public static List<String> randomNewBowEnchant() {
		List<String> lore = new ArrayList<>();

		Random r = new Random();
		// ausbesserung
		if(r.nextBoolean()) {
			lore.add("§7Ausbesserung "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Ausbesserung"))));
		}
		// explosion
		if(r.nextBoolean()) {
			lore.add("§7Explosion "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Explosion"))));
		}
		// donnerblitz
		if(r.nextBoolean()) {
			lore.add("§7Donnerblitz "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Donnerblitz"))));
		}
		// härte
		if(r.nextBoolean()) {
			lore.add("§7Härte "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Härte"))));
		}
		// schwebe
		if(r.nextBoolean()) {
			lore.add("§7Schwebe "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Schwebe"))));
		}
		// abprallen
		if(r.nextBoolean()) {
			lore.add("§7Abprallen "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Abprallen"))));
		}
		// Zielsuchend
		if(r.nextBoolean()) {
			lore.add("§7Zielsuchend "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Zielsuchend"))));
		}

		if(Methoden.chance(33)) {
			lore.add("§7Wegschleudern "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Wegschleudern"))));
		}else if(Methoden.chance(66)) {
			lore.add("§7Heranziehen "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Heranziehen"))));
		}else {
			lore.add("§7Festhalten "+Methoden.roemisch(minmax(1, EnchantLimits.getEnchantLimit("Festhalten"))));
		}
			
				
		
		return Sorter.sortLoreEnchants(lore);
	}
	
	
	
	
	public static void drawLaser(Location start, Location end, Double presi, float size, Color c) {
		    Vector v = end.toVector().subtract(start.toVector()).normalize();
		    Location act = start.clone();
		    for (int i = 0; i < 200; i++) {
		      act.getWorld().spawnParticle(Particle.REDSTONE, act.getX(), act.getY(), act.getZ(), 0, new Particle.DustOptions(c, size));
		      if (start.distance(act) >= start.distance(end))
		        break; 
		      act = act.add(v.clone().multiply(presi.doubleValue()));
		    } 
		  }
	public static Object getProjDataKey(HashMap<String, Object> map,String key){
		
		if(map.containsKey(key)) {
			return map.get(key);
		}
		
		return null;
	}
	
	public static Double getHardness(Material mat) {
		// TODO: dont know if strength now work
		return (double) CraftMagicNumbers.getBlock(mat).getBlockData().strength;
	}

	public static boolean isSlimeChunk(Player p, int x, int z){
		long seed = p.getWorld().getSeed();
    
		Random rnd = new Random(seed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 0x3AD8025F);
    
		return rnd.nextInt(10) == 0;
	}
	
	public static void calcDisenchanter(Player p,  Inventory inv) {
		ItemStack is = inv.getItem(4);
		if(is != null) {
			ItemMeta meta = is.getItemMeta();
			int slot = 9;
			
			ItemStack glasspane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE,1);
			ItemMeta nmeta = glasspane.getItemMeta();
			nmeta.setDisplayName("§7 §7");
			glasspane.setItemMeta(nmeta);
			
			for(int i = 0; i < 27; i++) {
				if(i != 4) {
					inv.setItem(i, glasspane);
				}
			}
			if(Methoden.hasCustomEnch(is)) {
				List<String> lore = meta.getLore();
				
				for(String s : lore) {
					if(s.startsWith("§7") && s.split(" ").length == 2) {
						ItemStack book = new ItemStack(Material.ENCHANTED_BOOK,1);
						ItemMeta bookmeta = book.getItemMeta();
						bookmeta.setDisplayName(s.replace("§7", "§7§e"));
						List<String> booklore = new ArrayList<>();
						booklore.add("§cLinksklick zum entfernen der Verzauberung!");
						int enchlvl = arabisch(s.split(" ")[1]);
						booklore.add("§eExperience: "+(13*enchlvl));
						bookmeta.setLore(booklore);
						book.setItemMeta(bookmeta);
						inv.setItem(slot, book);
						
						slot++;
					}
				}
			}
			if(meta.hasEnchants()) {
				for(Enchantment ench : meta.getEnchants().keySet()) {
					ItemStack book = new ItemStack(Material.ENCHANTED_BOOK,1);
					ItemMeta bookmeta = book.getItemMeta();
					bookmeta.setDisplayName(translateEnchantment(ench) +" "+ Methoden.roemisch(meta.getEnchantLevel(ench)));
					List<String> booklore = new ArrayList<>();
					booklore.add(ench.getName()+"");
					booklore.add("§cLinksklick zum entfernen der Verzauberung!");
					booklore.add("§eExperience "+(13*meta.getEnchantLevel(ench)));
					
					bookmeta.setLore(booklore);
					book.setItemMeta(bookmeta);
					inv.setItem(slot, book);
					
					slot++;
				}
			}
			p.updateInventory();
			
		}
		
	}
	
	public static boolean hasCustomLore(ItemStack is) {
		if(is != null) {
			if(is.getType() != null) {
				if(is.getItemMeta() != null) {
					if(is.getItemMeta().getLore() != null) {
						if(is.getItemMeta().getLore().size() > 0) {
							return true;
							
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean hasCustomEnch(ItemStack is) {
		if(is != null) {
			if(is.getType() != null) {
				if(is.getItemMeta() != null) {
					if(is.getItemMeta().getLore() != null) {
						for(String s : is.getItemMeta().getLore()) {
							if(s.startsWith("§7") && s.split(" ").length == 2) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
   public static void onBlockRemove(final Block oldBlock, long delay) {
        if (!Tag.LOGS.isTagged(oldBlock.getType()) && !Tag.LEAVES.isTagged(oldBlock.getType())) return;
        final String worldName = oldBlock.getWorld().getName();

        for (BlockFace neighborFace: main.NEIGHBORS) {
            final Block block = oldBlock.getRelative(neighborFace);
            if (!Tag.LEAVES.isTagged(block.getType())) continue;
            Leaves leaves = (Leaves)block.getBlockData();
            if (leaves.isPersistent()) continue;
            if (main.scheduledBlocks.contains(block)) continue;
            main.scheduledBlocks.add(block);
            Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> decay(block), delay);
        }
    }
	   
	 public static void decay(Block block) {
		  
	        if (!main.scheduledBlocks.remove(block)) return;
	        if (!Tag.LEAVES.isTagged(block.getType())) return;
	        Leaves leaves = (Leaves)block.getBlockData();
	        if (leaves.isPersistent()) return;
	        if (leaves.getDistance() < 7) return;
	        LeavesDecayEvent event = new LeavesDecayEvent(block);
	        Bukkit.getServer().getPluginManager().callEvent(event);
	        block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation().add(0.5, 0.5, 0.5), 8, 0.2, 0.2, 0.2, 0, block.getType().createBlockData());
	        
	        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.05f, 1.2f);
	        
	        block.breakNaturally();
	    }
	
	
	public static int cutWood(Player p, ItemStack axe, Location loc, Location started) {
		Block b = loc.getBlock();
		if(b.getType() == Material.ACACIA_LOG || b.getType() == Material.BIRCH_LOG || b.getType() == Material.DARK_OAK_LOG || b.getType() == Material.JUNGLE_LOG || b.getType() == Material.OAK_LOG || b.getType() == Material.SPRUCE_LOG) {
			loc.getBlock().breakNaturally(axe);
			axe.setDurability((short) (axe.getDurability()+1));
			
			if(axe.getDurability() > axe.getType().getMaxDurability()) {
				axe.setType(Material.AIR);
				p.setItemInHand(new ItemStack(Material.AIR));
			}
			
			int a = 1;
			
			if(loc.distance(started) <= 7.0 && axe.getType() != Material.AIR) {
				
				a += cutWood(p, axe, loc.clone().add(1, 0, 0),started);
				a += cutWood(p, axe, loc.clone().add(0, 1, 0),started);
				a += cutWood(p, axe, loc.clone().add(0, 0, 1),started);
				a += cutWood(p, axe, loc.clone().add(-1, 0, 0),started);
				a += cutWood(p, axe, loc.clone().add(0, -1, 0),started);
				a += cutWood(p, axe, loc.clone().add(0, 0, -1),started);
				
				a += cutWood(p, axe, loc.clone().add(-1, 1, -1),started);
				a += cutWood(p, axe, loc.clone().add(1, 1, -1),started);
				a += cutWood(p, axe, loc.clone().add(1, 1, 1),started);
				a += cutWood(p, axe, loc.clone().add(-1, 1, 1),started);
				
				a += cutWood(p, axe, loc.clone().add(0, 1, 1),started);
				a += cutWood(p, axe, loc.clone().add(1, 1, 0),started);
				a += cutWood(p, axe, loc.clone().add(-1, 1, 0),started);
				a += cutWood(p, axe, loc.clone().add(0, 1, -1),started);
				
			}
			
			return a;
		}
		return 0;
	}
	
	public static int getArmorEnchLevel(Player p, String s) {
		int i = 0;
		
		if(p.getInventory().getHelmet() != null) { i += getLoreEnchLevel(p.getInventory().getHelmet(), s); }
		if(p.getInventory().getChestplate() != null) { i += getLoreEnchLevel(p.getInventory().getChestplate(), s); }
		if(p.getInventory().getLeggings() != null) { i += getLoreEnchLevel(p.getInventory().getLeggings(), s); }
		if(p.getInventory().getBoots() != null) { i += getLoreEnchLevel(p.getInventory().getBoots(), s); }
		
		return i;
	}
	
	public static int getLoreEnchLevel(ItemStack is, String s) {
		if(is != null) {
			if(is.getItemMeta() != null) {
				if(is.getItemMeta().getLore() != null) {
					return getLoreEnchLevel(is.getItemMeta().getLore(), s);
				}
			}
		}
		return 0;
	}
	public static int getLoreEnchLevel(List<String> lore, String s) {
		for(String x : lore) {
			if(x.startsWith("§7"+s)) {
				return Methoden.arabisch(x.replace("§7"+s, ""));
			}
		}
		return 0;
	}
	
	public static boolean chance(double chance) {
		Double thischance = Math.random()*100;
		//Bukkit.broadcastMessage("chance: " + chance + " von " + thischance);
		if(thischance <= chance) {
			return true;
		}
		return false;
	}
	
	public static int getLoreEnchLvl(Double faktor, int min, int max) {
		int i = 0;
		
		i = (int) Math.floor(Math.random()*0.5*(faktor/2)*(max-min+1) +min);
		
		
		i = Math.max(min, Math.min(i, max));
		
		return i;
	}
	
	public static boolean hasLoreEnch(ItemStack is, String s) {
		if(is != null) {
			if(is.getItemMeta() != null) {
				if(is.getItemMeta().getLore() != null) {
					return hasLoreEnch(is.getItemMeta().getLore(), s);
				}
			}
		}
		return false;
	}
	public static boolean hasLoreEnch(List<String> lore, String s) {
		for(String x : lore) {
			if(x.startsWith("§7"+s)) {
				return true;
			}
		}
		
		
		return false;
	}

	public static boolean InventarNichtVoll(Player p){
		
		for(int i = 0; i < 36; i ++){
			if(p.getInventory().getItem(i) == null){
				return true;
			}
		}
		
		return false;
	}
	
	public static void sendActionMessage(Player player, String text){
        CraftPlayer p = (CraftPlayer)player;
        
      //  IChatBaseComponent cbc = ChatSerializer.a("{\"text\":\""+ text + "\"}");
        
        //PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc);
        //new PacketPlayOutChat(ichatbasecomponent, ChatMessageType.)
        
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText(text));
        
       
       // p.getHandle().playerConnection.sendPacket(ppoc);
	}
	

	
	public static ItemStack getBuch(String autor, String title, String[] lore, String[] seiten){
		
		  ItemStack item = item(Material.WRITTEN_BOOK, 1, (byte) 0, title, lore);
			BookMeta itemmeta = (BookMeta) item.getItemMeta();
			itemmeta.setAuthor(autor);
			itemmeta.setTitle(title);
			for(String s : seiten){
				itemmeta.addPage(s);
			}
			item.setItemMeta(itemmeta);

		return item;
	}

	// 
	public static void sendRawMessage(Player p, String text){
		//IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a(nachricht);
	    //PacketPlayOutChat packet = new PacketPlayOutChat(comp);
	    
	    p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.CHAT, net.md_5.bungee.api.chat.TextComponent.fromLegacyText(text));
	    
	    //((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	
	public static Double round(Double wert){
		wert = wert*100;
		wert = (double) Math.round(wert);
		wert = wert/100;
		
		return wert;
	}
	public static Location getWarp(String name) {
		List<String> warps = getWarpList();
		
		if(warps.contains(name)){
			File file = new File("plugins/warps",name+".yml");
			if(file.exists()){
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				try{
					return (Location) cfg.get("loc");
				}catch(Exception ex){
				}
			}else{
				removeWarp(name);
			}
		
		}
		
		return null;
	}
	public static boolean removeWarp(String name) {
		List<String> warps = getWarpList();
		
		if(warps.contains(name)){
			File file = new File("plugins/warps",name+".yml");
			if(file.exists()){
				file.delete();
				return true;
			}
		
		}
		
		return false;
	}
	public static boolean addWarp(String name, Location loc) {
		List<String> warps = getWarpList();
		
		if(!warps.contains(name)){
			File file = new File("plugins/warps",name+".yml");
			
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			cfg.set("loc", loc);
			
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	public static List<String> getWarpList() {
		File ordner = new File("plugins/warps/");
		List<String> warps = new ArrayList<>();
		
		if(!ordner.exists()){
			ordner.mkdir();
		}

		for(File file : ordner.listFiles()){
			if(file.getName().endsWith(".yml")){
				warps.add(file.getName().replace(".yml", ""));
			}
		}
		
		
		return warps;
	}

	public static ItemStack LeatherItem(Material mat, int anzahl, int data, String name, Color col, String[] lore) {
		ItemStack item = item(mat, anzahl, data, name, lore);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(col);
		item.setItemMeta(meta);
		
		return item;
	}

	public static ItemStack item(Material mat, int anzahl, int data, String name, String[] lore) {
		ItemStack item = new ItemStack(mat,anzahl,(byte)data);
		
		ItemMeta meta = item.getItemMeta();
		if(name != null){
			meta.setDisplayName(name);
		}
		if(lore != null){
			List<String> lorens = new ArrayList<>();
			for(String s : lore){
				lorens.add(s);
			}
			meta.setLore(lorens);
		}
		
		item.setItemMeta(meta);
		
		return item;
	}
	public static ItemStack item(Material mat, int anzahl, int data, String name, List<String> lore) {
		ItemStack item = new ItemStack(mat,anzahl,(byte)data);
		ItemMeta meta = item.getItemMeta();
		if(name != null){
			meta.setDisplayName(name);
		}
		if(lore != null){
			List<String> lorens = new ArrayList<>();
			for(String s : lore){
				lorens.add(s);
			}
			meta.setLore(lorens);
		}
		
		item.setItemMeta(meta);
		
		return item;
	}
	public static ItemStack kopf(String playername, String anzeigename,int amount, String[] lore) {
		ItemStack item = item(Material.PLAYER_HEAD, amount, 3, anzeigename, lore);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		
		meta.setOwner(playername);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack kopf(String playername, String anzeigename,int amount, List<String> lore) {
		ItemStack item = item(Material.PLAYER_HEAD, amount, 3, anzeigename, lore);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(playername);
		item.setItemMeta(meta);
		return item;
	}
	public static Inventory changeInventoryName(Inventory inv, String newName) {
		Inventory newInv = Bukkit.createInventory(null, inv.getSize(), newName);
		
		for(int i = 0; i < inv.getSize(); i ++){
			if(inv.getItem(i) != null){
				newInv.setItem(i,inv.getItem(i));
			}
		}
		return newInv;
	}
	 
	public static String getMoneyString(long value){
		String money = "";
		
		if(value > 100000000){
			money = value/10000000 + " Mio Gulden";
		}else if(value > 100000){
			money = value/100 + " k Gulden";
		}
		
		
		return money;
	}
	
	public static String roemisch(int i){
		String text = "";
		int wert = i;
		if(wert > 4000){
			text = "...";
			wert = 0;
		}
		while(wert > 0){
			if(wert >= 1000){
				text = text + "M";
				wert -= 1000;
			}else if(wert >= 500){
				text = text + "D";
				wert -= 500;
			}else if (wert >= 100){
				text = text + "C";
				wert -= 100;
			}else if(wert >= 50){
				text = text + "L";
				wert -= 50;
			}else if (wert >= 10){
				text = text + "X";
				wert -= 10;
			}else if (wert >= 5){
				text = text + "V";
				wert -= 5;
			}else if (wert >= 1){
				text = text + "I";
				wert--;
			}
		}
		text = text.replaceAll("DCCCC", "CM");
		text = text.replaceAll("CCCC", "CD");
		text = text.replaceAll("LXXXX", "XC");
		text = text.replaceAll("XXXX", "XL");
		text = text.replaceAll("VIIII", "IX");
		text = text.replaceAll("IIII", "IV");
		
		return text;
	}
	
	public static int arabisch(String s){
		int zahl = 0;
		s = s.replaceAll("CM", "DCCCC");
		s = s.replaceAll("CD", "CCCC");
		s = s.replaceAll("XC", "LXXXX");
		s = s.replaceAll("XL", "XXXX");
		s = s.replaceAll("IX", "VIIII");
		s = s.replaceAll("IV", "IIII");
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if(c == 'M'){
				zahl += 1000;
			}
			if(c == 'D'){
				zahl += 500;
			}
			if(c == 'C'){
				zahl += 100;
			}
			if(c == 'L'){
				zahl += 50;
			}
			if(c == 'X'){
				zahl += 10;
			}
			if(c == 'V'){
				zahl += 5;
			}
			if(c == 'I'){
				zahl++;
			}
		}
		return zahl;
	}
	public static String toBase64(List<ItemStack> items) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			// Write the size of the inventory
			dataOutput.writeInt(items.size());
			// Save every element in the list
			for (int i = 0; i < items.size(); i++) {
				dataOutput.writeObject(items.get(i));
			}
			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}
	public static String toBase64(Inventory inventory) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			// Write the size of the inventory
			dataOutput.writeInt(inventory.getSize());
			// Save every element in the list
			for (int i = 0; i < inventory.getSize(); i++) {
				dataOutput.writeObject(inventory.getItem(i));
			}
			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}
	public static Inventory fromBase64(String data) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			int size = dataInput.readInt();
			int diff = 0;
			while ((size+diff)%9!=0)
				diff++;
			Inventory inventory = Bukkit.getServer().createInventory(null, size+diff);
			// Read the serialized inventory
			for (int i = 0; i < size; i++) {
				inventory.setItem(i, (ItemStack) dataInput.readObject());
			}
			dataInput.close();
			return inventory;
		} catch (Exception e) {
			try {
				throw new IOException("Unable to decode class type.", e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	public static String removeChatColorCodes(String text){
		text = text.replace("§0", "");
		text = text.replace("§1", "");
		text = text.replace("§2", "");
		text = text.replace("§3", "");
		text = text.replace("§4", "");
		text = text.replace("§5", "");
		text = text.replace("§6", "");
		text = text.replace("§7", "");
		text = text.replace("§8", "");
		text = text.replace("§9", "");
		text = text.replace("§a", "");
		text = text.replace("§b", "");
		text = text.replace("§c", "");
		text = text.replace("§d", "");
		text = text.replace("§e", "");
		text = text.replace("§f", "");
	
		text = text.replace("§n", "");
		text = text.replace("§m", "");
		text = text.replace("§o", "");
		text = text.replace("§l", "");
		text = text.replace("§k", "");

		return text;
	}
	public static String translateChatColorCodes(String text){
		text = text.replace("&0", "§0");
		text = text.replace("&1", "§1");
		text = text.replace("&2", "§2");
		text = text.replace("&3", "§3");
		text = text.replace("&4", "§4");
		text = text.replace("&5", "§5");
		text = text.replace("&6", "§6");
		text = text.replace("&7", "§7");
		text = text.replace("&8", "§8");
		text = text.replace("&9", "§9");
		text = text.replace("&a", "§a");
		text = text.replace("&b", "§b");
		text = text.replace("&c", "§c");
		text = text.replace("&d", "§d");
		text = text.replace("&e", "§e");
		text = text.replace("&f", "§f");
	
		text = text.replace("&n", "§n");
		text = text.replace("&m", "§m");
		text = text.replace("&o", "§o");
		text = text.replace("&l", "§l");
		text = text.replace("&k", "§k");

		return text;
	}
	public static String balken(Double d){
		if (d<0)
			d = 0.0;
		double d2 = 100 - d;
		ChatColor c = ChatColor.WHITE;
		if (d<=20){
			c = ChatColor.DARK_RED;
		}else if(d<=40){
			c = ChatColor.RED;
		}else if(d<=50){
			c = ChatColor.GOLD;
		}else if(d<=60){
			c = ChatColor.YELLOW;
		}else if(d<=80){
			c = ChatColor.GREEN;
		}else if(d<=110){
			c = ChatColor.DARK_GREEN;
		}else{
			c = ChatColor.DARK_AQUA;
		}
		String at = "";
		while (d2>10){
			d2-=10;
			at = at + "█";
		}
		String antitext = "";
		if (d2>=10){
			antitext = "█";
		}else if (d2>=8.75){
			antitext = "▉";
		}else if (d2>=7.5){
			antitext = "▊";
		}else if (d2>=6.25){
			antitext = "▋";
		}else if (d2>=5){
			antitext = "▌";
		}else if (d2>=3.75){
			antitext = "▌";
		}else if (d2>=2.5){
			antitext = "▎";
		}else if (d2>=1.25){
			antitext = "▏";
		}else{
			antitext = "";
		}
		String text = ChatColor.BLUE+"["+c;
		for(Integer i=1;i<=10;i++){
			  if (d>=10){
				  text = text +"█";
				  d-=10;
			  }else if (d>=8.75){
				  text = text +"▉";
				  d-=8.75;
			  }else if (d>=7.5){
				  text = text +"▊";
				  d-=8.75;
			  }else if (d>=6.25){
				  text = text +"▋";
				  d-=6.25;
			  }else if (d>=5){
				  text = text +"▌";
				  d-=5;
			  }else if (d>=3.75){
				  text = text +"▌";
				  d-=3.75;
			  }else if (d>=2.5){
				  text = text +"▎";
				  d-=2.5;
			  }else if (d>=1.25){
				  text = text +"▏";
				  d-=1.25;
			  }
		}
		text = text + ChatColor.BLACK + antitext + at + ChatColor.BLUE+"]";
		return text;
	}
	public static void openDisenchantInv(Player p, ItemStack is) {
		Inventory inv = Bukkit.createInventory(p, 27, "§3§lDisenchanter");
		ItemStack glasspane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE,1);
		ItemMeta meta = glasspane.getItemMeta();
		meta.setDisplayName("§7 §7");
		glasspane.setItemMeta(meta);
		for(int i = 0; i < 27; i++) {
			inv.setItem(i, glasspane);
		}
		inv.setItem(4, is);
		
		p.openInventory(inv);
	}
	public static String translateEnchantment(Enchantment ent){
		String name = ent.getKey().toString();
		
		if (ent.getKey().equals(Enchantment.ARROW_DAMAGE.getKey()))
			name = "Stärke";
		if (ent.getKey().equals(Enchantment.MENDING.getKey()))
			name = "Reparatur";
		if (ent.getKey().equals(Enchantment.ARROW_FIRE.getKey()))
			name = "Flamme";
		if (ent.getKey().equals(Enchantment.ARROW_INFINITE.getKey()))
			name = "Unendlichkeit";
		if (ent.getKey().equals(Enchantment.ARROW_KNOCKBACK.getKey()))
			name = "Schlag";
		if (ent.getKey().equals(Enchantment.DAMAGE_ALL.getKey()))
			name = "Schärfe";
		if (ent.getKey().equals(Enchantment.DAMAGE_ARTHROPODS.getKey()))
			name = "Nemesis der Gliederfüßer";
		if (ent.getKey().equals(Enchantment.DAMAGE_UNDEAD.getKey()))
			name = "Bann";
		if (ent.getKey().equals(Enchantment.DEPTH_STRIDER.getKey()))
			name = "Wasserläufer";
		if (ent.getKey().equals(Enchantment.DIG_SPEED.getKey()))
			name = "Effizienz";
		if (ent.getKey().equals(Enchantment.DURABILITY.getKey()))
			name = "Haltbarkeit";
		if (ent.getKey().equals(Enchantment.FIRE_ASPECT.getKey()))
			name = "Verbrennung";
		if (ent.getKey().equals(Enchantment.KNOCKBACK.getKey()))
			name = "Rückstoß";
		if (ent.getKey().equals(Enchantment.LOOT_BONUS_BLOCKS.getKey()))
			name = "Glück";
		if (ent.getKey().equals(Enchantment.LOOT_BONUS_MOBS.getKey()))
			name = "Plünderung";
		if (ent.getKey().equals(Enchantment.LUCK.getKey()))
			name = "Glück des Meeres";
		if (ent.getKey().equals(Enchantment.LURE.getKey()))
			name = "Köder";
		if (ent.getKey().equals(Enchantment.OXYGEN.getKey()))
			name = "Atmung";
		if (ent.getKey().equals(Enchantment.PROTECTION_ENVIRONMENTAL.getKey()))
			name = "Schutz";
		if (ent.getKey().equals(Enchantment.PROTECTION_EXPLOSIONS.getKey()))
			name = "Explosionsschutz";
		if (ent.getKey().equals(Enchantment.PROTECTION_FALL.getKey()))
			name = "Federfall";
		if (ent.getKey().equals(Enchantment.PROTECTION_FIRE.getKey()))
			name = "Feuerschutz";
		if (ent.getKey().equals(Enchantment.PROTECTION_PROJECTILE.getKey()))
			name = "Schussicher";
		if (ent.getKey().equals(Enchantment.SILK_TOUCH.getKey()))
			name = "Behutsamkeit";
		if (ent.getKey().equals(Enchantment.THORNS.getKey()))
			name = "Dornen";
		if (ent.getKey().equals(Enchantment.WATER_WORKER.getKey()))
			name = "Wasseraffinität";
		if (ent.getKey().equals(Enchantment.SWEEPING_EDGE.getKey()))
			name = "Schwungkraft";
		return name;
	}

	public static String removeColorCodes(String str) {
		
		for(ChatColor cc : ChatColor.values()) {
			str = str.replaceAll(""+cc, "");
		}
		return str;
	}






	
}
