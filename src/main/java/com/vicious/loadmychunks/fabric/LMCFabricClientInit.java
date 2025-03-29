//? if fabric {
package com.vicious.loadmychunks.fabric;

import com.vicious.loadmychunks.client.LoadMyChunksClient;
import com.vicious.loadmychunks.common.integ.Integrations;
import net.fabricmc.api.ClientModInitializer;

public class LMCFabricClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LoadMyChunksClient.init();
        clientInit();
    }

    public static void clientInit(){
        //? if cc-tweaked
        Integrations.invokeWhenLoaded("computercraft","com.vicious.loadmychunks.fabric.integ.CCTFabric","clientInit",new Class[0]);
    }
}
//?}