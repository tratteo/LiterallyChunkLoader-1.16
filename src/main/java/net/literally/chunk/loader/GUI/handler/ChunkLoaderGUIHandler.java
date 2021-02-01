package net.literally.chunk.loader.GUI.handler;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.literally.chunk.loader.data.AreaData;
import net.literally.chunk.loader.data.SerializableChunkPos;
import net.literally.chunk.loader.entity.ChunkLoaderBlockEntity;
import net.literally.chunk.loader.initializer.LCLGUIHandlers;
import net.literally.chunk.loader.network.packets.packet.ForcedChunksC2SPacket;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ChunkLoaderGUIHandler extends SyncedGuiDescription
{
    ChunkLoaderBlockEntity loaderEntity;
    WToggleButton[][] buttonsMatrix;
    
    public ChunkLoaderGUIHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(LCLGUIHandlers.CHUNK_LOADER_SCREEN_HANDLER, syncId, playerInventory, null, null);
        context.run((world, pos) ->
        {
            loaderEntity = (ChunkLoaderBlockEntity) world.getBlockEntity(pos);
        });
        
        WPlainPanel root = new WPlainPanel();
        root.setSize(128, 140);
        
        
        setupButtonsGrid(root);
        
        WButton selectAll = new WButton(new LiteralText("All"));
        selectAll.setOnClick(() ->
        {
            ArrayList<SerializableChunkPos> data = new ArrayList<>();
            for(int i = 0; i < AreaData.SIZE; i++)
            {
                for(int j = 0; j < AreaData.SIZE; j++)
                {
                    buttonsMatrix[i][j].setToggle(true);
                    data.add(localToWorld(i, j));
                }
            }
            ForcedChunksC2SPacket pack = new ForcedChunksC2SPacket(data, loaderEntity.getPos().getX(), loaderEntity.getPos().getZ(), true);
            pack.send();
        });
        
        WButton selectNone = new WButton((new LiteralText("None")));
        selectNone.setOnClick(() ->
        {
            ArrayList<SerializableChunkPos> data = new ArrayList<>();
            for(int i = 0; i < AreaData.SIZE; i++)
            {
                for(int j = 0; j < AreaData.SIZE; j++)
                {
                    buttonsMatrix[i][j].setToggle(false);
                    data.add(localToWorld(i, j));
                }
            }
            ForcedChunksC2SPacket pack = new ForcedChunksC2SPacket(data, loaderEntity.getPos().getX(), loaderEntity.getPos().getZ(), false);
            pack.send();
        });
        
        root.add(selectAll, 16, 128, 40, 24);
        root.add(selectNone, 71, 128, 40, 24);
        setRootPanel(root);
    }
    
    public void refreshGUI(ArrayList<SerializableChunkPos> chunks, int x, int z)
    {
        SerializableChunkPos currentPos = new SerializableChunkPos(loaderEntity.getPos(), world.getRegistryKey().getValue().getPath());
        int offsetX = currentPos.getX() - (AreaData.SIZE / 2);
        int offsetZ = currentPos.getZ() - (AreaData.SIZE / 2);
        
        for(int i = 0; i < AreaData.SIZE; i++)
        {
            for(int j = 0; j < AreaData.SIZE; j++)
            {
                if(chunks.contains(new SerializableChunkPos(i + offsetX, j + offsetZ, currentPos.getDimension())))
                {
                    buttonsMatrix[j][i].setToggle(true);
                }
            }
        }
    }
    
    private void setupButtonsGrid(WPlainPanel root)
    {
        WLabel north = new WLabel(new LiteralText("N"));
        north.setHorizontalAlignment(HorizontalAlignment.CENTER);
        north.setVerticalAlignment(VerticalAlignment.CENTER);
        WLabel south = new WLabel(new LiteralText("S"));
        south.setHorizontalAlignment(HorizontalAlignment.CENTER);
        south.setVerticalAlignment(VerticalAlignment.CENTER);
        WLabel east = new WLabel(new LiteralText("E"));
        east.setHorizontalAlignment(HorizontalAlignment.CENTER);
        east.setVerticalAlignment(VerticalAlignment.CENTER);
        WLabel west = new WLabel(new LiteralText("W"));
        west.setHorizontalAlignment(HorizontalAlignment.CENTER);
        west.setVerticalAlignment(VerticalAlignment.CENTER);
        
        buttonsMatrix = new WToggleButton[AreaData.SIZE][AreaData.SIZE];
        for(int i = 0; i < AreaData.SIZE; i++)
        {
            int posY = (19 * i + 16);
            for(int j = 0; j < AreaData.SIZE; j++)
            {
                WToggleButton curr = new WToggleButton(new Identifier("lchunkloader:textures/gui/loaded.png"), new Identifier("lchunkloader:textures/gui/not_loaded.png"));
                int finalI = i;
                int finalJ = j;
                curr.setOnToggle((on) ->
                {
                    curr.setToggle(on);
                    ForcedChunksC2SPacket pack = new ForcedChunksC2SPacket(loaderEntity.getPos().getX(), loaderEntity.getPos().getZ(), on, localToWorld(finalI, finalJ));
                    pack.send();
                });
                int posX = 19 * j + 16;
                root.add(curr, posX, posY, 16, 16);
                buttonsMatrix[i][j] = curr;
            }
        }
        
        root.add(north, 54, 2, 16,16);
        root.add(south, 54, 109, 16,16);
        root.add(west, 2, 54, 16,16);
        root.add(east, 111, 54, 16,16);
    }
    
    private SerializableChunkPos localToWorld(int i, int j)
    {
        int offsetX = (loaderEntity.getPos().getX() >> 4) - (AreaData.SIZE / 2);
        int offsetZ = (loaderEntity.getPos().getZ() >> 4) - (AreaData.SIZE / 2);
        return new SerializableChunkPos(i + offsetX, j + offsetZ, world.getRegistryKey().getValue().getPath());
    }
}