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
            getPlugin(Main.class).createItemName();
            CmdColor cmd = new CmdColor();
            commandSender.sendMessage(getPlugin(Main.class).getMessage().getString("prefix") + getPlugin(Main.class).getMessage().getString("reload"));
        } else {
            commandSender.sendMessage(getPlugin(Main.class).getMessage().getString("prefix") + getPlugin(Main.class).getMessage().getString("notperm"));
        }
        return true;
    }
}
