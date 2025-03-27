//? if neoforge {
/*package com.vicious.loadmychunks.neoforge;

import com.vicious.loadmychunks.common.integ.Integrations;

import com.vicious.loadmychunks.neoforge.LMCNeoInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
//? if >=1.20.6
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//? if <1.20.6
/^@Mod.EventBusSubscriber(modid=LoadMyChunks.MOD_ID,bus= Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)^/
//? if >=1.20.6
@EventBusSubscriber(modid=LoadMyChunks.MOD_ID, bus= EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class LMCNeoforgeClient {
        @SubscribeEvent
        public static void clientInit(FMLClientSetupEvent event) {
                LoadMyChunksClient.init();
                clientInit();
        }
        public static void clientInit() {
            //? if cct
            Integrations.invokeWhenLoaded("computercraft","com.vicious.loadmychunks.neoforge.integ.CCTNeo","clientInit",new Class[0]);
        }
}*/
//?}
