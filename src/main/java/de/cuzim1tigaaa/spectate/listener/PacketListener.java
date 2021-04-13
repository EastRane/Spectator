package de.cuzim1tigaaa.spectate.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import de.cuzim1tigaaa.spectate.Main;
import de.cuzim1tigaaa.spectate.files.Config;
import de.cuzim1tigaaa.spectate.files.Paths;
import de.cuzim1tigaaa.spectate.files.Permissions;
import de.cuzim1tigaaa.spectate.player.Inventory;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PacketListener {

    private final Main instance;

    public PacketListener(Main plugin) {
        this.instance = plugin;
        this.register();
    }

    public void register() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(PacketAdapter.params().plugin(instance).types(PacketType.Play.Client.USE_ENTITY).optionAsync()) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                if(!player.getGameMode().equals(GameMode.SPECTATOR)) return;
                if(!instance.getSpectators().contains(player)) return;

                EnumWrappers.EntityUseAction action = event.getPacket().getEntityUseActions().read(0);
                if(!action.equals(EnumWrappers.EntityUseAction.ATTACK)) return;

                Entity entity = event.getPacket().getEntityModifier(player.getWorld()).read(0);
                if(!entity.getType().equals(EntityType.PLAYER)) return;

                Player target = (Player) entity;
                if(target.hasPermission(Permissions.BYPASS_SPECTATED) || !player.hasPermission(Permissions.COMMAND_SPECTATE_OTHERS)) {
                    if(!player.hasPermission(Permissions.BYPASS_SPECTATEALL)) {
                        player.sendMessage(Config.getMessage(Paths.MESSAGES_GENERAL_BYPASS, "TARGET", target.getName()));
                        event.setCancelled(true);
                    }
                }else {
                    if(player.hasPermission(Permissions.UTILS_MIRROR_INVENTORY) && Config.mirrorInventory) Inventory.getInventory(player, target);
                    instance.getRelation().put(player, target);
                }
            }
        });
    }
}
