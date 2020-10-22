package net.literally.chunk.loader.mixin;


import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageInvoker
{
    @Invoker("isTooFarFromPlayersToSpawnMobs")
    boolean invokeIsTooFarFromPlayersToSpawnMobs(ChunkPos arg);
    
    @Invoker("entryIterator")
    Iterable<ChunkHolder> invokeEntryIterator();
    
    @Invoker("getTicketManager")
    ThreadedAnvilChunkStorage.TicketManager invokeGetTicketManager();
}