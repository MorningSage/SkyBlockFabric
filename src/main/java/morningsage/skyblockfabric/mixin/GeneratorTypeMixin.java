package morningsage.skyblockfabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import morningsage.skyblockfabric.SkyBlockUtils;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(GeneratorType.class)
public abstract class GeneratorTypeMixin {
    @Shadow @Final private Text translationKey;
    @Shadow @Final protected static List<GeneratorType> VALUES;

    public boolean isSkyblock() {
        return ((TranslatableText) this.translationKey).getKey().equals("generator.skyblock.skyblock");
    }

    @Inject(
        method = "<clinit>",
        at = @At("TAIL")
    )
    private static void addSkyblockGenerator(CallbackInfo ci) {
        // Return an instance of GeneratorType.DEFAULT which acts differently when it's called skyblock
        Class<?> clazz = GeneratorType.DEFAULT.getClass();
        GeneratorType skyblockGeneratorType;

        try {
            Constructor<?> ctor = clazz.getDeclaredConstructor(String.class);
            skyblockGeneratorType = (GeneratorType)ctor.newInstance("skyblock.skyblock");
            VALUES.add(skyblockGeneratorType);
        } catch (NoSuchMethodException|IllegalAccessException|InstantiationException|InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }

    @Inject(
        method = "createDefaultOptions",
        at = @At("HEAD"),
        cancellable = true
    )
    private void setToSkyblockChunkGenerator(DynamicRegistryManager.Impl registryManager, long seed, boolean generateStructures, boolean bonusChest, CallbackInfoReturnable<GeneratorOptions> cir) {
        if (this.isSkyblock()) {
            cir.setReturnValue(new GeneratorOptions(
                seed, generateStructures, bonusChest,
                GeneratorOptions.method_28608(
                    registryManager.get(Registry.DIMENSION_TYPE_KEY),
                    SkyBlockUtils.getSkyblockSimpleRegistry(registryManager.get(Registry.DIMENSION_TYPE_KEY), registryManager.get(Registry.BIOME_KEY), registryManager.get(Registry.NOISE_SETTINGS_WORLDGEN), seed),
                    SkyBlockUtils.createOverworldGenerator(registryManager.get(Registry.BIOME_KEY), registryManager.get(Registry.NOISE_SETTINGS_WORLDGEN), seed)
                )
            ));
        }
    }
}
