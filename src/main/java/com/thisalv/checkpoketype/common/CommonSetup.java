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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("checkpoketype")
@SuppressWarnings("unused")
public class CommonSetup {
    private static final Logger logger = LogManager.getLogger("CheckPokeType");

    public CommonSetup() {
        logger.info("Setup for logical server usage.");

        logger.info("Prepare onFmlCommonSetup subscriber");
        FMLJavaModLoadingContext.get().getModEventBus().register(CommonSetup.class);

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(
                () -> FMLNetworkConstants.IGNORESERVERONLY,
                (version, remote) -> true
        ));
    }

    @SubscribeEvent
    public static void onFmlCommonSetup(FMLCommonSetupEvent event) {
        logger.info("Prepare CommandsRegistration event listeners");
        MinecraftForge.EVENT_BUS.register(CommandsRegistration.class);
    }
}
