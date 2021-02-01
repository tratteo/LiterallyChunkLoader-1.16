package net.literally.chunk.loader.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.literally.chunk.loader.GUI.handler.ChunkLoaderGUIHandler;
import net.literally.chunk.loader.initializer.LCLEntities;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

public class ChunkLoaderBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ExtendedScreenHandlerFactory
{
    
    public ChunkLoaderBlockEntity()
    {
        super(LCLEntities.CHUNK_LOADER_BLOCK_ENTITY);
    }
    
    @Override public Text getDisplayName()
    {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
    
    @Override public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
    {
        return new ChunkLoaderGUIHandler(syncId, inv, ScreenHandlerContext.create(world, pos));
    }
    
    @Override public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf)
    {
        packetByteBuf.writeBlockPos(pos);
    }
}
