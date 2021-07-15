package de.freesoccerhdx.testingstuff.main.utils;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutChat;



public class JSONMessage {
    private JSONObject chatObject;

    @SuppressWarnings("unchecked")
	public JSONMessage(String text) {
        chatObject = new JSONObject();
        chatObject.put("text", text);
    }
    
    public void addExtra(String text,JSONClickType JSONClickType, String clicktext,JSONHoverType JSONHoverType, String hovertext, Color color){
    	addExtra(text, JSONClickType, clicktext, JSONHoverType, hovertext, color, null);
    }
    
    public void addExtra(String text,JSONClickType JSONClickType, String clicktext,JSONHoverType JSONHoverType, String hovertext){
    	addExtra(text, JSONClickType, clicktext, JSONHoverType, hovertext, Color.WHITE, null);
    }
    
    public void addExtra(String text,JSONClickType JSONClickType, String clicktext,JSONHoverType JSONHoverType, String hovertext, List<JSONStyleElement> styleelements){
    	addExtra(text, JSONClickType, clicktext, JSONHoverType, hovertext, null, styleelements);
    }
    
    public void addExtra(String text,JSONClickType JSONClickType, String clicktext,JSONHoverType JSONHoverType, String hovertext, JSONStyleElement[] styleelements){
    	addExtra(text, JSONClickType, clicktext, JSONHoverType, hovertext, null, Arrays.asList(styleelements));
    }
    public void addExtra(String text,JSONClickType JSONClickType, String clicktext,JSONHoverType JSONHoverType, String hovertext, JSONStyleElement[] styleelements, Color color){
    	addExtra(text, JSONClickType, clicktext, JSONHoverType, hovertext, color, Arrays.asList(styleelements));
    }
    
    @SuppressWarnings("unchecked")
	public void addExtra(String text,JSONClickType JSONClickType, String clicktext, JSONHoverType JSONHoverType, String hovertext, Color color, List<JSONStyleElement> styleelements){
    	JSONObject chatExtra = new JSONObject();
    	chatExtra.put("text", text);
    	
    	if(color != null) {
    		String hexcolor = String.format("#%06x", color.getRGB() & 0xFFFFFF);
    		chatExtra.put("color", hexcolor);
    	}
    	if(styleelements != null) {
    		for(JSONStyleElement style : styleelements) {
    			chatExtra.put(style.toString().toLowerCase(), true);
    		}
    	}
    	/*
    	chatExtra.put("bold", true);
    	chatExtra.put("italic", false);
    	chatExtra.put("underlined", false);
    	chatExtra.put("strikethrough", true);
    	chatExtra.put("obfuscated", true);
    	*/
    
    	if(JSONClickType != null){
    		JSONObject clickEvent = new JSONObject();
         	clickEvent.put("action", JSONClickType.getTypeString());
         	clickEvent.put("value", clicktext);
         	chatExtra.put("clickEvent", clickEvent);
    	}
    	if(JSONHoverType != null){
    		JSONObject hoverEvent = new JSONObject();
         	hoverEvent.put("action", JSONHoverType.getTypeString());
         	hoverEvent.put("value", hovertext);
         	chatExtra.put("hoverEvent", hoverEvent);
    	}
    	
    	if(!chatObject.containsKey("extra")){
    		chatObject.put("extra", new JSONArray());
    	}
    	JSONArray extra = (JSONArray) chatObject.get("extra");
    	extra.add(chatExtra);
    	chatObject.put("extra", extra);
    	
    }
    public void sendToAllPlayers(){
    	for(Player p : Bukkit.getOnlinePlayers()){
    		sendToPlayer(p);
    	}
    }
    public void sendToPlayer(Player p) {
    	//PacketPlayOutChat ppoc = new PacketPlayOutChat(ChatSerializer.a(chatObject.toJSONString()), ChatMessageType.CHAT, p.getUniqueId());
    	PacketPlayOutChat ppoc = new PacketPlayOutChat(ChatSerializer.a(chatObject.toJSONString()), ChatMessageType.a, p.getUniqueId());
    	((CraftPlayer) p).getHandle().b.sendPacket(ppoc);
        //((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(chatObject.toJSONString())));
    }

    @Override
	public String toString() {
    	
        return chatObject.toJSONString();
    }
    
    public static enum JSONStyleElement{
    	BOLD, ITALIC, UNDERLINED, STRIKETHROUGH, OBFUSCATED;

    }
    
    public static enum JSONClickType {
        RUN_COMMAND("run_command"),
        SUGGEST_COMMAND("suggest_command"),
        OPEN_URL("open_url");
        private final String type;

        JSONClickType(String type) {
            this.type = type;
        }

        public String getTypeString() {
            return type;
        }
    }
    public static enum JSONHoverType {
        SHOW_TEXT("show_text"),
        SHOW_ITEM("show_item"),
        SHOW_ACHIEVEMENT("show_achievement");
        private final String type;

        JSONHoverType(String type) {
            this.type = type;
        }

        public String getTypeString() {
            return type;
        }
    }
	public JSONObject getMessageObject() {
		return chatObject;
	}
    
}
