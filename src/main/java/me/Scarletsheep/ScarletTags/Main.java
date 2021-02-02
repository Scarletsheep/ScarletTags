package me.Scarletsheep.ScarletTags;
// Bukkit import

import me.Scarletsheep.ScarletTags.commands.Stag;
import me.Scarletsheep.ScarletTags.errors.MissingDependency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static FileConfiguration config ;
    public static Plugin plugin;

    @Override
    public void onEnable() {
        // File config creation if not present
        this.saveDefaultConfig();

        // Global plugin variable setting
        plugin = this;

        // Config load
        config = getConfig();

        // Database load
        DataService.getData();

        // Registering commands
        this.getCommand("stag").setExecutor(new Stag());

        // Event registering
        try {
            this.getServer().getPluginManager().registerEvents(new EventsListener(), this);
        } catch (MissingDependency missingDependency) {
            Main.plugin.getLogger().severe(
                    "ProtocolLib is not present! It is needed for ScarletTags to function correctly"
            );
            Main.plugin.getLogger().severe(
                    "Disabling plugin"
            );
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable(){
        // Database save
        DataService.saveData();
    }

    public void reloadConfigFile() {
        config = getConfig();
        plugin = this;
    }
}
