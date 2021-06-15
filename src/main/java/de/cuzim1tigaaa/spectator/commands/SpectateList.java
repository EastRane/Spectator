package de.cuzim1tigaaa.spectator.commands;

import de.cuzim1tigaaa.spectator.Main;
import de.cuzim1tigaaa.spectator.cycle.CycleHandler;
import de.cuzim1tigaaa.spectator.files.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SpectateList implements CommandExecutor, TabCompleter {

    private final Main instance;

    public SpectateList(Main plugin) {
        this.instance = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission(Permissions.COMMANDS_SPECTATE_LIST)) {
            sender.sendMessage(Config.getMessage(Paths.MESSAGE_DEFAULT_PERMISSION));
            return true;
        }
        int spectators = instance.getSpectators().size();
        if(spectators == 0) {
            sender.sendMessage(Config.getMessage(Paths.MESSAGES_COMMANDS_LIST_NONE));
            return true;
        }
        sender.sendMessage(Config.getMessage(Paths.MESSAGES_COMMANDS_LIST_TITLE, "AMOUNT", spectators));
        for(Player all : instance.getSpectators()) {
            if(all != null) {
                String msg;
                if(instance.getRelation().containsKey(all)) {
                    if(CycleHandler.isPlayerCycling(all)) msg = Config.getMessage(Paths.MESSAGES_COMMANDS_LIST_CYCLING, "SPECTATOR", all.getDisplayName());
                    else msg = Config.getMessage(Paths.MESSAGES_COMMANDS_LIST_SPECTATING, "SPECTATOR", all.getDisplayName(), "TARGET", instance.getRelation().get(all).getDisplayName());
                }else msg = Config.getMessage(Paths.MESSAGES_COMMANDS_LIST_DEFAULT, "SPECTATOR", all.getDisplayName());
                sender.sendMessage(msg);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }
}