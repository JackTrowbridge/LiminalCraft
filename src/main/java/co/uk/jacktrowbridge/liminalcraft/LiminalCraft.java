package co.uk.jacktrowbridge.liminalcraft;

import net.minestom.server.MinecraftServer;

public class LiminalCraft {

    public static void main(String[] args){

        // Initialize
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Start the server
        minecraftServer.start("0.0.0.0", 25565);

    }

}
