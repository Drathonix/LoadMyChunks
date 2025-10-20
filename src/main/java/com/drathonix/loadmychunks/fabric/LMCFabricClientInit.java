//? if fabric {
package com.drathonix.loadmychunks.fabric;

import com.drathonix.loadmychunks.client.LoadMyChunksClient;
import com.drathonix.loadmychunks.common.integ.Integrations;
import net.fabricmc.api.ClientModInitializer;

public class LMCFabricClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LoadMyChunksClient.init();
        clientInit();
    }

    public static void clientInit(){
        //? if computercraft {
        Integrations.invokeWhenLoaded("computercraft","com.drathonix.loadmychunks.fabric.integ.CCTFabric","clientInit",new Class[0]);
        //?}
    }
}
//?}