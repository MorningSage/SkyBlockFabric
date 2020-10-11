package morningsage.skyblockfabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;

public class SkyBlockFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Registry.register(Registry.CHUNK_GENERATOR, "skyblock", SkyBlockChunkGenerator.CODEC);

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(CommandManager.literal("island").executes(context -> {
				ServerPlayerEntity player = context.getSource().getPlayer();

				if (player == null) {
					// Run by Rcon?
					System.out.println("This command should be run by players.");
					return 0;
				}

				System.out.println("foo");
				return 1;
			}).then(CommandManager.literal("create").executes(context -> {
				System.out.println("bar");
				return 1;
			})));
		});

		System.out.println("Hello Fabric world!");
	}


}
