package net.kunmc.lab.dksgblock;

import net.kunmc.lab.ikisugilogger.IkisugiLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class DKSGBlock extends JavaPlugin {

    @Override
    public void onEnable() {
        DKSGBlockCommand.init(this);
        IkisugiLogger logger = new IkisugiLogger("ikisugi\n16x block");
        logger.setColorType(IkisugiLogger.ColorType.RAINBOW);
        logger.setCenter(true);
        getLogger().info("\n" + logger.create());
    }

    @Override
    public void onDisable() {

    }
}
