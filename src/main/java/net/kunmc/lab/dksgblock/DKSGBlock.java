package net.kunmc.lab.dksgblock;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kunmc.lab.ikisugilogger.IkisugiLogger;
import net.teamfruit.easystructure.EasyStructure;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class DKSGBlock extends JavaPlugin {
    private static final Gson GSON = new Gson();
    public static final Map<String, String> MAP = new HashMap<>();

    @Override
    public void onEnable() {
        DKSGBlockCommand.init(this);
        MAP.clear();
        InputStream stream = new BufferedInputStream(DKSGBlock.class.getResourceAsStream("/dksgblock/map.json"));
        JsonObject map = GSON.fromJson(new InputStreamReader(stream), JsonObject.class);
        Path esFol = JavaPlugin.getPlugin(EasyStructure.class).getDataFolder().toPath().resolve("schematics");
        esFol.toFile().mkdirs();
        for (Map.Entry<String, JsonElement> entry : map.entrySet()) {
            String name = entry.getValue().getAsString();
            MAP.put(entry.getKey(), name);
            InputStream stricter = DKSGBlock.class.getResourceAsStream("/dksgblock/stricters/" + name + ".schem");
            if (stricter != null) {
                stricter = new BufferedInputStream(stricter);
                try {
                    Path path = esFol.resolve(name + ".schem");
                    if (!path.toFile().exists())
                        Files.write(path, DKSGUtil.streamToByteArray(stricter));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                getLogger().log(Level.WARNING, "Not find file: " + name);
            }
        }

        IkisugiLogger logger = new IkisugiLogger("ikisugi\n16x block");
        logger.setColorType(IkisugiLogger.ColorType.RAINBOW);
        logger.setCenter(true);
        getLogger().info("\n" + logger.create());
    }

    @Override
    public void onDisable() {

    }
}
