package com.telepathicgrunt.zombieawarenessfix.mixin;

import com.corosus.zombieawareness.ZAUtil;
import com.telepathicgrunt.zombieawarenessfix.ZombieawarenessfixMod;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ExecutionException;

@Mixin(ZAUtil.class)
public class ZAUtilMixin {
    @Inject(method = "canSpawnTrace(Lnet/minecraft/world/World;DDD)Z",
            at = @At(value = "HEAD"),
            cancellable = true,
            remap = false)
    private static void zombieawarenessfix$levelcheck(World world, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) throws ExecutionException, InterruptedException {
        if (world instanceof ServerWorld) {
            if (!ZombieawarenessfixMod.getChunkFuture(((ServerWorld)world).getChunkSource(), (int) x, (int) z, ChunkStatus.FULL, false).get().left().isPresent()) {
                cir.setReturnValue(true);
            }
        }
    }
}