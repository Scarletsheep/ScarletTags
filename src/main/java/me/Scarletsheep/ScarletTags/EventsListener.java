package me.Scarletsheep.ScarletTags;

import me.Scarletsheep.ScarletTags.errors.NicknameChangeNotAllowed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventsListener implements Listener {
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
}
