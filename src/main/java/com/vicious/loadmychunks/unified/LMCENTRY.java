package com.vicious.loadmychunks.unified;

import com.vicious.loadmychunks.common.LoadMyChunks;


//? if >1.18.2
import com.vicious.loadmychunks.common.util.BoolArgument;


//? if fabric {
/*import com.vicious.loadmychunks.fabric.LMCFabricInit;
import net.fabricmc.api.ModInitializer;
public class LMCENTRY implements ModInitializer {
        @Override
        public void onInitialize() {
                LoadMyChunks.init();
                LMCFabricInit.init();
        }
}
*///?}

//? elif forge {
//? if <=1.16.5
/*import me.shedaniel.architectury.platform.forge.EventBuses;*/

//? if >1.18.2 {
import com.vicious.loadmychunks.forge.LMCForge;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
//? if >1.19.3
import net.minecraft.core.registries.Registries;
//?}
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
//? if >1.16.5 {
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import dev.architectury.platform.forge.EventBuses;
//?}
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//? if <=1.16.5 {
/*import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
*///?}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
@Mod(LoadMyChunks.MOD_ID)
public class LMCENTRY {
    public LMCENTRY() {
        LMCForge.init();
    }
}
//?}

//? elif neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.vicious.loadmychunks.neoforge.LMCNeoInit;
@Mod(LoadMyChunks.MOD_ID)
public class LMCENTRY {
    public LMCENTRY(IEventBus meb) {
        LoadMyChunks.init();
        LMCNeoInit.init(meb);
    }
}
*///?}

