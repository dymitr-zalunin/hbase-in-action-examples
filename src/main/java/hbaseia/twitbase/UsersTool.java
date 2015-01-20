package hbaseia.twitbase;

import hbaseia.twitbase.hbase.UsersDAO;
import hbaseia.twitbase.model.User;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class UsersTool {

    private static final Logger log = Logger.getLogger(UsersTool.class);

    public static final String usage =
            "usertool action ...\n" +
                    "  add user name email password - add a new user.\n" +
                    "  get user - retrieve a specific user.\n";

    public static void main(String[] args) throws IOException {
        if (args.length == 0 || "help".equals(args[0])) {
            System.out.println(usage);
            System.exit(0);
        }

        Configuration configuration = HBaseConfiguration.create();
        HConnection connection = HConnectionManager.createConnection(configuration);
        UsersDAO dao = new UsersDAO(connection);

        if ("get".equals(args[0])) {
            log.debug(String.format("Getting user %s", args[1]));
            User u = dao.getUser(args[1]);
            System.out.println(u);
        }

        if ("add".equals(args[0])) {
            log.debug("Adding user...");
            dao.addUser(args[1], args[2], args[3], args[4]);
            User user = dao.getUser(args[1]);
            System.out.println("Successfully added user" + user);
        }

        if ("list".equals(args[0])) {
            List<User> users = dao.getUsers();
            log.info(String.format("Found %s users.", users.size()));
            for(User u : users) {
                System.out.println(u);
            }
        }

        connection.close();

    }

}