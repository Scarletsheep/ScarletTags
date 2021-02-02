package me.Scarletsheep.ScarletTags;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import me.Scarletsheep.ScarletTags.errors.MissingDependency;
import me.Scarletsheep.ScarletTags.errors.NicknameChangeNotAllowed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;
import java.util.List;

public class EventsListener implements Listener {
    private ProtocolManager protocolManager;

    public EventsListener() throws MissingDependency{
        super();
        try {
            protocolManager = ProtocolLibrary.getProtocolManager();
            listenForPackets();
        } catch (NullPointerException nullPointerException) {
            throw new MissingDependency();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Tag retrieval from database
        // Checks if player has a saved tag
        if (DataService.checkVariable(player.getUniqueId(), "tag")) {
            // Sets the player's display name to the saved tag
            String newTag = Utils.translateAlternateColorCodes(DataService.getVariable(player.getUniqueId(), "tag"));
            try {
                Utils.setPlayerTag(player, newTag);
            } catch (NicknameChangeNotAllowed error) {
                // TODO handle errors at login nametag setting
            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Utils.removePlayerNametagEntities(event.getPlayer());
    }


    public void listenForPackets() {
        protocolManager.addPacketListener(
                new PacketAdapter(Main.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        Player player = event.getPlayer();
                        player.sendMessage("hi!");
                        StructureModifier<Entity> entityStructureModifier = event.getPacket().getEntityModifier(event);
                        List<Field> fieldList = entityStructureModifier.getFields();

                        for (Field field : fieldList) {
                            player.sendMessage(field.getName());
                        }
                    }
                }
        );
    }
}
