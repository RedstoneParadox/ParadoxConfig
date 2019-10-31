package redstoneparadox.paradoxconfig.mixin.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import redstoneparadox.paradoxconfig.ParadoxConfigKt;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "main", at = @At("HEAD"))
    private static void main(String[] strings_1, CallbackInfo ci) {
        ParadoxConfigKt.initConfigs();
    }
}
