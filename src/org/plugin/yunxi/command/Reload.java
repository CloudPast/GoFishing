package org.plugin.yunxi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.plugin.yunxi.color.CmdColor;
import org.plugin.yunxi.pluginMain.Main;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class Reload implements CommandExecutor {
    private Plugin plugin = getPlugin(Main.class);
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            this.plugin.reloadConfig();
            getPlugin(Main.class).createMessage();
            CmdColor cmd = new CmdColor();
            this.plugin.getLogger().info(cmd.getYellow() + "钓鱼功能: " + cmd.getReset());
            this.plugin.getLogger().info(cmd.getYellow() + " |-当前概率: " + cmd.getGreen() + this.plugin.getConfig().getInt("fish.item.range") + "%" + cmd.getReset());
            this.plugin.getLogger().info(cmd.getYellow() + " |-仅用指令: " + cmd.getGreen() + this.plugin.getConfig().getBoolean("fish.item.notItemDrop") + cmd.getReset());
            this.plugin.getLogger().info(cmd.getYellow() + " |-原版物品: " + cmd.getGreen() + this.plugin.getConfig().getBoolean("fish.item.notFishType") + cmd.getReset());
            this.plugin.getLogger().info(cmd.getYellow() + " |-执行命令: " + cmd.getGreen() + this.plugin.getConfig().getBoolean("fish.item.Consolecommand.type") + cmd.getReset());
            this.plugin.getLogger().info(cmd.getYellow() + " |-随机命令: " + cmd.getGreen() + this.plugin.getConfig().getBoolean("fish.item.Consolecommand.randomType") + cmd.getReset());
            this.plugin.getLogger().info(cmd.getYellow() + " |-钓鱼音效: " + cmd.getGreen() + this.plugin.getConfig().getBoolean("fish.item.sound.type") + cmd.getReset());
            this.plugin.getLogger().info(cmd.getYellow() + "额外功能: " + cmd.getReset());
            this.plugin.getLogger().info(cmd.getYellow() + " |-实体功能: " + cmd.getGreen() + this.plugin.getConfig().getBoolean("fishingEntity.type") + cmd.getReset());
            this.plugin.getLogger().info(cmd.getYellow() + " |-当前概率: " + cmd.getGreen() + this.plugin.getConfig().getInt("fishingEntity.range") + "%" + cmd.getReset());
            this.plugin.getLogger().info(cmd.getCyan() + "插件已成功加载！    感谢使用本插件-鹊吟猫" + cmd.getReset());
            commandSender.sendMessage(getPlugin(Main.class).getMessage().getString("prefix") + getPlugin(Main.class).getMessage().getString("reload"));
        } else {
            commandSender.sendMessage(getPlugin(Main.class).getMessage().getString("prefix") + getPlugin(Main.class).getMessage().getString("notperm"));
        }
        return true;
    }
}
