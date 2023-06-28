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
    private File customConfigFile;
    private FileConfiguration customConfig;
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
            reloadConfig();
            Bukkit.getPluginManager().registerEvents(new Listening(),this);
            getCommand("gfreload").setExecutor(new Reload());
            getLogger().info(cmd.getYellow() + "钓鱼功能: " + cmd.getReset());
            getLogger().info(cmd.getYellow() + " |-当前概率: " + cmd.getGreen() + getConfig().getInt("fish.item.range") + "%" + cmd.getReset());
            getLogger().info(cmd.getYellow() + " |-仅用指令: " + cmd.getGreen() + getConfig().getBoolean("fish.item.notItemDrop") + cmd.getReset());
            getLogger().info(cmd.getYellow() + " |-原版物品: " + cmd.getGreen() + getConfig().getBoolean("fish.item.notFishType") + cmd.getReset());
            getLogger().info(cmd.getYellow() + " |-执行命令: " + cmd.getGreen() + getConfig().getBoolean("fish.item.Consolecommand.type") + cmd.getReset());
            getLogger().info(cmd.getYellow() + " |-随机命令: " + cmd.getGreen() + getConfig().getBoolean("fish.item.Consolecommand.randomType") + cmd.getReset());
            getLogger().info(cmd.getYellow() + " |-钓鱼音效: " + cmd.getGreen() + getConfig().getBoolean("fish.item.sound.type") + cmd.getReset());
            getLogger().info(cmd.getYellow() + "额外功能: " + cmd.getReset());
            getLogger().info(cmd.getYellow() + " |-实体功能: " + cmd.getGreen() + getConfig().getBoolean("fishingEntity.type") + cmd.getReset());
            getLogger().info(cmd.getYellow() + " |-当前概率: " + cmd.getGreen() + getConfig().getInt("fishingEntity.range") + "%" + cmd.getReset());
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
        return this.customConfig;
    }
    public void createMessage(){
        customConfigFile = new File(getDataFolder(),"message.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("message.yml", false);
        }
        customConfig = new YamlConfiguration();
        try{
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
