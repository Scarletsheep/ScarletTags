package me.Scarletsheep.ScarletTags;

import me.Scarletsheep.ScarletTags.errors.NicknameChangeNotAllowed;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;

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

        // Entities set as nametag
        if (Main.config.getBoolean("UseEntitiesAsNametag")) {
            // Calling for previous tags removal
            removePlayerNametagEntities(player);

            // Armor Stand creation
            Entity marker = player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            ArmorStand armorStand = (ArmorStand) marker;

            armorStand.setCustomName(player.getDisplayName());
            armorStand.setGravity(false);
            armorStand.setSilent(true);
            armorStand.setPersistent(true);
            armorStand.setMarker(true);
            armorStand.setCollidable(false);
            armorStand.setInvisible(true);
            armorStand.setCustomNameVisible(true);

            // Slime creation
            Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.SLIME);
            Slime slime = (Slime) entity;

            slime.setPersistent(true);
            slime.setGravity(false);
            slime.setSilent(true);
            slime.setInvulnerable(true);
            slime.setInvisible(true);
            slime.setCollidable(false);
            slime.setAI(false);
            slime.setSize(1);

            // Player entities addition
            slime.addPassenger(armorStand);
            player.addPassenger(slime);

            // Entities save in database
            DataService.saveVariable(
                            player.getUniqueId(),
                            "armorStand",
                            armorStand.getUniqueId().toString());
            DataService.saveVariable(
                    player.getUniqueId(),
                    "slime",
                    slime.getUniqueId().toString());
        }
    }

    public static void removePlayerNametagEntities(Player player) {
        String armorStandId = "", slimeId = "";

        // Armor Stand check
        if(DataService.checkVariable(player.getUniqueId(), "armorStand")) {
            armorStandId = DataService.getVariable(player.getUniqueId(), "armorStand");
        }

        // Slime check
        if(DataService.checkVariable(player.getUniqueId(), "slime")) {
            slimeId = DataService.getVariable(player.getUniqueId(), "slime");
        }

        // Removing entities from global list to preserve eventual
        // player passengers that are not from ScarletTags
        List<LivingEntity> livingEntities = player.getWorld().getLivingEntities();
        for(LivingEntity entity : livingEntities) {
            // Armor Stand id comparison
            if (!armorStandId.equals("")) {
                if (entity.getUniqueId().toString().equals(armorStandId)) {
                    entity.remove();
                }
            }

            // Slime id comparison
            if (!slimeId.equals("")) {
                if (entity.getUniqueId().toString().equals(slimeId)) {
                    entity.remove();
                }
            }
        }

        // Database clear
        DataService.removeVariable(player.getUniqueId(), "armorStand");
        DataService.removeVariable(player.getUniqueId(), "slime");
    }

    public static void resetPlayerTag(Player player) {
        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
        removePlayerNametagEntities(player);
    }

    public static boolean isNicknameChanging(Player player, String newTag) {
        String baseOldTag = player.getName();
        String baseNewTag = ChatColor.stripColor(newTag);

        // Nickname change check
        if (baseOldTag.equals(baseNewTag)) {
            return false;
        }

        return true;
    }
}
