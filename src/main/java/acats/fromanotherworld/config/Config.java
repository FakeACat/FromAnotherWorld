package acats.fromanotherworld.config;

import acats.fromanotherworld.FromAnotherWorld;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class Config {
    public static void load(){
        if (!getFolder().exists()){
            getFolder().mkdirs();
        }
        EntityClassification.load();
    }

    public static File getFolder() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), FromAnotherWorld.MODID + "/");
    }
}
