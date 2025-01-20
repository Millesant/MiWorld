package com.github.nollyak.plugin;

import cn.nukkit.plugin.PluginBase;

public abstract class NoWorldPlugin extends PluginBase {

    @Override
    public void onLoad() {
        this.onPluginLoad();
    }

    public abstract void onPluginLoad();

    @Override
    public void onEnable() {
        this.onPluginStart();
    }

    public abstract void onPluginStart();

    @Override
    public void onDisable() {
        this.onPluginStop();
    }

    public abstract void onPluginStop();

}
