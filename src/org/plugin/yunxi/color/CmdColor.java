package org.plugin.yunxi.color;

public class CmdColor {
    private String reset = "\u001b[0m";
    private String red = "\u001b[31m";
    private String green = "\u001b[32m";
    private String yellow = "\u001b[33m";
    private String blue = "\u001b[34m";
    private String purple = "\u001b[35m";
    private String cyan = "\u001b[36m";
    private String white = "\u001b[37m";

    public String getRed() {
        return red;
    }

    public String getReset() {
        return reset;
    }

    public String getGreen() {
        return green;
    }

    public String getYellow() {
        return yellow;
    }

    public String getBlue() {
        return blue;
    }

    public String getPurple() {
        return purple;
    }

    public String getCyan() {
        return cyan;
    }

    public String getWhite() {
        return white;
    }
}
