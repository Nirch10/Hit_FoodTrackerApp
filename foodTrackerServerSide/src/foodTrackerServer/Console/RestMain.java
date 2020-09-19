package foodTrackerServer.Console;

import foodTrackerServer.API.AbstractHttpServer;
import foodTrackerServer.API.FoodTrackerHttpServer;
import foodTrackerServer.API.RestModelConnector;
import foodTrackerServer.Config.FoodTrackerServerConfig;
import foodTrackerServer.Config.FoodTrackerServerConfigWrapper;
import foodTrackerServer.lib.DAO.HibernateFoodTypeDAO;
import foodTrackerServer.lib.DAO.HibernateRecordDAO;
import foodTrackerServer.lib.DAO.HibernateUserDAO;
import foodTrackerServer.lib.UsersPlatformException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RestMain {
    public static void main(String args[]) throws IOException, UsersPlatformException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        FoodTrackerServerConfig config = FoodTrackerServerConfigWrapper.Deserialize("./Config.json");
        RestModelConnector restModelConnector =
                new RestModelConnector(new HibernateUserDAO(config), new HibernateFoodTypeDAO(config),
                        new HibernateRecordDAO(config));
        AbstractHttpServer server = new FoodTrackerHttpServer(1234, restModelConnector);
        server.start();
        System.out.println("Started serving - port : " + 1234);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
    }
}

