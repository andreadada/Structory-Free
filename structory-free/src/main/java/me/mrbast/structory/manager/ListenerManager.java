package me.mrbast.structory.manager;

import me.mrbast.structory.event.StructureEvent;
import me.mrbast.structory.event.StructureEventHandler;
import me.mrbast.structory.event.Listener;
import me.mrbast.structory.event.StructureEventPriority;
import me.mrbast.structory.option.Option;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ListenerManager {


    public static class Caller{
        private Method method;
        private Listener listener;


        public Caller(Method method, Listener listener){
            this.method = method;
            this.listener = listener;
            this.method.setAccessible(true);
        }

        public void call(StructureEvent event) throws InvocationTargetException, IllegalAccessException {
            this.method.invoke(listener, event);
        }
    }

    //private final Map<Class<? extends StructureEvent>, Set<Caller>> listeners = new HashMap<>();
    private final Map<Class<? extends StructureEvent>, TreeMap<StructureEventPriority, Set<Caller>>> prioritizedListeners = new HashMap<>();
    private final Map<Class<? extends StructureEvent>, Class<? extends StructureEvent>> willAlsoCall = new HashMap<>();


    private static final ListenerManager instance = new ListenerManager();
    public static ListenerManager getInstance() {return instance;}


    public void clear(){

        prioritizedListeners.clear();
        willAlsoCall.clear();

    }

    private void call(Class<? extends StructureEvent> clazz, StructureEvent event ) {


        Class<? extends StructureEvent>  superClazz = willAlsoCall.getOrDefault(clazz, null);
        if(superClazz != null){
            call(superClazz, event);
        }

        Map<StructureEventPriority, Set<Caller>> prioritySetMap = prioritizedListeners.get(clazz);
        if(prioritySetMap == null) return;

        prioritySetMap.forEach((priority, set) -> set.forEach(caller -> {
            try {
                caller.call(event);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void call(StructureEvent event){

        if(!prioritizedListeners.containsKey(event.getClass())){
            registerSuperClasses(event.getClass());
        }
        call(event.getClass(), event);

        /*

        NO PRIORITY

        Set<Caller> resolver = listeners.get(event.getClass());
        if(resolver == null || resolver.isEmpty()) return;

        resolver.forEach(x-> {
            try {
                x.call(event);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });;

         */

    }
    public void subscribe(Option option){

        for (Field field : option.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (!Listener.class.isAssignableFrom(field.getType())) continue;

            try {
                subscribe((Listener) field.get(option));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

    }
    public void subscribe(Listener listener) {


        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(StructureEventHandler.class)) {
                Class<?>[] paramTypes = method.getParameterTypes();

                if(paramTypes.length != 1) continue;

                if(!StructureEvent.class.isAssignableFrom(paramTypes[0])) continue;


                StructureEventHandler StructureEventHandler = method.getAnnotation(StructureEventHandler.class);

                StructureEventPriority priority = StructureEventHandler.priority();
                
                //Set<Caller> set = listeners.computeIfAbsent((Class<? extends StructureEvent>) paramTypes[0], x -> new HashSet<>());
                //set.add(new Caller(method, listener));

                Class<? extends StructureEvent> clazz = (Class<? extends StructureEvent>) paramTypes[0];
                register(method, listener, priority, clazz);


            }
        }


    }

    private void register(Method method, Listener listener, StructureEventPriority priority, Class<? extends StructureEvent> clazz){

        Map<StructureEventPriority, Set<Caller>> map = prioritizedListeners.computeIfAbsent(clazz, x -> new TreeMap<>(Comparator.comparingInt(StructureEventPriority::ordinal)));
        Set<Caller> set = map.computeIfAbsent(priority, x -> new HashSet<>());
        set.add(new Caller(method, listener));


        registerSuperClasses(clazz);


    }

    private void registerSuperClasses(Class<? extends StructureEvent> clazz){
        Class<? extends StructureEvent> subClass = clazz;
        Class<?> superclass = clazz.getSuperclass();
        while(superclass != null && StructureEvent.class.isAssignableFrom(superclass)){
            willAlsoCall.put(subClass, (Class<? extends StructureEvent>)superclass);
            subClass = (Class<? extends StructureEvent>) superclass;
            superclass = superclass.getSuperclass();
        }
    }
}
