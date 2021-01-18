package me.Scarletsheep.ScarletTags.commands;

import me.Scarletsheep.ScarletTags.DataService;
import me.Scarletsheep.ScarletTags.Main;
import me.Scarletsheep.ScarletTags.Utils;
import me.Scarletsheep.ScarletTags.errors.NicknameChangeNotAllowed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stag implements CommandExecutor {
    // Called on command execution
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            try {
                switch (args[0]) {
                    case "set":
                        String newTag = Utils.translateAlternateColorCodes(args[1]);
                        Utils.setPlayerTag(player, newTag);

                        // New tag save
                        DataService.saveVariable(player.getUniqueId(), "tag", args[1]);
                        break;
                    case "reset":
                        Utils.resetPlayerTag(player);

                        // Old tag remove
                        DataService.removeVariable(player.getUniqueId(), "tag");
                        break;
                    case "config":
                        switch(args[1]) {
                            case "reload":
                                Main.plugin.reloadConfig();
                                player.sendMessage("Config reloaded.");
                                break;
                            default:
                                player.sendMessage("Type /stag help for help");
                                break;
                        }
                        break;
                    default:
                        player.sendMessage("Type /stag help for help");
                        break;
                }
            } catch (NicknameChangeNotAllowed error) {
                player.sendMessage("you are not allowed to modify your base nickname.");
            }

            return true;

        }

        return false;
    }
}
