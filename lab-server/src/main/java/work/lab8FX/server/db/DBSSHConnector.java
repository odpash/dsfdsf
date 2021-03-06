package work.lab8FX.server.db;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import work.lab8FX.common.exceptions.DatabaseException;
import work.lab8FX.common.util.TextColoring;
import work.lab8FX.server.ServerConfig;
import work.lab8FX.server.interfaces.DBConnectable;
import work.lab8FX.server.interfaces.SQLConsumer;
import work.lab8FX.server.interfaces.SQLFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;

public class DBSSHConnector implements DBConnectable {

    private static Session session;
    private final String dbBase = "jdbc:postgresql://";
    private final String dbName = "studs";
    private final int dbPort = 5432;
    private final String dbHost = "pg";
    private final int sshPort = 2222;
    private String svLogin;
    private String svPass;
    private String svAddr;
    private int forwardingPort;


    public DBSSHConnector() {
        try {
            this.svLogin = System.getenv("SV_LOGIN");
            this.svPass = System.getenv("SV_PASS");
            this.svAddr = System.getenv("SV_ADDR");
            this.forwardingPort = Integer.parseInt(System.getenv("FORWARDING_PORT"));
            connectSSH();
            initializeDB();
        } catch (SQLException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Error occurred during initializing tables!"));
            e.printStackTrace();
            System.exit(1);
        } catch (JSchException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Troubles during connecting to DB with ssh!"));
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Mistakes in environment variables!"));
            System.exit(1);
        }

    }

    public static void closeSSH() {
        if (session != null) {
            session.disconnect();
        }
    }

    private void connectSSH() throws JSchException {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        session = jsch.getSession(svLogin, svAddr, sshPort);
        session.setPassword(svPass);
        session.setConfig(config);
        session.connect();
        session.setPortForwardingL(forwardingPort, dbHost, dbPort);
    }

    public void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbBase + "localhost:" + forwardingPort + "/" + dbName, svLogin, svPass)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbBase + "localhost:" + forwardingPort + "/" + dbName, svLogin, svPass)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }


    private void initializeDB() throws SQLException {

        Connection connection = DriverManager.getConnection(dbBase + "localhost:" + forwardingPort + "/" + dbName, svLogin, svPass);

        Statement statement = connection.createStatement();

        statement.execute("CREATE SEQUENCE IF NOT EXISTS s335097musicbands_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE SEQUENCE IF NOT EXISTS s335097users_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE TABLE IF NOT EXISTS s335097users "
                + "("
                + "login varchar(255) NOT NULL UNIQUE CHECK(login<>''),"
                + "password varchar(255) NOT NULL CHECK(password<>''),"
                + "id bigint NOT NULL PRIMARY KEY DEFAULT nextval('s335097users_id_seq')"
                + ");");

        statement.execute("CREATE TABLE IF NOT EXISTS s335097musicbands "
                + "("
                + "id bigint NOT NULL PRIMARY KEY DEFAULT nextval('s335097musicbands_id_seq'),"
                + "creationDate date NOT NULL,"
                + "name varchar(100) NOT NULL CHECK(name<>''),"
                + "x float NOT NULL CHECK(x <= 947),"
                + "y float NOT NULL CHECK(y <= 104),"
                + "numberOfParticipants bigint NOT NULL CHECK(numberOfParticipants > 0),"
                + "description varchar(300),"
                + "musicGenre varchar(21) CHECK(musicGenre = 'PROGRESSIVE_ROCK' "
                + "OR musicGenre = 'PSYCHEDELIC_CLOUD_RAP' "
                + "OR musicGenre = 'JAZZ' "
                + "OR musicGenre = 'BLUES' "
                + "OR musicGenre = 'BRIT_POP'),"
                + "studioAddress varchar(100),"
                + "owner_id bigint NOT NULL REFERENCES s335097users (id)"
                + ");");

        connection.close();
    }
}
