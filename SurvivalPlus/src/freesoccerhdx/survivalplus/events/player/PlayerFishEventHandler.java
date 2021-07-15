package freesoccerhdx.survivalplus.events.player;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import freesoccerhdx.survivalplus.haupt.Methoden;

public class PlayerFishEventHandler implements Listener {
	
	
	@EventHandler
	public void PlayerFish(PlayerFishEvent e) {
		Player p = e.getPlayer();
		//LootTable t1 = Bukkit.getServer().getLootTable(NamespacedKey.minecraft("gameplay/fishing/fish"));

		
		/*
		Builder lc = new LootContext.Builder(p.getLocation());

		lc.lootingModifier(LootContext.DEFAULT_LOOT_MODIFIER);
		
		WorldServer worldServer = ((CraftWorld) p.getWorld()).getHandle();
		net.minecraft.server.v1_15_R1.LootTableInfo.Builder lti = new LootTableInfo.Builder(worldServer);
		
		//org.bukkit.craftbukkit.v1_15_R1.CraftLootTable clt = new org.bukkit.craftbukkit.v1_15_R1.CraftLootTable(NamespacedKey.randomKey(), t1); 
				//.getHandle();
		
		LootContextParameterSet lcps = new LootContextParameter<>(MinecraftKey.a(""));
		Collection<ItemStack> fuckingitems = net.minecraft.server.v1_15_R1.LootTable.b().b().populateLoot(lti.build(lcps));
		
		p.sendMessage(lc.build().getLocation().toString());
		Collection<ItemStack> is = t1.populateLoot(new Random(), lc.build());
		
		for(ItemStack iss : is) {
			p.getWorld().dropItemNaturally(p.getLocation(), iss);
		}
		*/
		
		if(e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
			if(p.getInventory().getItemInMainHand() != null) {
				int doublefish = Methoden.getLoreEnchLevel(p.getItemInHand(), "Doppelfischen");
				if(doublefish > 0) {
					Set<Material> fish = new HashSet<>();
					fish.add(Material.PUFFERFISH);
					fish.add(Material.TROPICAL_FISH);
					fish.add(Material.COD);
					fish.add(Material.SALMON);
					fish.add(Material.PUFFERFISH);
					fish.add(Material.NAME_TAG);
					fish.add(Material.IRON_INGOT);
					fish.add(Material.GOLD_INGOT);
					fish.add(Material.DIAMOND);
					fish.add(Material.EMERALD);
					fish.add(Material.COAL);
					
					//Tag<Keyed>.ITEMS_FISHES
					for(int i = 0; i < doublefish; i ++) {
						Material mat = (Material) fish.toArray()[(int) Math.floor(Math.random()*fish.size())];
						p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(mat,1));
					}
				}
			}
		}
	}

}
