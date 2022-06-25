package co.uk.jacktrowbridge.liminalcraft;

import co.uk.jacktrowbridge.liminalcraft.events.BlockProtectionEvents;
import co.uk.jacktrowbridge.liminalcraft.utils.PlayerUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.ConnectionManager;
import org.bson.Document;

public class LiminalCraft {

    public static void main(String[] args){

        // Database
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("LiminalCraft");
        // Player database
        MongoCollection<Document> collection = database.getCollection("players");

        // Initialize
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        ConnectionManager connectionManager = MinecraftServer.getConnectionManager();

        // Create instance
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(unit -> {
            final Point start = unit.absoluteStart();
            final Point size = unit.size();
            for (int x = 0; x < size.blockX(); x++) {
                for (int z = 0; z < size.blockZ(); z++) {
                    for (int y = 0; y < Math.min(40 - start.blockY(), size.blockY()); y++) {
                        unit.modifier().setBlock(start.add(x, y, z), Block.STONE);
                    }
                }
            }
        });

        // Player
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {

           final Player player = event.getPlayer();
           event.setSpawningInstance(instanceContainer);
           player.setRespawnPoint(new Pos(0, 42, 0));

            try {

                // "https://api.mojang.com/users/profiles/minecraft/" + player.getUsername()

                PlayerSkin playerSkin = PlayerSkin.fromUuid(PlayerUtils.getUUID(player.getUsername()));
                player.setSkin(playerSkin);

            } catch (Exception e) {
                player.kick("§cWe couldn't register your account. Please try and reconnect.");
                throw new RuntimeException(e);
            }

        });
        // TODO: Fix "Packet class net.minestom.server.network.packet.client.play.ClientSetRecipeBookStatePacket does not have any default listener!"

        // Registering
        registerEvents(globalEventHandler);
        registerCommands();

        // Start the server
        minecraftServer.start("0.0.0.0", 25565);

    }

    private static void registerEvents(GlobalEventHandler globalEventHandler){
        new BlockProtectionEvents().registerEvents(globalEventHandler);
    }

    private static void registerCommands(){

    }

}
