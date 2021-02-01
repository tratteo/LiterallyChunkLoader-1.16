package net.literally.chunk.loader.network.packets.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class CustomC2SPacket
{
    
    public CustomC2SPacket()
    {
    }
    
    public abstract void send();
    
    public abstract void onReceive(MinecraftServer server, ServerPlayerEntity servPlayer, ServerPlayNetworkHandler handler, PacketSender sender);
    
    public abstract void write(PacketByteBuf buf);
}
