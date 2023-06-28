package org.plugin.yunxi.color;


import net.md_5.bungee.api.ChatColor;

public class Message {
    public String Message (String message) {
        //将消息以#分割传入数组
        String[] sendMessage = message.split("#");
        //记录消息
        String endMessage = "";
        //循环该消息
        for (int i = 1; i < sendMessage.length; i++) {
            //将每个字符串另外赋值
            String s = "#" + (sendMessage[i].substring(0, 6));
            //将消息取出
            sendMessage[i] = sendMessage[i].substring(6, sendMessage[i].length());
            //将单个消息转换为输出语句
            endMessage += ChatColor.of(s) + sendMessage[i];
        }
        endMessage = sendMessage[0] + endMessage;
        return endMessage;
    }
}
