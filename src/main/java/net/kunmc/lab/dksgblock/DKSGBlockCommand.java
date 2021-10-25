package net.kunmc.lab.dksgblock;

import com.google.gson.Gson;
import net.teamfruit.easystructure.I18n;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        //Material.valueOf("")
    /*    JsonObject ja = new JsonObject();
        BlockType.REGISTRY.iterator().forEachRemaining(n -> {
            String str = n.getDefaultState().getAsString();
            String[] sts = str.split("\\[");
            if (sts.length >= 2) {
                str = sts[1].split("\\]")[0];
            } else {
                str = "";
            }
            ja.addProperty(n.getId(), str);
        });
        String str = GSON.toJson(ja);
        try {
            Files.write(Paths.get("blocksstates.json"), str.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        DKSGTest.please(((Player) sender).getLocation(), ((Player) sender).getWorld());
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> strs = new ArrayList<>();
        strs.add("ftize");
        return strs;
    }
}
