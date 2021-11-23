package net.kunmc.lab.dksgblock;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.item.ItemTypes;
import net.teamfruit.easystructure.ESUtils;
import net.teamfruit.easystructure.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DKSGBlockCommand implements CommandExecutor, TabCompleter {
    private static final Gson GSON = new Gson();

    public static void init(DKSGBlock plugin) {
        plugin.getCommand("dksg").setExecutor(new DKSGBlockCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equals("dksg"))
            return true;

        if (!(sender instanceof Player)) {
            sender.sendMessage(I18n.format("command.error.playeronly"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("引数が足りねぇぜ");
            return true;
        }

        com.sk89q.worldedit.entity.Player wPlayer = BukkitAdapter.adapt(((Player) sender).getPlayer());

        switch (args[0]) {
            case "gen":
                try {
                    Location location = ((Player) sender).getLocation();
                    Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(DKSGBlock.class), () -> {
                        try {
                            DKSGSave.save(sender, location, ((Player) sender).getWorld());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                sender.sendMessage("Saved!");
                break;
            case "get":
                if (args.length == 1) {
                    sender.sendMessage("ブロックIDを引数に加えてください");
                    return true;
                }
                String name = DKSGBlock.MAP.get(args[1]);
                if (name == null) {
                    sender.sendMessage("存在しない、またはデータがないブロックIDです");
                    return true;
                }

                BaseItemStack itemStack = new BaseItemStack(ItemTypes.BLAZE_ROD, ESUtils.createWandItem(name, args[1]), 1);
                wPlayer.giveItem(itemStack);
                break;
            case "place":
                File generates = new File("generates");
                if (!generates.toPath().resolve(args[1] + ".json").toFile().exists()) {
                    sender.sendMessage("ファイルがないです");
                    return true;
                }
                try {
                    DKSGGen.please(((Player) sender).getLocation(), ((Player) sender).getWorld(), args[1], n -> {
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) {
            return Lists.newArrayList("get", "gen", "place");
        }

        if (args.length == 2 && "get".equals(args[0])) {
            return new ArrayList<>(DKSGBlock.MAP.keySet());
        }

        if (args.length == 2 && "place".equals(args[0])) {
            List<String> strs = new ArrayList<>();
            File generates = new File("generates");
            for (File file : generates.listFiles()) {
                strs.add(file.getName().split("\\.")[0]);
            }
            return strs;
        }

        return Lists.newArrayList();
    }


}
