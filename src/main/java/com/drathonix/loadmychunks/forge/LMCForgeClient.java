//? if forge {
/*package com.drathonix.loadmychunks.forge;
import com.drathonix.loadmychunks.client.LoadMyChunksClient;
import com.drathonix.loadmychunks.common.LoadMyChunks;
import com.drathonix.loadmychunks.common.integ.Integrations;
import com.drathonix.loadmychunks.forge.LMCForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
@Mod.EventBusSubscriber(modid= LoadMyChunks.MOD_ID,bus= Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
public class LMCForgeClient {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
            LoadMyChunksClient.init();
            clientInit();
    }
    public static void clientInit() {
        //? if cc-tweaked
        Integrations.invokeWhenLoaded("computercraft","com.drathonix.loadmychunks.forge.integ.CCTForge","clientInit",new Class[0]);
    }
}
*///?}
