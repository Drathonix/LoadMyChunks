package com.vicious.loadmychunks.unified;

import com.vicious.loadmychunks.client.LoadMyChunksClient;
import com.vicious.loadmychunks.common.LoadMyChunks;
//? if forge {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
*///?}
//? if fabric {
/*import net.fabricmc.api.ClientModInitializer;
*///?}


//? if fabric {
/*public class LMCCLIENTRY implements ClientModInitializer {
        @Override
        public void onInitializeClient() {
                LoadMyChunksClient.init();
        }
}
*///?}

//? elif forge {
/*@Mod.EventBusSubscriber(modid= LoadMyChunks.MOD_ID,bus= Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
public class LMCCLIENTRY {
        @SubscribeEvent
        public static void clientInit(FMLClientSetupEvent event) {
                LoadMyChunksClient.init();
        }
}
*///?}

//? elif neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//? if <=1.20.5
/*@Mod.EventBusSubscriber(modid=LoadMyChunks.MOD_ID,bus= Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)*/
public class LMCCLIENTRY {
        @SubscribeEvent
        public static void clientInit(FMLClientSetupEvent event) {
                LoadMyChunksClient.init();
        }
}
//?}

