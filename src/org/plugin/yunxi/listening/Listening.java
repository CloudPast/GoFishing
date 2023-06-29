package org.plugin.yunxi.listening;

import org.bukkit.entity.Item;
import org.bukkit.persistence.PersistentDataType;
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
    private String[] outMatItem;
    private String[] entity;
    private NamespacedKey itemNameKey;
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
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)
                && this.plugin.getConfig().getBoolean("fishingEntity." + event.getPlayer().getWorld().getName() + ".type")) {

            this.plugin.getConfig().getStringList("fishingEntity." + event.getPlayer().getWorld().getName() + ".entity")
                    .stream()
                    .filter(s -> s.contains(event.getHook().getHookedEntity().getType().toString()))
                    .findFirst()
                    .ifPresent(s -> {
                        entity = s.split(",");
                    });

            //判断是否是配置中的实体
            if (entity[0].contains(event.getHook().getHookedEntity().getType().toString()) && event.getHook().pullHookedEntity()) {
                if (!"none".equals(entity[1])) {
                    //判断是否加载点券插件
                    if (getPlugin(Main.class).getpoint()) {
                        //赋值
                        PlayerPoints pp = (PlayerPoints) Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
                        //判断该实体是否是列表内实体 然后判断点券够不够
                        if (entity[1].equals("point")
                                && pp.getAPI().look(event.getPlayer().getUniqueId()) >= Double.parseDouble(entity[2])) {
                            //替换关键字
                            String message = getPlugin(Main.class).getMessage().getString("message.takePoint");
                            message = message.replace("%Point%", entity[2]);
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
                            message = message.replace("%Money%", entity[2]);
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
                }
                if (r.nextInt(99) < this.plugin.getConfig().getInt("fishingEntity." + event.getPlayer().getWorld().getName() + ".range")) {
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
                    message = message.replace("%Entity%", this.plugin.getConfig().getString("entity." + entity[0]));
                    event.getPlayer().sendMessage(this.message.Message(message));
                    return;
                } else {
                    //删除实体
                    event.getCaught().remove();
                    //替换关键字
                    String message = getPlugin(Main.class).getMessage()
                            .getString("prefix") + getPlugin(Main.class)
                            .getMessage().getString("message.notEntity");
                    message = message.replace("%Entity%", this.plugin.getConfig().getString("entity." + entity[0]));
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
            //判断世界
            if (!this.plugin.getConfig().getBoolean("fish." + event.getPlayer().getWorld().getName() + ".type")) {
                return;
            }
            //判断钓到的是否是空的
            if (event.getCaught() != null && event.getCaught().getType() == EntityType.DROPPED_ITEM) {
                //重置咬钩的title
                event.getPlayer().resetTitle();
                Random ran = new Random();
                int sizeRandom = ran.nextInt(this.plugin.getConfig().getStringList("fish." + event.getPlayer().getWorld().getName() + ".mat").size());
                //获取mat列表
                this.plugin.getConfig().getStringList("fish." + event.getPlayer().getWorld().getName() + ".mat")
                        .stream()
                        .filter((s) -> s.contains(this.plugin.getConfig().getStringList("fish." + event.getPlayer().getWorld().getName() + ".mat").get(sizeRandom)))
                        .findFirst()
                        .ifPresent(s -> {
                            this.getItem = s.split(",");
                        });

                //获取物品ItemStack
                ItemStack matItem = new ItemStack(Material.valueOf(getItem[0]),
                        ran.nextInt(Integer.parseInt(getItem[2])
                                - Integer.parseInt(getItem[1])
                                + 1)
                                + Integer.parseInt(getItem[1]));

                boolean mat = true;
                //获得mat物品
                if (ran.nextDouble() < Double.parseDouble(getItem[3].split("%")[0])/100) {
                    event.getPlayer().getInventory().addItem(matItem);
                    mat = false;
                    String itemName = getPlugin(Main.class).getitemName().getString(getItem[0]);
                    //替换变量
                    itemName = getPlugin(Main.class).getMessage().getString("prefix") + getPlugin(Main.class).getMessage().getString("message.getItem").replace("%Item%", itemName);
                    itemName = itemName.replace("%Amount%", String.valueOf(matItem.getAmount()));
                    event.getPlayer().sendMessage(message.Message(itemName));
                    //判断是否启用指令功能
                    if (this.plugin.getConfig().getBoolean("fish." + event.getPlayer().getWorld().getName() + ".matCommand.type")){
                        //创建集合用于存储指令集群
                        ArrayList<String> list = new ArrayList<>();
                        //遍历并进行PlaceholderAPI解析 塞入集合中
                        for (int i = 0; i < this.plugin.getConfig().getStringList("fish." + event.getPlayer().getWorld().getName() + ".matCommand.item." + getItem[0]).size(); i++) {
                            list.add(PlaceholderAPI.setPlaceholders(event.getPlayer(),
                                    this.plugin.getConfig()
                                            .getStringList("fish." + event.getPlayer().getWorld().getName() + ".matCommand.item." + getItem[0])
                                            .get(i)));
                        }
                        //判断是否开启随机指令
                        if (this.plugin.getConfig().getBoolean("fish." + event.getPlayer().getWorld().getName() + ".matCommand.random")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),list.get(ran.nextInt(list.size())));
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),list.get(i));
                            }
                        }
                    }
                }

                //获得原版物品
                if (this.plugin.getConfig().getBoolean("fish." + event.getPlayer().getWorld().getName() + ".defaultItem.type")){
                    Double range = Double.parseDouble(this.plugin.getConfig().getString("fish." + event.getPlayer().getWorld().getName() + ".defaultItem.range").split("%")[0])/100;
                    if (ran.nextDouble() > range) {
                        event.getCaught().remove();
                    } else {
                        //判断获取的物品
                        ItemStack caughtItem = ((Item) event.getCaught()).getItemStack();
                        //并将物品导入翻译库
                        String itemName = getPlugin(Main.class).getitemName().getString(getItemName(caughtItem));
                        //替换变量
                        itemName = getPlugin(Main.class).getMessage().getString("prefix") + getPlugin(Main.class).getMessage().getString("message.getItem").replace("%Item%", itemName);
                        itemName = itemName.replace("%Amount%", String.valueOf(caughtItem.getAmount()));
                        //输出语句
                        event.getPlayer().sendMessage(message.Message(itemName));
                        return;
                    }
                }
                if (!this.plugin.getConfig().getBoolean("fish." + event.getPlayer().getWorld().getName() + ".outMat.type")) {
                    String message = getPlugin(Main.class).getMessage().getString("prefix") + getPlugin(Main.class).getMessage().getString("message.notFish");
                    event.getPlayer().sendMessage(this.message.Message(message));
                    return;
                }

                int sizeRandom2 = ran.nextInt(this.plugin.getConfig().getStringList("fish." + event.getPlayer().getWorld().getName() + ".outMat.item").size());
                this.plugin.getConfig().getStringList("fish." + event.getPlayer().getWorld().getName() + ".outMat.item")
                        .stream()
                        .filter((s) -> s.contains(this.plugin.getConfig().getStringList("fish." + event.getPlayer().getWorld().getName() + ".outMat.item").get(sizeRandom2)))
                        .findFirst()
                        .ifPresent(s -> {
                            this.outMatItem = s.split(",");
                        });

                ItemStack item = new ItemStack(Material.valueOf(outMatItem[0]),
                        ran.nextInt((Integer.parseInt(outMatItem[2])
                                - Integer.parseInt(outMatItem[1])
                                + 1))
                                + Integer.parseInt(outMatItem[1]));
                //获得保底物品
                if (this.plugin.getConfig().getBoolean("fish." + event.getPlayer().getWorld().getName() + ".outMat.type")
                        && mat) {
                    String itemName = getPlugin(Main.class).getitemName().getString(outMatItem[0]);
                    itemName = getPlugin(Main.class).getMessage().getString("prefix") + getPlugin(Main.class).getMessage().getString("message.getItem").replace("%Item%", itemName);
                    itemName = itemName.replace("%Amount%", String.valueOf(item.getAmount()));
                    event.getPlayer().sendMessage(message.Message(itemName));
                    event.getPlayer().getInventory().addItem(item);
                }
            }
        }
    }

    //获得钓来的鱼的英文ID
    private String getItemName(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(itemNameKey, PersistentDataType.STRING)) {
            return item.getItemMeta().getPersistentDataContainer().get(itemNameKey, PersistentDataType.STRING);
        }
        return item.getType().toString();
    }
}
