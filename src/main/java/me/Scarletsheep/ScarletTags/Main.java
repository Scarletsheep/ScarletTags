package me.Scarletsheep.ScarletTags;
// Bukkit import

import me.Scarletsheep.ScarletTags.commands.Stag;
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
        this.getServer().getPluginManager().registerEvents(new EventsListener(), this);
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
