package com.woch.library;

/**
 * Created by wangdake on 2017/11/30.
 */

public class MarkEntity {

    private String markPath;
    private int x;
    private int y;
    private boolean isText;
    private String fontsize;
    private String fontcolor;
    private String boxcolor;
    private String text;

    public String getMarkPath() {
        return markPath;
    }

    public void setMarkPath(String markPath) {
        this.markPath = markPath;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isText() {
        return isText;
    }

    public void setText(boolean text) {
        isText = text;
    }

    public String getFontsize() {
        return fontsize;
    }

    public void setFontsize(String fontsize) {
        this.fontsize = fontsize;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public String getBoxcolor() {
        return boxcolor;
    }

    public void setBoxcolor(String boxcolor) {
        this.boxcolor = boxcolor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
