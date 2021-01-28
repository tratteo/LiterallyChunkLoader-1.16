package net.literally.chunk.loader.initializer;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.literally.chunk.loader.data.AreaData;
import net.literally.chunk.loader.data.SerializedAreasData;
import net.literally.chunk.loader.saves.ChunksSerializeManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;

public final class LCLCommands
{
    
    public static void initialize()
    {
        CommandRegistry.INSTANCE.register(false, dispatcher ->
        {
            LCLLocate.define(dispatcher);
        });
    }
    
    private final static class LCLLocate
    {
        public static void define(CommandDispatcher<ServerCommandSource> dispatcher)
        {
            ArrayList<String> launchersList = new ArrayList<>();
            dispatcher.register(CommandManager.literal("lclocate").executes(ctx ->
            {
                SerializedAreasData areasData = ChunksSerializeManager.deserialize(ctx.getSource().getWorld().getServer().getSaveProperties().getLevelName());
                if(areasData == null)
                {
                    ctx.getSource().sendFeedback((new LiteralText("No loaders found")), false);
                    return 1;
                }
                ArrayList<AreaData> areas = areasData.getAreas();
                int size = areas.size();
                if(size == 0)
                {
                    ctx.getSource().sendFeedback((new LiteralText("No loaders found")), false);
                    return 1;
                }
                String response = "Found " + size + " placed loaders: \n";
                for(int i = 0; i < size; i++)
                {
                    AreaData current = areas.get(i);
                    response += "[" + current.getCentreData().getX() + ", " + current.getCentreData().getZ() + "]";
                    if(i < size - 1)
                    {
                        response += ", ";
                    }
                }
                ctx.getSource().sendFeedback((new LiteralText(response)), false);
                return 1;
            }));
        }
    }
}
