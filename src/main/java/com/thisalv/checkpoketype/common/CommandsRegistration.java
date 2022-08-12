package com.thisalv.checkpoketype.common;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class CommandsRegistration {
    private static final Logger logger = LogManager.getLogger("CheckPokeType");

    @SubscribeEvent
    public static void onRegisterCommandsEvent(RegisterCommandsEvent event) {
        logger.info("Register /type command");
        TypeCommand.register(event.getDispatcher());
    }
}
