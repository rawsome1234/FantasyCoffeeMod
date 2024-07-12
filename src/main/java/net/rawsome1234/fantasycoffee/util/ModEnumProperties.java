package net.rawsome1234.fantasycoffee.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ModEnumProperties {

    public static enum BiomeTemperature implements StringRepresentable {
        TEMPERATE("temperate"){
        },
        COLD("cold"){
        },
        WARM("warm"){
        };


        public static final Codec<Biome.TemperatureModifier> CODEC = StringRepresentable.fromEnum(Biome.TemperatureModifier::values);

        public static BiomeTemperature toEnum(float temperature){
            if(temperature >= .9f){
                return BiomeTemperature.WARM;
            }
            else if (temperature <= 0){
                return BiomeTemperature.COLD;
            }
            return BiomeTemperature.TEMPERATE;
        }

        private final String name;
        BiomeTemperature(String pName) {
            this.name = pName;
        }

        public String getName() {
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }
    }

//    public static EnumProperty<BiomeTemperature> TEMP = new EnumProperty<BiomeTemperature>().

}
