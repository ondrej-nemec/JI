package utils.serverclient;

import java.io.IOException;

import utils.serverclient.Client;

public class ClientTest {

    public static void main(String[] args) {
        String name = args[0];
        
        LoggerImpl logger = new LoggerImpl();
        try {
            Client client = new Client("localhost", 8080, 30000, 10000, logger);
            
            for (int i = 0; i < 10; i++) {
                logger.log(
                    client.communicate(name + ": message " + i)
                );
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            logger.fatal("Client " + name + " main: " + e.getClass(), e);
        }
    }
    
}
