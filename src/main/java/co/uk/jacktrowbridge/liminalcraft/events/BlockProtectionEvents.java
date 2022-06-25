package co.uk.jacktrowbridge.liminalcraft.events;

import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;

public class BlockProtectionEvents {

    public void registerEvents(GlobalEventHandler globalEventHandler){

        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            // TODO: Check if player is in build mode
            event.setCancelled(true);
        });

        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, event -> {
            // TODO: Check if player is in build mode
            event.setCancelled(true);
        });

    }

}
