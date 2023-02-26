package com.telepathicgrunt.zombieawarenessfix;

import com.mojang.datafixers.util.Either;
import com.telepathicgrunt.zombieawarenessfix.mixin.ServerChunkProviderAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Mod("zombieawarenessfix")
public class ZombieawarenessfixMod
{
    private static final Logger LOGGER = LogManager.getLogger();

    public ZombieawarenessfixMod() {}

    // Copy of ServerChunkProvider$getChunkFuture since it gets stripped on servers.
    public static CompletableFuture<Either<IChunk, ChunkHolder.IChunkLoadingError>> getChunkFuture(ServerChunkProvider serverChunkProvider, int x, int z, ChunkStatus chunkStatus, boolean forceLoad) {
        boolean flag = Thread.currentThread() == ((ServerChunkProviderAccessor)serverChunkProvider).getMainThread();
        CompletableFuture<Either<IChunk, ChunkHolder.IChunkLoadingError>> completablefuture;
        if (flag) {
            completablefuture = ((ServerChunkProviderAccessor)serverChunkProvider).callGetChunkFutureMainThread(x, z, chunkStatus, forceLoad);
            ((ServerChunkProviderAccessor)serverChunkProvider).getMainThreadProcessor().managedBlock(completablefuture::isDone);
        } else {
            completablefuture = CompletableFuture.supplyAsync(
                () -> ((ServerChunkProviderAccessor)serverChunkProvider).callGetChunkFutureMainThread(x, z, chunkStatus, forceLoad),
                ((ServerChunkProviderAccessor)serverChunkProvider).getMainThreadProcessor()
            ).thenCompose((eitherCompletableFuture) -> eitherCompletableFuture);
        }

        return completablefuture;
    }
}
