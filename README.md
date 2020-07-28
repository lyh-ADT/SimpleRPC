# SimpleRPC

Simple RPC Framework implemented with Java. Using HTTP as transport protocol.

## Usage
#### server
```java
import com.lyh.rpc.server.RPCHandler;
import com.lyh.rpc.server.RPCServer;

public class Server{
    public static void main(String[] args){
        // start a http server
        RPCServer.start();
                                               
        // register to server
        RPCHandler handler = RPCHandler.getInstance();
        handler.register(new Interface_Implementation());
    }
}
```
#### client
```java
import com.lyh.rpc.client.ProxyFactory;

public class Server{
    public static void main(String[] args){
        // get proxy
        Interface test = ProxyFactory.getProxy(Interface.class);
        
        // rpc
        Object returnValue = test.method(info);
    }
}
```

