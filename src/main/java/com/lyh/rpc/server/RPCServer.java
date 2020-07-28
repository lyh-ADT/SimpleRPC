package com.lyh.rpc.server;

import com.lyh.rpc.server.RPCCallHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class RPCServer {
    /**
     * Starts this server in a new background thread. The background thread
     * inherits the priority, thread group and context class loader
     * of the caller.
     */
    public static void start() throws IOException {
        HttpServer server= HttpServer.create(new InetSocketAddress(10000), 0);
        server.createContext("/", new RPCCallHandler());
        server.start();
    }
}

