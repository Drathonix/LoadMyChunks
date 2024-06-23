package com.vicious.loadmychunks.unified;

import com.vicious.loadmychunks.client.LoadMyChunksClient;
import net.fabricmc.api.ClientModInitializer;
//? if fabric {
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
//?}


//? if fabric {
public class LMCCLIENTRY implements ClientModInitializer {
        @Override
        public void onInitializeClient() {
                LoadMyChunksClient.init();
        }
}
//?}

//? elif forge {
/*public class LMCCLIENTRY {

}
*///?}

//? elif neoforge {
/*public class LMCCLIENTRY {
}
*///?}

