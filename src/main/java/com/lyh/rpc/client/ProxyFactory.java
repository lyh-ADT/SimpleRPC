package com.lyh.rpc.client;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProxyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz){

        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object returnValue = callRemote(clazz.getName(), method.getName(), args);
                return returnValue;
            }
        });
    }

    private static Object callRemote(String className, String methodName, Object[] args){
        String message = "className="+className+"&methodName="+methodName+"&";
        byte[] data = message.getBytes();
        try {
            URL url = new URL("http://localhost:10000");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            OutputStream outputStream = urlConnection.getOutputStream();

            outputStream.write(data);

            // 序列化参数
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(args);
            objectOutputStream.close();

            outputStream.close();
            urlConnection.getResponseCode();
            InputStream inputStream = urlConnection.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
