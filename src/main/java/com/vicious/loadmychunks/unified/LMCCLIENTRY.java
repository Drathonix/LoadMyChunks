package com.vicious.loadmychunks.unified;

import com.vicious.loadmychunks.client.LoadMyChunksClient;
import com.vicious.loadmychunks.common.LoadMyChunks;
//? if forge {
/*import com.vicious.loadmychunks.forge.LMCForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
*///?}
//? if fabric {
import com.vicious.loadmychunks.fabric.LMCFabricInit;
import net.fabricmc.api.ClientModInitializer;
//?}


//? if fabric {
public class LMCCLIENTRY implements ClientModInitializer {
        @Override
        public void onInitializeClient() {
                LoadMyChunksClient.init();
                LMCFabricInit.clientInit();
        }
}
//?}

//? elif forge {
/*@Mod.EventBusSubscriber(modid= LoadMyChunks.MOD_ID,bus= Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
public class LMCCLIENTRY {
        @SubscribeEvent
        public static void clientInit(FMLClientSetupEvent event) {
                LoadMyChunksClient.init();
                LMCForge.clientInit();
        }
}
*///?}

//? elif neoforge {
/*import com.vicious.loadmychunks.neoforge.LMCNeoInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
//? if >=1.20.6
/^import net.neoforged.fml.common.EventBusSubscriber;^/
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//? if <1.20.6
@Mod.EventBusSubscriber(modid=LoadMyChunks.MOD_ID,bus= Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
//? if >=1.20.6
/^@EventBusSubscriber(modid=LoadMyChunks.MOD_ID, bus= EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)^/
public class LMCCLIENTRY {
        @SubscribeEvent
        public static void clientInit(FMLClientSetupEvent event) {
                LoadMyChunksClient.init();
                LMCNeoInit.clientInit();
        }
}
*///?}

