package net.literally.chunk.loader.network.packets.packet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

public abstract class CustomS2CPacket
{
    
    public CustomS2CPacket()
    {
    }
    
    public abstract void sendTo(PlayerEntity player);
    
    protected abstract void onReceive(MinecraftClient ctx);
    
    public abstract void write(PacketByteBuf buf);
}
