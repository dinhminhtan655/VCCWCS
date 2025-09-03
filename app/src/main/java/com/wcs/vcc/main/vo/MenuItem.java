package com.wcs.vcc.main.vo;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.List;

public class MenuItem {
    public static final String MENU_ITEM_NAME = "menu_item_name";

    private int position = -1;
    private int name;
    private int icon;
    private List<Integer> groups;
    private int notification;
    private Class<?> menuDetailActivity;
    private String strTypeDORO;

    public MenuItem(@StringRes int name,
                    @DrawableRes int icon,
                    @NonNull List<Integer> groups,
                    @NonNull Class<?> menuDetailActivity) {
        this.name = name;
        this.icon = icon;
        this.groups = groups;
        this.menuDetailActivity = menuDetailActivity;

    }



    public MenuItem(@StringRes int name,
                    @DrawableRes int icon,
                    @NonNull List<Integer> groups,
                    @NonNull Class<?> menuDetailActivity, String strTypeDORO) {
        this.name = name;
        this.icon = icon;
        this.groups = groups;
        this.menuDetailActivity = menuDetailActivity;
        this.strTypeDORO = strTypeDORO;
    }

    public MenuItem(int name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @StringRes
    public int getName() {
        return name;
    }

    @DrawableRes
    public int getIcon() {
        return icon;
    }

    public List<Integer> getGroups() {
        return groups;
    }

    public int getNotification() {
        return notification;
    }

    public String getStrTypeDORO() {
        return strTypeDORO;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public void addNotification(int newNotification) {
        setNotification(notification + newNotification);
    }


    public Class<?> getMenuDetailActivity() {
        return menuDetailActivity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) obj;
            return menuItem.name == name;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name;
    }

    @Override
    public String toString() {
        return "Position: " + getPosition() + '\n' +
                "Name: " + getName() + '\n' +
                "Groups: " + getGroups() + '\n' +
                "Notification: " + getNotification() + '\n' +
                "MenuDetailActivity: " + getMenuDetailActivity() + '\n' +
                "HasCode: " + Integer.toHexString(hashCode());
    }
}
