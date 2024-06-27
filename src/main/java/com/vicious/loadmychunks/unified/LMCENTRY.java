package com.vicious.loadmychunks.unified;

import com.vicious.loadmychunks.common.LoadMyChunks;


//? if >1.18.2
/*import com.vicious.loadmychunks.common.util.BoolArgument;*/


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
import com.vicious.loadmychunks.forge.LMCForge;
import net.minecraftforge.fml.common.Mod;

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

