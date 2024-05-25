package com.vicious.loadmychunks;

import java.util.ArrayList;
import java.util.List;

/**
 * Added to ensure that nothing registers in the wrong order on the fabric side.
 */
public enum RegistryInit {
    TABS,
    BLOCKS,
    ITEMS,
    BLOCKENTITIES;

    public final List<Runnable> toExec = new ArrayList<>();

    public void run(){
        while(!toExec.isEmpty()){
            toExec.remove(0).run();
        }
    }

    public void queue(Runnable runnable){
        toExec.add(runnable);
    }
}
