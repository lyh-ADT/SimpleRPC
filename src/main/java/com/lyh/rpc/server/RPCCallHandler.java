package com.lyh.rpc.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class RPCCallHandler implements HttpHandler {
    private RPCHandler handler = RPCHandler.getInstance();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRemoteAddress().getHostString());
        InputStream requestBody = exchange.getRequestBody();

        Object returnValue = this.call(requestBody);

        exchange.sendResponseHeaders(200, 0);
        this.sendReturnValue(exchange.getResponseBody(), returnValue);
        exchange.close();
    }

    private Object call(InputStream stream){
        Map<String, Object> args = parse(stream);
        return handler.handle((String)args.get("className"), (String)args.get("methodName"), (Object[])args.get("args"));
    }

    private void sendReturnValue(OutputStream stream, Object value) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
        objectOutputStream.writeObject(value);
    }

    private Map<String, Object> parse(InputStream stream){
        Map<String, Object> result = new HashMap<>(3);
        try {
            result.putAll(readOne(stream));
            result.putAll(readOne(stream));
            ObjectInputStream objectInputStream = new ObjectInputStream(stream);
            result.put("args", objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, String> readOne(InputStream stream) throws IOException {
        final int lengthOfChar = 1;
        String key = "";
        String value = "";
        // 一个字符一个字符地读
        byte[] data = new byte[lengthOfChar];
        while(stream.read(data) == lengthOfChar){
            String t = new String(data, 0, lengthOfChar);
            if("=".equals(t)){
                break;
            }
            key += t;
        }
        while(stream.read(data) == lengthOfChar){
            String t = new String(data, 0, lengthOfChar);
            if("&".equals(t)){
                break;
            }
            value += t;
        }
        Map<String,String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
