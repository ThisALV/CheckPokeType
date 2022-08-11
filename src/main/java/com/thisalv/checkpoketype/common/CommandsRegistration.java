package com.thisalv.checkpoketype.common;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandsRegistration {
    @SubscribeEvent
    public static void onRegisterCommandsEvent(RegisterCommandsEvent event) {
        TypeCommand.register(event.getDispatcher());
    }
}
