package net.kunmc.lab.dksgblock;

import org.bukkit.plugin.java.JavaPlugin;

public final class DKSGBlock extends JavaPlugin {

    @Override
    public void onEnable() {
        DKSGBlockCommand.init(this);
    }

    @Override
    public void onDisable() {

    }
}
