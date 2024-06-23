package com.vicious.loadmychunks.unified;

import com.vicious.loadmychunks.common.LoadMyChunks;
//? if fabric {
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
//?}


//? if fabric {
public class LMCENTRY implements ModInitializer {
        @Override
        public void onInitialize() {
                LoadMyChunks.init();
                ServerLifecycleEvents.SERVER_STARTED.register(LoadMyChunks::serverStarted);
                ServerLifecycleEvents.SERVER_STOPPED.register(LoadMyChunks::serverStopped);
        }
}
//?}

//? elif forge {
/*public class LMCENTRY {

}
*///?}

//? elif neoforge {
/*public class LMCENTRY {
}
*///?}

