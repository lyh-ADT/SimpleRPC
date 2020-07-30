package com.lyh.rpc.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RPCServer {
    /**
     * Starts this server in a new background thread. The background thread
     * inherits the priority, thread group and context class loader
     * of the caller.
     */
    public static void start() throws IOException {
        start(new InetSocketAddress(10000));
    }

    public static void start(InetSocketAddress address) throws IOException {
        HttpServer server = HttpServer.create(address, 0);
        server.createContext("/", new RPCCallHandler());
        server.start();
    }
}

