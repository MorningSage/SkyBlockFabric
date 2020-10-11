package morningsage.skyblockfabric.mixin;

import com.mojang.serialization.Lifecycle;
import morningsage.skyblockfabric.SkyBlockChunkGenerator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin {
    @Shadow @Final private GeneratorOptions generatorOptions;

    // Removes the annoying "This is experimental; Thar Be Dragons" message before starting any world with a skyblock chunk generator
    @Inject(
        method = "getLifecycle",
        at = @At("HEAD"),
        cancellable = true
    )
    @Environment(EnvType.CLIENT)
    private void markAsStable(CallbackInfoReturnable<Lifecycle> cir) {
        if (this.generatorOptions.getChunkGenerator() instanceof SkyBlockChunkGenerator) {
            cir.setReturnValue(Lifecycle.stable());
        }
    }
}
