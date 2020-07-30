package com.lyh.rpc.client;

import java.io.*;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProxyFactory {
    private static URL url;

    static {
        try {
            ProxyFactory.setUrl("http://localhost:10000");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void setUrl(String url) throws MalformedURLException {
        ProxyFactory.url = new URL(url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz) {

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> callRemote(clazz.getName(), method.getName(), args));
    }

    private static Object callRemote(String className, String methodName, Object[] args) {
        String message = "className="+className+"&methodName="+methodName+"&";
        byte[] data = message.getBytes();
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            OutputStream outputStream = urlConnection.getOutputStream();

            outputStream.write(data);

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
