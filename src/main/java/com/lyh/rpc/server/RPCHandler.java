package com.lyh.rpc.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RPCHandler {
    private RPCHandler(){}
    private static RPCHandler instance = new RPCHandler();
    public static RPCHandler getInstance(){
        return instance;
    }

    private Map<String, Object> impls = new HashMap<>();


    public void register(Object impl){
        for(Class<?> i : impl.getClass().getInterfaces()){
            impls.put(i.getName(), impl);
        }
    }

    Object handle(String className, String methodName, Object ...args){
        Object target = impls.get(className);
        Class<?>[] parameterTypes = new Class[args.length];
        for(int i=0; i < args.length; i++){
            parameterTypes[i] = args[i].getClass();
        }
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
