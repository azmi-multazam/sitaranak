package com.zam.sidik_padang.home.newsinfo;

/**
 * Created by supriyadi on 10/13/17.
 */

class NavigationMenuItem {
    String id, nama;
    boolean selected = false;

    public NavigationMenuItem(String id, String nama, boolean selected) {
        this.id = id;
        this.nama = nama;
        this.selected = selected;
    }

    public NavigationMenuItem() {
    }
}
