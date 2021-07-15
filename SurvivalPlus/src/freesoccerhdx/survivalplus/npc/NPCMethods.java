package freesoccerhdx.survivalplus.npc;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;

public class NPCMethods {
	
	public static NPCPlayer getNPCOfKlon(Entity klon) {
		for(NPCPlayer npc : NPCHandler.getRegistered()) {
			if(npc.hasKlon()) {
				if(npc.getKlon().equals(klon)) {
					return npc;
				}
			}
		}
		return null;
	}
	public static boolean isKlonOfNPC(Entity klon) {
		for(NPCPlayer npc : NPCHandler.getRegistered()) {
			if(npc.hasKlon()) {
				if(npc.getKlon().equals(klon)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static String toBase64(ItemStack is) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			// Save every element in the list
			
			dataOutput.writeObject(is);
			
			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}
	
	public static ItemStack fromBase64(String data) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

			ItemStack is = (ItemStack) dataInput.readObject();
			
			dataInput.close();
			return is;
		} catch (Exception e) {
			try {
				throw new IOException("Unable to decode class type.", e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	public static org.bukkit.inventory.ItemStack getBukkitItemStack(net.minecraft.server.v1_16_R3.ItemStack craftitemstack) {
		return CraftItemStack.asBukkitCopy(craftitemstack); 
	}
	public static net.minecraft.server.v1_16_R3.ItemStack getCraftItemStack(org.bukkit.inventory.ItemStack bukkititemstack){
		return CraftItemStack.asNMSCopy(bukkititemstack);
	}
	
	public static void setValue(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		}catch(Exception ex) {
		}
	}
	
	public static Object getValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		}catch(Exception ex) {
			return null;
		}
	}
	
	
	public static void destroyKlon(Player p, Entity ent) {
		PacketPlayOutEntityDestroy ppoed = new PacketPlayOutEntityDestroy(((CraftEntity) ent).getHandle().getId());
		sendPacket(p, ppoed);
	}
	
	public static void sendPacket(Player p, Packet<?> packet) {
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		ep.playerConnection.sendPacket(packet);
	}

	 public static Object getPrivateField(String fieldName, Class clazz, Object object)
	    {
	        Field field;
	        Object o = null;

	        try
	        {
	            field = clazz.getDeclaredField(fieldName);

	            field.setAccessible(true);

	            o = field.get(object);
	        }
	        catch(NoSuchFieldException e)
	        {
	            e.printStackTrace();
	        }
	        catch(IllegalAccessException e)
	        {
	            e.printStackTrace();
	        }

	        return o;
	    }


}
