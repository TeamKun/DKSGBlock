package net.kunmc.lab.dksgblock;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.extension.platform.permission.ActorSelectorLimits;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class DKSGSave {
    private static final Gson GSON = new Gson();
    private static Vector3 lastBlock = Vector3.ZERO;

    public static void save(CommandSender sender, @NotNull Location location, @NotNull World world) throws Exception {
        JsonObject map = new JsonObject();
        File fol = new File("generates");
        for (File file : fol.listFiles()) {
            String name = file.getName().split("\\.")[0];
            System.out.println(name);
            sender.sendMessage("Generate: " + name);
            UUID uuid = UUID.randomUUID();
            map.addProperty(name, uuid.toString());
            generate(location, world, name);
            save(sender, uuid.toString());
            Thread.sleep(1);
        }

        try {
            Files.write(Paths.get("map.json"), GSON.toJson(map).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generate(@NotNull Location location, @NotNull World world, String name) throws Exception {
        for (int x = -32; x < 32; x++) {
            for (int y = -32; y < 32; y++) {
                for (int z = -32; z < 32; z++) {
                    int finalX = x;
                    int finalY = y;
                    int finalZ = z;
                    Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(DKSGBlock.class), () -> {
                        world.getBlockAt(finalX + location.getBlockX(), finalY + location.getBlockY(), finalZ + location.getBlockZ()).setBlockData(Material.AIR.createBlockData());
                    });
                }
            }
        }
        DKSGGen.please(location, world, name, n -> {
            lastBlock = n;
        });
    }

    public static void save(CommandSender sender, String name) throws Exception {
        AbstractPlayerActor wPlayer = BukkitAdapter.adapt(((Player) sender).getPlayer());
        com.sk89q.worldedit.world.World world = wPlayer.getWorld();
        com.sk89q.worldedit.util.Location loc = wPlayer.getLocation();

        LocalSession session = WorldEdit.getInstance().getSessionManager().get(wPlayer);

        session.getRegionSelector(world).selectPrimary(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), ActorSelectorLimits.forActor(wPlayer));
        session.getRegionSelector(world).selectSecondary(BlockVector3.at(loc.getBlockX() + 16, loc.getBlockY() + 16, loc.getBlockZ() + 16), ActorSelectorLimits.forActor(wPlayer));

        Region region = session.getSelection(world);
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                editSession, region, clipboard, region.getMinimumPoint()
        );
        Operations.complete(forwardExtentCopy);
        new File("blockschems").mkdir();
        File file = Paths.get("blockschems").resolve(name + ".schem").toFile();
        ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file));
        writer.write(clipboard);
        writer.close();
    }
}
