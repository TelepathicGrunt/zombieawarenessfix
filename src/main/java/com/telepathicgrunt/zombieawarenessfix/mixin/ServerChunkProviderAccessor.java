package com.telepathicgrunt.zombieawarenessfix.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.concurrent.CompletableFuture;

@Mixin(ServerChunkProvider.class)
public interface ServerChunkProviderAccessor {
    @Accessor("mainThread")
    Thread getMainThread();

    @Accessor("mainThreadProcessor")
    ServerChunkProvider.ChunkExecutor getMainThreadProcessor();

    @Invoker("getChunkFutureMainThread")
    CompletableFuture<Either<IChunk, ChunkHolder.IChunkLoadingError>> callGetChunkFutureMainThread(int p_217233_1_, int p_217233_2_, ChunkStatus p_217233_3_, boolean p_217233_4_);
}
