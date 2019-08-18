package utils.serverclient;

import java.io.IOException;
import java.net.Socket;
import java.util.function.BiFunction;
import utils.serverclient.Server;

public class ServerTest {

    public static void main(String[] args) {
        LoggerImpl logger = new LoggerImpl();
        try {
            Server server = new Server(8080, 5, 10000, createFunction(), logger);
            server.start();
            for (int i = 0; i < 121; i++) {
                Thread.sleep(i * 1000);
                logger.log("THREAD COUNT: " + server.getActualThreadCount());
            }
            server.stop();
        } catch (IOException | InterruptedException e) {
            logger.fatal("Server main: " + e.getClass(), e);
        }
        
    }

    private static BiFunction<String, Socket, String> createFunction() {
        return (message, socket)-> {
            return message;
        };
    }
    
}
