package net.literally.chunk.loader.initializer;

import net.literally.chunk.loader.block.ChunkLoaderBlock;
import net.literally.chunk.loader.entity.ChunkLoaderBlockEntity;
import net.literally.chunk.loader.loaders.LCLLoader;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public final class LCLEntities
{
    public static BlockEntityType<ChunkLoaderBlockEntity> CHUNK_LOADER_BLOCK_ENTITY;
    
    public static void initialize()
    {
        CHUNK_LOADER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, LCLLoader.MOD_ID + ":" + ChunkLoaderBlock.ID.getPath(), BlockEntityType.Builder.create(ChunkLoaderBlockEntity::new, LCLBlocks.CHUNK_LOADER_BLOCK).build(null));
    }
}
