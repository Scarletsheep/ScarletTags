package me.Scarletsheep.ScarletTags;

import me.Scarletsheep.ScarletTags.errors.NicknameChangeNotAllowed;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public class Utils {
    public static String translateAlternateColorCodes(String newTagWithCodes) {
        // Sets the player's display name to the new tag

        // Codes replacing with bukkit's color objects
        String newColoredTag = ChatColor.translateAlternateColorCodes('&', newTagWithCodes);

        // Reset object addition to end of tag
        newColoredTag = newColoredTag + ChatColor.RESET;

        return newColoredTag;
    }

    public static void setPlayerTag(Player player, String newTag) throws NicknameChangeNotAllowed {
        // Saving old display name for later
        String oldDisplayName = player.getDisplayName();

        // Nickname change check
        if (isNicknameChanging(player, newTag)) {
            if (!Main.config.getBoolean("AllowNicknameChange")) {
                throw new NicknameChangeNotAllowed();
            }
        }

        // Sets display name
        if (Main.config.getBoolean("AllowChatTagModification")) {
            player.setDisplayName(newTag);
        }
        // Sets tablist name
        if (Main.config.getBoolean("AllowTablistTagModification")) {
            player.setPlayerListName(newTag);
        }

        if (Main.config.getBoolean("UseArmorStandAsNameTag")) {
            // Removing previous Armor Stands
            List<Entity> passengers = player.getPassengers();
            for (Entity passenger : passengers) {
                if (passenger.getCustomName().equals(oldDisplayName)) {
                    player.removePassenger(passenger);
                    passenger.remove();
                }
            }

            // Setting Armor Stand as player's nametag
            Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

            // Armor Stand configuration
            entity.setCustomName(player.getDisplayName());
            entity.setGravity(false);
            entity.setInvulnerable(true);
            entity.setSilent(true);
            entity.setPersistent(true);

            ArmorStand armorStand = (ArmorStand) entity;

            armorStand.setInvisible(true);
            armorStand.setCollidable(false);
            armorStand.setSmall(true);
            entity.setCustomNameVisible(true);

            // Adding Armor Stand as passenger of player
            player.addPassenger(armorStand);
        }
    }

    public static void resetPlayerTag(Player player) {
        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
    }

    public static boolean isNicknameChanging(Player player, String newTag) {
        String baseOldTag = player.getName();
        String baseNewTag = ChatColor.stripColor(newTag);

        player.sendMessage(baseOldTag + " " + newTag);

        // Nickname change check
        if (baseOldTag.equals(baseNewTag)) {
            return false;
        }

        return true;
    }
}
