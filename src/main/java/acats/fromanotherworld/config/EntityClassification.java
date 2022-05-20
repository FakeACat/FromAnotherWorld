package acats.fromanotherworld.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;

import java.io.*;
import java.util.Objects;

public class EntityClassification {

    private static final String[] HUMANOIDS = {
            "minecraft:villager",
            "minecraft:wandering_trader",
            "minecraft:piglin",
            "minecraft:evoker",
            "minecraft:piglin_brute",
            "minecraft:pillager",
            "minecraft:vindicator",
            "minecraft:witch"
    };

    private static final String[] LARGE_QUADRUPEDS = {
            "minecraft:hoglin",
            "minecraft:ravager"
    };

    private static final String[] QUADRUPEDS = {
            "minecraft:cat",
            "minecraft:cow",
            "minecraft:donkey",
            "minecraft:fox",
            "minecraft:horse",
            "minecraft:mooshroom",
            "minecraft:mule",
            "minecraft:ocelot",
            "minecraft:pig",
            "minecraft:sheep"
    };

    private static final String[] SMALL = {
            "minecraft:chicken",
            "minecraft:parrot",
            "minecraft:silverfish"
    };

    private static final String[] ATTACKABLE_BUT_NOT_ASSIMILABLE = {
            "minecraft:iron_golem",
            "minecraft:snow_golem",
            "minecraft:wither"
    };

    private static File getFile(){
        return new File(Config.getFolder(), "entity_classification.json");
    }

    public static void load(){
        if (!getFile().exists()){
            genFile();
        }
    }

    private static void genFile(){
        JsonObject cfg = new JsonObject();
        cfg.addProperty("Humanoids", createString(HUMANOIDS));
        cfg.addProperty("LargeQuadrupeds", createString(LARGE_QUADRUPEDS));
        cfg.addProperty("Quadrupeds", createString(QUADRUPEDS));
        cfg.addProperty("Small", createString(SMALL));
        cfg.addProperty("AttackableButNotAssimilable", createString(ATTACKABLE_BUT_NOT_ASSIMILABLE));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            getFile().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            fileWriter.write(gson.toJson(cfg));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createString(String[] stringArray){
        if (stringArray == null)
            return "null";

        int iMax = stringArray.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(stringArray[i]);
            if (i == iMax)
                return b.toString();
            b.append(", ");
        }
    }

    private static boolean isX(Entity entity, String x){
        try {
            Reader reader = new FileReader(getFile());
            String[] h = new Gson().fromJson(reader, JsonObject.class).get(x).getAsString().replace(" ", "").split(",");
            for (String s:
                    h) {
                if (Objects.equals(s, EntityType.getId(entity.getType()).toString())){
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isHumanoid(Entity entity){
        return isX(entity, "Humanoids");
    }

    public static boolean isLargeQuadruped(Entity entity){
        return isX(entity, "LargeQuadrupeds");
    }

    public static boolean isQuadruped(Entity entity){
        return isX(entity, "Quadrupeds");
    }

    public static boolean isSmall(Entity entity){
        return isX(entity, "Small");
    }

    public static boolean isAttackableButNotAssimilable(Entity entity){
        return isX(entity, "AttackableButNotAssimilable") || (entity instanceof PlayerEntity p && !p.isCreative() && !p.isSpectator());
    }
}
