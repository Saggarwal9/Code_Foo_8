import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class Database {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_NAME = "test_backend3";
    private static final String SUPPRESS_SSL= "?verifyServerCertificate=false&UseSSL=true&createDatabaseIfNotExist=true";
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME+ SUPPRESS_SSL;
    private static Connection conn;
    private Statement statement;
    private int MAX_ROWS = 10; //Change this to increase the number of rows getting printed.

    private static String TABLE_NAME = "ign";

    public boolean open(String username, String password) {

        try {
            try {
                Class.forName(JDBC_DRIVER).newInstance();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }

        try {
            conn=DriverManager.getConnection(CONNECTION_STRING,username,password);
            statement = conn.createStatement();

        }catch(SQLException e) {
            return false;
        }finally {
            try {
                if(statement!=null)
                    statement.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }
        }

        return true;  
    }

    public void insertData(Data data) {
        Statement statement=null;
        try {
            statement=conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (GUID varchar(50) NOT NULL PRIMARY KEY, Category text, Title text, Description text, Date text, Link text, "
                    + "Slug text, Network text, State text, Tags text, Thumbnail text)" );
            //            System.out.println("INSERT INTO " + TABLE_NAME + " Values('" + data.getGuid() + "', '" + data.getCategory() + "', '" + data.getTitle()
            //            + "', '" + data.getDescription() + "', '" + data.getDate() + "', '" + data.getLink() + "', '" + data.getIgn_slug() + "', '" 
            //            + data.getIgn_network() + "', '" + data.getIgn_state() + "', '" + data.getIgn_tags() + "', '" + data.getIgn_thumbnail_compact() + "')");
            statement.execute("INSERT INTO " + TABLE_NAME + " Values('" + data.getGuid() + "', '" + data.getCategory() + "', '" + data.getTitle()
            + "', '" + data.getDescription() + "', '" + data.getDate() + "', '" + data.getLink() + "', '" + data.getIgn_slug() + "', '" 
            + data.getIgn_network() + "', '" + data.getIgn_state() + "', '" + data.getIgn_tags() + "', '" + data.getIgn_thumbnail_compact() + "')");
        }catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            System.out.println("Entry for GUID: " + data.getGuid() + " already exists. Proceeding with next entry."); 
            return;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                if(statement!=null)
                    statement.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void printData() {
        //Statement statement=null;

        //statement=conn.createStatement();
        //ResultSet results=statement.executeQuery("Select * from " + TABLE_NAME);

        DBTablePrinter.printTable(conn,TABLE_NAME,MAX_ROWS);
        //            while(results.next()) {
        //                System.out.println(results.getString("Title") + " : " + results.getString("Description"));
        //            }

        // TODO Auto-generated catch block


    }


    public boolean close() {
        try {
            //            Statement statement=conn.createStatement();
            //            statement.execute("DROP TABLE " + TABLE_NAME);
            //            statement.close();
            if(conn!=null) {
                conn.close();
                return true;
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
