package net.kunmc.lab.dksgblock;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sk89q.worldedit.math.Vector3;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class DKSGGen {
    private static final Gson GSON = new Gson();

    public static void please(@NotNull Location location, @NotNull World world, String name, Consumer<Vector3> start, Consumer<Vector3> last) throws Exception {
        File generates = new File("generates", name + ".json");
        JsonObject jo = GSON.fromJson(new FileReader(generates), JsonObject.class);
        JsonObject parret = jo.getAsJsonObject("parret");
        JsonObject entrys = jo.getAsJsonObject("entry");
        int mx = 0;
        int my = 0;
        int mz = 0;
        int nx = 0;
        int ny = 0;
        int nz = 0;

        for (Map.Entry<String, JsonElement> en : entrys.entrySet()) {
            String[] strs = en.getKey().split(",");
            int x = Integer.parseInt(strs[0]);
            int y = Integer.parseInt(strs[1]);
            int z = Integer.parseInt(strs[2]);
            mx = Math.max(mx, x);
            my = Math.max(my, y);
            mz = Math.max(mz, z);

            nx = Math.min(nx, x);
            ny = Math.min(ny, y);
            nz = Math.min(nz, z);
            BlockStateble bp = getParret(parret, en.getValue().getAsInt());
            if (bp != null && !bp.getName().isEmpty()) {
                Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(DKSGBlock.class), () -> {
                    setBlock(x, y, z, location, world, bp.getName(), bp.getAxis());
                });
            }
        }
        start.accept(Vector3.at(nx, ny, nz));
        last.accept(Vector3.at(mx, my, mz));
    }

    private static BlockStateble getParret(JsonObject parret, int num) {
        for (Map.Entry<String, JsonElement> en : parret.entrySet()) {
            if (en.getValue().getAsInt() == num) {
                if (en.getKey().split("@").length == 1)
                    return new BlockStateble(en.getKey(), null);
                else
                    return new BlockStateble(en.getKey().split("@")[0], Axis.valueOf(en.getKey().split("@")[1].toUpperCase(Locale.ROOT)));
            }

        }
        return null;
    }

    private static void setBlock(int x, int y, int z, Location location, World world, String type, Axis axis) {
        if (type.split(":").length == 0) {
            type = "minecraft:" + type;
        }
        Material mte = Material.matchMaterial(type);
        Block bl = world.getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
        BlockData bld = mte.createBlockData();
        if (bld instanceof Orientable && axis != null)
            ((Orientable) bld).setAxis(axis);
        bl.setBlockData(bld);
    }

    public static class BlockStateble {
        private final String name;
        private final Axis axis;

        public BlockStateble(String name, Axis axis) {
            this.name = name;
            this.axis = axis;
        }

        public String getName() {
            return name;
        }

        public Axis getAxis() {
            return axis;
        }
    }
}
