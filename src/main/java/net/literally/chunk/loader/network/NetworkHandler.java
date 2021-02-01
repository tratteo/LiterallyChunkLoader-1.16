package net.literally.chunk.loader.network;


import net.literally.chunk.loader.network.packets.packet.CustomS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class NetworkHandler
{
    public static void sendToAll(CustomS2CPacket packet, PlayerManager manager)
    {
        List<ServerPlayerEntity> targets = manager.getPlayerList();
        for(int i = 0; i < targets.size(); ++i)
        {
            packet.sendTo(targets.get(i));
        }
    }
}
