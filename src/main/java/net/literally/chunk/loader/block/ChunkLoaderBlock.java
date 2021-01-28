package net.literally.chunk.loader.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.literally.chunk.loader.data.AreaData;
import net.literally.chunk.loader.data.CentreData;
import net.literally.chunk.loader.initializer.LCLItems;
import net.literally.chunk.loader.initializer.LCLPersistentChunks;
import net.literally.chunk.loader.loaders.LCLLoader;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

public class ChunkLoaderBlock extends Block
{
    public static final Identifier ID = new Identifier(LCLLoader.MOD_ID, "chunk_loader");
    public static final DirectionProperty FACING;
    public static final BooleanProperty ACTIVE;
    
    static
    {
        ACTIVE = BooleanProperty.of("active");
        FACING = HorizontalFacingBlock.FACING;
    }
    
    public ChunkLoaderBlock()
    {
        super(FabricBlockSettings.of(Material.METAL).breakByHand((true)).sounds(BlockSoundGroup.METAL).strength(1F, 1F).nonOpaque().build());
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos)
    {
        return Block.createCuboidShape(1F, 0F, 1F, 15F, 16F, 15F);
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        if(!world.isClient)
        {
            setActive(world, pos,state, !state.get(ACTIVE));
        }
        return ActionResult.SUCCESS;
    }
    
    private void setActive(World world, BlockPos pos, BlockState state, boolean active)
    {
        world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE, active), 0B1011);
        MinecraftServer server = world.getServer();
        AreaData newArea = new AreaData(pos.getX(), pos.getZ(), world.getRegistryKey().getValue().getPath());
        LCLPersistentChunks.toggleAreaState(server, newArea, active);
    }
    
    @Override public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        affectedBreak(world, pos, state, player, true);
        super.onBreak(world, pos, state, player);
    }
    
    public void affectedBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, boolean affectState)
    {
        if(!world.isClient && affectState)
        {
            MinecraftServer server = world.getServer();
            AreaData newArea = new AreaData(new CentreData(pos.getX(), pos.getZ()), world.getRegistryKey().getValue().getPath());
            LCLPersistentChunks.removePersistentArea(server, newArea);
        }
    }
    
    @Override public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        boolean isOn = (Boolean) state.get(ACTIVE);
        setActive(world, pos, state, !isOn);
        super.scheduledTick(state, world, pos, random);
    }
    
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved)
    {
        boolean isRecevingRedstonePower = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        if(isRecevingRedstonePower)
        {
            world.getBlockTickScheduler().schedule(pos, this, 8);
        }
    }
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack)
    {
        if(!world.isClient)
        {
            MinecraftServer server = world.getServer();
            AreaData newArea = new AreaData(pos.getX(), pos.getZ(), world.getRegistryKey().getValue().getPath());
            boolean correctPlacing = LCLPersistentChunks.canPlaceLoaderAt(newArea);
            if(!correctPlacing && placer instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) placer;
                ItemScatterer.spawn(world, pos, new SimpleInventory(new ItemStack(LCLItems.CHUNKLOADERITEM, 1)));
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                affectedBreak(world, pos, state, player, false);
                server.getPlayerManager().getPlayer(player.getUuid()).sendMessage(new LiteralText("Can't place a Loader within a 5 chunk radius from another Loader!"), MessageType.GAME_INFO, Util.NIL_UUID);
                return;
            }
            else
            {
                LCLPersistentChunks.addPersistentArea(server, newArea);
            }
            
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }
    
    
    @Override public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
    {
        return true;
    }
    
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
    {
        if(world.getBlockState(pos).get(ACTIVE))
        {
            double d = (double) pos.getX() + 0.65D - (double) (random.nextFloat() * 0.3F);
            double e = (double) pos.getY() + 1F - (double) (random.nextFloat() * 0.5F);
            double f = (double) pos.getZ() + 0.65D - (double) (random.nextFloat() * 0.3F);
            double g = (double) (0.4F - (random.nextFloat() + random.nextFloat()) * 0.4F);
            if(random.nextInt(6) == 0)
            {
                world.addParticle(ParticleTypes.END_ROD, d + 0.1F * g, e + 0.1F * g, f + 0.1F * g, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D);
            }
            double x = (double) pos.getX() + 0.65D - (double) (random.nextFloat() * 0.3F);
            double y = (double) pos.getY() + 2.75D;
            double z = (double) pos.getZ() + 0.65D - (double) (random.nextFloat() * 0.3F);
            world.addParticle(ParticleTypes.PORTAL, x, y, z, 0, -3D, 0);
        }
    }
    
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager)
    {
        stateManager.add(FACING).add(ACTIVE);
    }
    
    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }
    
    public BlockState rotate(BlockState state, BlockRotation rotation)
    {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    
    public BlockState mirror(BlockState state, BlockMirror mirror)
    {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
    
}
