package org.plugin.yunxi.pluginMain;

import org.plugin.yunxi.color.CmdColor;
import org.plugin.yunxi.command.Reload;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.yunxi.listening.Listening;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    private File message;
    private File item;
    private FileConfiguration messageConfig;
    private FileConfiguration itemConfig;
    private boolean vault;
    private boolean point;
    private boolean papi;
    public Economy econ = null;
    private String  plugin;
    CmdColor cmd = new CmdColor();
    @Override
    public void onEnable() {
        System.out.println();
        if (Integer.parseInt(getServer().getBukkitVersion().substring(2, 4)) > 12) {
            saveDefaultConfig();
            createMessage();
            createItemName();
            reloadConfig();
            Bukkit.getPluginManager().registerEvents(new Listening(),this);
            getCommand("gfreload").setExecutor(new Reload());
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                RegisteredServiceProvider<Economy> r = this.getServer().getServicesManager().getRegistration(Economy.class);
                if (r != null) {
                    this.econ = r.getProvider();
                }
                this.vault = true;
                getLogger().info(cmd.getCyan() + "Vault v" + getServer().getPluginManager().getPlugin("Vault").getDescription().getVersion() + "已兼容" + cmd.getReset());
            } else {
                getLogger().info(cmd.getRed() + "Vault未成功兼容" + cmd.getReset());
                this.vault = false;
            }
            if (getServer().getPluginManager().getPlugin("PlayerPoints") != null) {
                this.point = true;
                getLogger().info(cmd.getCyan() + "PlayerPoints v" + getServer().getPluginManager().getPlugin("PlayerPoints").getDescription().getVersion() + "已兼容" + cmd.getReset());
            } else {
                getLogger().info(cmd.getRed() + "PlayerPoints未成功兼容" + cmd.getReset());
                this.point = false;
            }
            if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                this.papi = true;
                getLogger().info(cmd.getCyan() + "PlaceholderAPI v" + getServer().getPluginManager().getPlugin("PlaceholderAPI").getDescription().getVersion() + "已兼容" + cmd.getReset());
            } else {
                getLogger().info(cmd.getRed() + "PlaceholderAPI未成功兼容" + cmd.getReset());
                this.papi = false;
            }
        } else {
            getLogger().info(cmd.getRed() + "GoFishing已检测到该服务端版本过低，已成功卸载" + cmd.getReset());
            Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("GoFishing"));
        }
    }
    public boolean getvault () {
        return this.vault;
    }
    public boolean getpoint () {
        return this.point;
    }
    public boolean getpapi () {
        return this.papi;
    }
    public FileConfiguration getMessage(){
        return this.messageConfig;
    }
    public FileConfiguration getitemName(){
        return this.itemConfig;
    }
    public void createMessage(){
        message = new File(getDataFolder(),"message.yml");
        if (!message.exists()) {
            message.getParentFile().mkdirs();
            saveResource("message.yml", false);
        }
        messageConfig = new YamlConfiguration();
        try{
            messageConfig.load(message);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void createItemName(){
        item = new File(getDataFolder(),"itemTranslations.yml");
        if (!item.exists()) {
            item.getParentFile().mkdirs();
            saveResource("itemTranslations.yml", false);
        }
        itemConfig = new YamlConfiguration();
        try{
            itemConfig.load(item);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
