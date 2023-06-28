package org.plugin.yunxi.listening;

import org.plugin.yunxi.color.Message;
import org.plugin.yunxi.pluginMain.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class Listening implements Listener {

    private Plugin plugin = getPlugin(Main.class);
    private Random r = new Random();
    private String[] getItem;
    private String[] NoRanItem;
    private String[] entity;

    private Message message = new Message();

    @EventHandler
    public void fishEvent(PlayerFishEvent event) {
        //抛竿
        if (event.getState().equals(PlayerFishEvent.State.FISHING) && this.plugin.getConfig().getBoolean("message.fishing")) {
            event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class)
                    .getMessage().getString("prefix") + getPlugin(Main.class)
                    .getMessage().getString("message.fishing")));
            return;
        }
        //钓到实体
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY) && this.plugin.getConfig().getBoolean("fishingEntity.type")) {
            this.plugin.getConfig().getStringList("fishingEntity.entity")
                    .stream()
                    .filter(s -> s.contains(event.getHook().getHookedEntity().getType().toString()))
                    .findFirst()
                    .ifPresent(s -> {
                        entity = s.split(",");
                    });
            //判断是否是配置中的实体
            if (entity[0].contains(event.getHook().getHookedEntity().getType().toString()) && event.getHook().pullHookedEntity()) {
                //判断是否加载点券插件
                if (getPlugin(Main.class).getpoint()) {
                    //赋值
                    PlayerPoints pp = (PlayerPoints) Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
                    //判断该实体是否是列表内实体 然后判断点券够不够
                    if (entity[1].equals("point")
                            && pp.getAPI().look(event.getPlayer().getUniqueId()) >= Double.parseDouble(entity[2])) {
                        //替换关键字
                        String message = getPlugin(Main.class).getMessage().getString("message.takePoint");
                        message = String.format(message, Integer.parseInt(entity[2]));
                        event.getPlayer().sendMessage(this.message.Message(message));
                        pp.getAPI().take(event.getPlayer().getUniqueId(), Integer.parseInt(entity[2]));
                    }
                    //判断该实体是否是列表内实体 然后判断点券不够 然后输出点券不够语句
                    else if (entity[1].equals("point")
                            && pp.getAPI().look(event.getPlayer().getUniqueId()) < Double.parseDouble(entity[2])) {
                        event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("message.notPoint")));
                        return;
                    }
                    //若为点券扣除 以上两条判断过了 则直接return
                    else if (entity[1].equals("point")) {
                        return;
                    }
                }
                //判断是否加载金币插件
                if (getPlugin(Main.class).getvault()) {
                    if (entity[1].equals("money")
                            && getPlugin(Main.class).econ.has(event.getPlayer(), Double.parseDouble(entity[2]))) {
                        //替换关键字
                        String message = getPlugin(Main.class).getMessage().getString("message.takeMoney");
                        message = String.format(message, Integer.parseInt(entity[2]));
                        event.getPlayer().sendMessage(message);
                        getPlugin(Main.class).econ.withdrawPlayer(event.getPlayer(), Double.parseDouble(entity[2]));
                    } else if (entity[1].equals("money")
                            && !getPlugin(Main.class).econ.has(event.getPlayer(), Double.parseDouble(entity[2]))) {
                        event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("message.notMoney")));
                        return;
                    } else if (entity[1].equals("money")) {
                        return;
                    }
                }
                if (r.nextInt(99) < this.plugin.getConfig().getInt("fishingEntity.range")) {
                    //删除实体
                    event.getCaught().remove();
                    //设置实体刷怪蛋
                    ItemStack item = new ItemStack(Material.valueOf(event.getCaught().getType() + "_SPAWN_EGG"), 1);
                    //给予刷怪蛋
                    event.getPlayer().getInventory().addItem(item);
                    //替换关键字
                    String message = getPlugin(Main.class).getMessage()
                            .getString("prefix") + getPlugin(Main.class)
                            .getMessage().getString("message.getEntity");
                    message = String.format(message, this.plugin.getConfig().getString("entity." + entity[0]));
                    event.getPlayer().sendMessage(this.message.Message(message));
                    return;
                } else {
                    //删除实体
                    event.getCaught().remove();
                    //替换关键字
                    String message = getPlugin(Main.class).getMessage()
                            .getString("prefix") + getPlugin(Main.class)
                            .getMessage().getString("message.notEntity");
                    message = String.format(message, this.plugin.getConfig().getString("entity." + entity[0]));
                    event.getPlayer().sendMessage(this.message.Message(message));
                    return;
                }
            }
            return;
        }
        //收杆了
        if (event.getState().equals(PlayerFishEvent.State.REEL_IN)
                && this.plugin.getConfig().getBoolean("message.reelIn")) {

            event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class)
                    .getMessage().getString("prefix") + getPlugin(Main.class)
                    .getMessage().getString("message.reelIn")));
            return;

        }
        //鱼跑掉了
        if (event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT)
                && this.plugin.getConfig().getBoolean("message.failedAttempt")) {

            event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class)
                    .getMessage().getString("prefix") + getPlugin(Main.class)
                    .getMessage().getString("message.failedAttempt")));
            return;
        }
        //咬钩了
        if (event.getState().equals(PlayerFishEvent.State.BITE)
                && this.plugin.getConfig().getBoolean("message.allBite")) {

            event.getPlayer().sendTitle(this.message.Message(getPlugin(Main.class)
                    .getMessage().getString("message.bite")), this.message.Message(getPlugin(Main.class)
                    .getMessage().getString("message.subBite")));
            return;
        }
        //上鱼了
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            if (r.nextInt(99) < this.plugin.getConfig().getInt("fish.item.range")
                    && this.plugin.getConfig().getBoolean("fish.item.type")
                    && event.getCaught() != null
                    && event.getCaught().getType() == EntityType.DROPPED_ITEM) {

                event.getPlayer().resetTitle();
                boolean Ran = false;
                if (this.plugin.getConfig().getBoolean("fish.item.notItemDrop")) {
                    int ran = r.nextInt(this.plugin.getConfig().getStringList("fish.item.mat").size());
                    int getRandom = r.nextInt(99);

                    //获取mat列表
                    this.plugin.getConfig().getStringList("fish.item.mat")
                            .stream()
                            .filter((s) -> s.contains(this.plugin.getConfig().getStringList("fish.item.mat").get(ran)))
                            .findFirst()
                            .ifPresent(s -> {
                                this.getItem = s.split(",");
                            });

                    if (Double.parseDouble(this.getItem[3]) >= getRandom) {
                        int amountRandom =
                                r.nextInt((Integer.parseInt(this.getItem[2])
                                        - Integer.parseInt(this.getItem[1]) + 1))
                                        + Integer.parseInt(this.getItem[1]);

                        //将Item类输出的物品ID导入ItemStack
                        ItemStack additem = new ItemStack(Material.getMaterial(this.getItem[0]), amountRandom);
                        //给玩家随机到的物品
                        event.getPlayer().getInventory().addItem(additem);
                        if (this.plugin.getConfig().getBoolean("message.getItem")) {
                            String message =
                                    getPlugin(Main.class).getMessage().getString("prefix")
                                            + getPlugin(Main.class).getMessage().getString("message.getItem");

                            //替换关键字
                            message = String.format(message,
                                    additem.getAmount(),
                                    this.plugin.getConfig().getString("mat." + this.getItem[0]));
                            Ran = true;

                            event.getPlayer().sendMessage(this.message.Message(message));
                        }
                    } else {
                        int ran2 = r.nextInt(this.plugin.getConfig().getStringList("fish.item.defaultMat").size());
                        this.plugin.getConfig().getStringList("fish.item.defaultMat")
                                .stream()
                                .filter((s) -> s.contains(this.plugin.getConfig().getStringList("fish.item.defaultMat").get(ran2)))
                                .findFirst()
                                .ifPresent(s -> {
                                    this.NoRanItem = s.split(",");
                                });

                        int amountRandom =
                                r.nextInt((Integer.parseInt(this.NoRanItem[2])
                                        - Integer.parseInt(this.NoRanItem[1]) + 1))
                                        + Integer.parseInt(this.NoRanItem[1]);

                        //将Item类输出的物品ID导入ItemStack
                        ItemStack additem = new ItemStack(Material.getMaterial(this.NoRanItem[0]), amountRandom);
                        //给玩家随机到的物品
                        event.getPlayer().getInventory().addItem(additem);
                        if (this.plugin.getConfig().getBoolean("message.getItem")) {
                            String message =
                                    getPlugin(Main.class).getMessage().getString("prefix")
                                            + getPlugin(Main.class).getMessage().getString("message.getItem");

                            //替换关键字
                            message = String.format(message,
                                    additem.getAmount(),
                                    this.plugin.getConfig().getString("mat." + this.NoRanItem[0]));

                            event.getPlayer().sendMessage(this.message.Message(message));
                        }
                    }

                }
                //判断是否开启音效
                if (this.plugin.getConfig().getBoolean("fish.item.sound.type")) {
                    //判断是否有这个音效
                    if (!this.plugin.getConfig().getString("fish.item.sound.sounds." + getItem[0]).equals("false")
                            && this.plugin.getConfig().getString("fish.item.sound.sounds." + this.getItem[0]) != null) {
                        //获取玩家位置
                        Location playerLocation = event.getPlayer().getLocation();
                        //设置音效位置
                        Location location = new Location(
                                event.getPlayer().getWorld(),
                                playerLocation.getX(),
                                playerLocation.getY(),
                                playerLocation.getZ());

                        //播放音效
                        event.getPlayer().playSound(location,
                                Sound.valueOf(this.plugin.getConfig()
                                        .getString("fish.item.sound.sounds." + this.getItem[0])), 1, 1);
                    }
                }
                //删除玩家钓到的物品
                event.getCaught().remove();
                //钓到某个物品执行某个指令
                if (this.plugin.getConfig().getBoolean("fish.item.Consolecommand.type")) {
                    ArrayList<String> list = new ArrayList<>();
                    if (Ran) {
                        //创建集合接受指令组
                        for (int i = 0; i < this.plugin.getConfig().getStringList("fish.item.Consolecommand.item." + this.getItem[0]).size(); i++) {
                            list.add(PlaceholderAPI.setPlaceholders(
                                    event.getPlayer(),
                                    this.plugin.getConfig().getStringList("fish.item.Consolecommand.item." + this.getItem[0]).get(i)));
                        }
                    } else {
                        for (int i = 0; i < this.plugin.getConfig().getStringList("fish.item.Consolecommand.item." + this.NoRanItem[0]).size(); i++) {
                            list.add(PlaceholderAPI.setPlaceholders(
                                    event.getPlayer(),
                                    this.plugin.getConfig().getStringList("fish.item.Consolecommand.item." + this.NoRanItem[0]).get(i)));
                        }
                        //判断是否打开随机指令
                        if (this.plugin.getConfig().getBoolean("fish.item.Consolecommand.randomType")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), list.get(r.nextInt(list.size())));
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), list.get(i));
                            }
                        }
                    }
                } else if (!this.plugin.getConfig().getBoolean("fish.item.notFishType")) {
                    //没钓到鱼
                    event.getPlayer().sendTitle("", "");
                    event.getCaught().remove();
                    if (this.plugin.getConfig().getBoolean("message.notFish")) {
                        event.getPlayer().sendMessage(this.message.Message(
                                getPlugin(Main.class).getMessage().getString("prefix")
                                        + getPlugin(Main.class).getMessage().getString("message.notFish")));
                    }
                }
            }
        }
    }
}
