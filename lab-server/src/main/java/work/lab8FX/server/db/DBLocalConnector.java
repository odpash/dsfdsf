package work.lab8FX.server.db;


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

public class DBLocalConnector implements DBConnectable {

    private final String dbUrl = "jdbc:postgresql://localhost:5432/s335097db";
    private final String user = "postgres";
    private final String pass = "991155";

    public DBLocalConnector() {
        try {
            initializeDB();
        } catch (SQLException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Error occurred during initializing tables!" + e.getMessage()));
            System.exit(1);
        }
    }

    public void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }


    private void initializeDB() throws SQLException {

        Connection connection = DriverManager.getConnection(dbUrl, user, pass);

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
