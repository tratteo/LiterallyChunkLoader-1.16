package net.literally.chunk.loader.loaders;

import net.fabricmc.api.ModInitializer;
import net.literally.chunk.loader.initializer.LCLBlocks;
import net.literally.chunk.loader.initializer.LCLItems;
import net.literally.chunk.loader.initializer.LCLPersistentChunks;
import net.literally.chunk.loader.initializer.LCLTicker;

public class LCLLoader implements ModInitializer
{
    public static final String MOD_ID = "lchunkloader";
    
    @Override public void onInitialize()
    {
        LCLTicker.initialize();
        LCLBlocks.initialize();
        LCLItems.initialize();
        LCLPersistentChunks.initialize();
    }
}
