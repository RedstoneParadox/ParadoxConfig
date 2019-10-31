package redstoneparadox.paradoxconfig.mixin.client;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import redstoneparadox.paradoxconfig.ParadoxConfigKt;

@Mixin(Main.class)
public abstract class MainMixin {

    @Inject(method = "main", at = @At("HEAD"))
    private static void main(String[] strings_1, CallbackInfo ci) {
        ParadoxConfigKt.initConfigs();
    }
}
