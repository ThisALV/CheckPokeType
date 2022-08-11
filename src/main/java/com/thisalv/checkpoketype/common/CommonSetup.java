package com.thisalv.checkpoketype.common;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod("checkpoketype")
public class CommonSetup {
    public CommonSetup() {
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER,
                () -> (DistExecutor.SafeRunnable)
                        () -> FMLJavaModLoadingContext.get().getModEventBus().register(CommonSetup.class)
        );

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(
                () -> FMLNetworkConstants.IGNORESERVERONLY,
                (version, remote) -> true
        ));
    }

    @SubscribeEvent
    public static void onFmlCommonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(CommandsRegistration.class);
    }
}
