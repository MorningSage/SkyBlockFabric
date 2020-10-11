package morningsage.skyblockfabric.mixin;

import morningsage.skyblockfabric.SkyBlockChunkGenerator;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    private static final int exitPortalHeight = 60;

    @Shadow private BlockPos exitPortalLocation;
    @Shadow @Final private ServerWorld world;

    @Inject(
        method = "generateEndPortal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/gen/feature/EndPortalFeature;configure(Lnet/minecraft/world/gen/feature/FeatureConfig;)Lnet/minecraft/world/gen/feature/ConfiguredFeature;",
            shift = At.Shift.BEFORE
        )
    )
    private void adjustExitPortalLocation(boolean open, CallbackInfo ci) {
        ChunkGenerator chunkGenerator = this.world.getChunkManager().getChunkGenerator();

        if (chunkGenerator instanceof SkyBlockChunkGenerator) {
            this.exitPortalLocation = new BlockPos(
                this.exitPortalLocation.getX(),
                exitPortalHeight,
                this.exitPortalLocation.getZ()
            );
        }
    }
}
