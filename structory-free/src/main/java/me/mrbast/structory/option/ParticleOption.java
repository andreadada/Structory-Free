package me.mrbast.structory.option;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.Parameters;

import me.mrbast.structory.async.StructureParticleScheduler;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.event.AltarCreateEvent;
import me.mrbast.structory.event.StructureEventHandler;
import me.mrbast.structory.event.Listener;
import me.mrbast.structory.particle.AltarParticle;
import me.mrbast.structory.particle.BoxSizedParticle;
import me.mrbast.structory.particle.FlameParticle;
import me.mrbast.structory.structure.Structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ParticleOption implements Option{


    private static final ParticleOption INSTANCE = new ParticleOption();
    public static ParticleOption getInstance() { return INSTANCE;}

    private final Map<String, Class<? extends AltarParticle>> stringAltarClassMap = new HashMap<>();


    private ParticleOption(){
        register(BoxSizedParticle.key().getKey().toUpperCase(), BoxSizedParticle.class);
        register(FlameParticle.key().getKey().toUpperCase(), FlameParticle.class);
    }
    public void register(String key, Class<? extends AltarParticle> clazz) {
        this.stringAltarClassMap.put(key, clazz);
    }



    public Class<? extends AltarParticle> getParticleClass(String type) {
        return stringAltarClassMap.get(type);
    }

    @Override
    public void read(Structure structure, ConfigSection configSection) {

        Optional<String> optType = configSection.read(String.class, "type");
        if(!optType.isPresent())  {return;}


        Class<? extends AltarParticle> clazz = stringAltarClassMap.get(optType.get());
        if(clazz == null) return;

        Optional<? extends AltarParticle> val = configSection.read(clazz, "", Parameters.create());
        val.ifPresent(altarParticle -> {
            StructureParticleScheduler.getInstance().add(structure, altarParticle);
        });
        /*
        try {
            Method method = clazz.getDeclaredMethod("read", ConfigSection.class, String.class, Parameters.class );
            method.setAccessible(true);
            altarParticle = (Optional<AltarParticle>) method.invoke(null, configSection, "", Parameters.create());
        } catch (ClassCastException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return;
        }

         */





    }

    @Override
    public void write(ConfigSection configSection) {

    }


    private final Listener createListener = new Listener(){


        @StructureEventHandler
        public void onCreate(AltarCreateEvent event){



        }

    };

    @Override
    public StructureSpacedKey getKey() {
        return StructureSpacedKey.OPTION_PARTICLE;
    }

    @Override
    public void init(Structure structure) {

    }

    @Override
    public void init() {

    }


}
