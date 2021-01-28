package net.literally.chunk.loader.loaders;

import net.fabricmc.api.ModInitializer;
import net.literally.chunk.loader.initializer.*;

public class LCLLoader implements ModInitializer
{
    public static final String MOD_ID = "lchunkloader";
    
    @Override public void onInitialize()
    {
        LCLTicker.initialize();
        LCLBlocks.initialize();
        LCLItems.initialize();
        LCLPersistentChunks.initialize();
        LCLCommands.initialize();
    }
}
