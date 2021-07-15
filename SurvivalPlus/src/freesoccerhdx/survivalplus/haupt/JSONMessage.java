package freesoccerhdx.survivalplus.haupt;

import org.bukkit.Bukkit;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;


public class JSONMessage {
    private JSONObject chatObject;

    @SuppressWarnings("unchecked")
	public JSONMessage(String text) {
        chatObject = new JSONObject();
        chatObject.put("text", text);
    }
    @SuppressWarnings("unchecked")
	public void addExtra(String text,JSONMessage.ClickType JSONClickType, String clicktext,JSONMessage.HoverType JSONHoverType, String hovertext){
    	JSONObject chatExtra = new JSONObject();
    	chatExtra.put("text", text);
    	  
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
    	new PacketPlayOutChat(ChatSerializer.a(chatObject.toJSONString()), ChatMessageType.CHAT, p.getUniqueId());
        //((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(chatObject.toJSONString())));
    }

    @Override
	public String toString() {
        return chatObject.toJSONString();
    }
    
    public static enum ClickType {
        RUN_COMMAND("run_command"),
        SUGGEST_COMMAND("suggest_command"),
        OPEN_URL("open_url");
        private final String type;

        ClickType(String type) {
            this.type = type;
        }

        public String getTypeString() {
            return type;
        }
    }
    public static enum HoverType {
        SHOW_TEXT("show_text"),
        SHOW_ITEM("show_item"),
        SHOW_ACHIEVEMENT("show_achievement");
        private final String type;

        HoverType(String type) {
            this.type = type;
        }

        public String getTypeString() {
            return type;
        }
    }
    
}
