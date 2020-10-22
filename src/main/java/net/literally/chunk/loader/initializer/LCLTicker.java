package net.literally.chunk.loader.initializer;


import com.mojang.datafixers.util.Either;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.literally.chunk.loader.mixin.ChunkTicketManagerInvoker;
import net.literally.chunk.loader.mixin.ChunkTicketTypeAccessor;
import net.literally.chunk.loader.mixin.ThreadedAnvilChunkStorageInvoker;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.GameRules;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Optional;

public final class LCLTicker
{
    public static void initialize()
    {
        ServerTickEvents.START_WORLD_TICK.register(LCLTicker::tick);
    }
    
    private static void tick(ServerWorld world)
    {
        ThreadedAnvilChunkStorageInvoker storage = (ThreadedAnvilChunkStorageInvoker) world.getChunkManager().threadedAnvilChunkStorage;
        ChunkTicketManagerInvoker ticketManager = (ChunkTicketManagerInvoker) storage.invokeGetTicketManager();
        int randomTickSpeed = world.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED);
        if(randomTickSpeed == 0)
        {
            return;
        }
        storage.invokeEntryIterator().forEach(chunkHolder ->
        {
            // Ensure the chunk is force loaded rather than a "regular" chunk outside the 128-block radius
            boolean forced = ticketManager.invokeGetTicketSet(chunkHolder.getPos().toLong()).stream().anyMatch(chunkTicket ->
                  ((ChunkTicketTypeAccessor) chunkTicket.getType()).getName().equals("forced"));
            if(!forced)
            {
                return;
            }
            
            Optional<WorldChunk> optionalWorldChunk = ((Either) chunkHolder.getTickingFuture().getNow(ChunkHolder.UNLOADED_WORLD_CHUNK)).left();
            if(!optionalWorldChunk.isPresent())
            {
                return;
            }
            // Make sure it's too far to get regular random ticks
            if(storage.invokeIsTooFarFromPlayersToSpawnMobs(chunkHolder.getPos()))
            {
                WorldChunk chunk = optionalWorldChunk.get();
                Profiler profiler = world.getProfiler();
                int startX = chunk.getPos().getStartX();
                int startZ = chunk.getPos().getStartZ();
                for(ChunkSection chunkSection : chunk.getSectionArray())
                {
                    if(chunkSection != WorldChunk.EMPTY_SECTION && chunkSection.hasRandomTicks())
                    {
                        int yOffset = chunkSection.getYOffset();
                        for(int m = 0; m < randomTickSpeed; m++)
                        {
                            BlockPos randomPosInChunk = world.getRandomPosInChunk(startX, yOffset, startZ, 15);
                            profiler.push("randomTick");
                            BlockState blockState = chunkSection.getBlockState(randomPosInChunk.getX() - startX,
                                  randomPosInChunk.getY() - yOffset,
                                  randomPosInChunk.getZ() - startZ);
                            if(blockState.hasRandomTicks())
                            {
                                blockState.randomTick(world, randomPosInChunk, world.random);
                            }
                            FluidState fluidState = blockState.getFluidState();
                            if(fluidState.hasRandomTicks())
                            {
                                fluidState.onRandomTick(world, randomPosInChunk, world.random);
                            }
                            profiler.pop();
                        }
                    }
                }
            }
        });
    }
}
