package net.kunmc.lab.dksgblock;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.stream.Collectors;

public class DKSGTest {
    private static final Gson GSON = new Gson();

    public static void please(@NotNull Location location, @NotNull World world) {
        try {
            BufferedImage image = ImageIO.read(new File("test.png"));
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    int col = image.getRGB(x, y);
                    if (col != 0)
                        setBlock(x, y, 0, location, world, "minecraft:" + getMostColorBlock(col));
                }
            }
            System.out.println("test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void setBlock(int x, int y, int z, Location location, World world, String type) {
        Material mte = Material.matchMaterial(type);
        world.getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z).setBlockData(mte.createBlockData());
    }

    private static String getMostColorBlock(int color) throws FileNotFoundException {
        JsonObject jo = GSON.fromJson(new FileReader("colors.json"), JsonObject.class);
        String[] colorBlocks = jo.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()).toArray(new String[]{});
        String str = ColorUtil.getApproximateColorObject(color, colorBlocks, n -> jo.get(n).getAsInt());
        return str;
    }

}
