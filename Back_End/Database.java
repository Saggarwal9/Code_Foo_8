import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;



public class Database {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_NAME = "test_backend11";
    private static final String SUPPRESS_SSL= "?verifyServerCertificate=false&UseSSL=true&createDatabaseIfNotExist=true";
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME+ SUPPRESS_SSL;
    private static Connection conn;
    private Statement statement;
    private int MAX_ROWS = 0; //Change this to increase the number of rows getting printed.
    private static int BACKEND_ID=1;
    private static int VIDEO_ID =1;
    private static int ARTICLE_ID=1;

    private static String ARTICLE_TABLE_NAME = "ign_articles"; //Table for Articles
    private static String VIDEO_TABLE_NAME = "ign_videos"; //Table for videos
    private static String BACK_END_TABLE_NAME = "ign_backend"; // Table for data under elements like ign_slur, ign_tags etc.
    private static String PAGE_TABLE = "ign_page";
    private static String PAGE_BACK_END_TABLE = "ign_page_backend";
    
    
    
    public static void openURL(String url) {
        Desktop desktop=Desktop.getDesktop();
            try {
                desktop.browse(new URL(url).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
    }
    
    
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
            statement.execute("DROP TABLE IF EXISTS " + VIDEO_TABLE_NAME);
            statement.execute("DROP TABLE IF EXISTS " + ARTICLE_TABLE_NAME);
            statement.execute("DROP TABLE IF EXISTS " + BACK_END_TABLE_NAME);

        }catch(SQLException e) {
            return false;
        }finally {
            createTables();
            try {
                if(statement!=null)
                    statement.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }
        }

        return true;  
    }


    public void createTables() {

        try {
            statement.execute("CREATE TABLE IF NOT EXISTS " + ARTICLE_TABLE_NAME + " (ID int, GUID varchar(50) NOT NULL PRIMARY KEY, Title text, Description text, Date text, Link text, id_backend varchar(3))");
            statement.execute("CREATE TABLE IF NOT EXISTS " + VIDEO_TABLE_NAME + " (ID int, GUID varchar(50) NOT NULL PRIMARY KEY, Title text, Description text, Date text, Link text, id_backend  varchar(3))");
            statement.execute("CREATE TABLE IF NOT EXISTS " + BACK_END_TABLE_NAME + " (ID int PRIMARY KEY, Slug text, Network text, State text, Tags text, "
                    + "Thumbnail_compact text, Thumbnail_Medium text, Thumbnail_Large text)" );
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void insertData(Data data) {
        Statement statement=null;
        try {
            statement=conn.createStatement();
            //Normalized into three tables.
            if(data.getCategory().equals("article")) {
                statement.execute("INSERT INTO " + ARTICLE_TABLE_NAME + " Values(" + ARTICLE_ID++ + ", '" + data.getGuid() + "', '" + data.getTitle()
                + "', '" + data.getDescription() + "', '" + data.getDate() + "', '" + data.getLink() + "', '" + BACKEND_ID +"')");
            }
            else if(data.getCategory().equals("video")) {
                statement.execute("INSERT INTO " + VIDEO_TABLE_NAME + " Values("+ VIDEO_ID++ + ", '" + data.getGuid() + "', '" + data.getTitle()
                + "', '" + data.getDescription() + "', '" + data.getDate() + "', '" + data.getLink() + "', '" + BACKEND_ID +"')");

            }
            statement.execute("INSERT INTO " + BACK_END_TABLE_NAME + " VALUES(" + BACKEND_ID++ + ", '" + data.getIgn_slug() + "', '" 
                    + data.getIgn_network() + "', '" + data.getIgn_state() + "', '" + data.getIgn_tags() + "', '" + data.getIgn_thumbnail_compact() + "', '" + data.getIgn_thumbnail_medium() + "', '" + data.getIgn_thumbnail_large() + "')");



        }catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
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

    /**
     * TODO: Add the choice to Join back_end table
     * TODO: Add the choice to select an article/video by the index and open the video.
     * @param choice
     */
    public void printData(String choice) {
        MAX_ROWS+=10;
        String choice2="";
        Scanner scanner=new Scanner(System.in);
        System.out.println("Would you like to view the back end data? (Enter yes or no)");
        do {
            choice2=scanner.nextLine();
            if(choice2.toLowerCase().equals("yes") || choice2.toLowerCase().equals("no"))
                break;
            else {
                System.out.println("Invalid choice. Please enter either yes or no.");
                continue;
            }
        }while(true);
        try {
            statement=conn.createStatement();
            switch(choice) {
            case "article":
                if(choice2.equals("no"))
                    DBTablePrinter.printTable(conn, ARTICLE_TABLE_NAME, MAX_ROWS);
                else {
                    ResultSet results = statement.executeQuery("SELECT * FROM " + ARTICLE_TABLE_NAME
                            + " INNER JOIN " + BACK_END_TABLE_NAME + " on " + ARTICLE_TABLE_NAME + ".id_backend = "
                            + BACK_END_TABLE_NAME + ".ID");
                    DBTablePrinter.printResultSet(results,MAX_ROWS);
                    
                }
                System.out.println("Enter the ID of the link you want to open. Enter 0 to exit.");
                int choice3= scanner.nextInt();
                if(choice3>0) {
                ResultSet resultSet=statement.executeQuery("SELECT * From "  + ARTICLE_TABLE_NAME + " WHERE ID=" + choice3);
                    resultSet.next();
                    String url=resultSet.getString("Link");
                    openURL(url);
                }
                break;
            case "video":
                if(choice2.toLowerCase().equals("yes"))
                    DBTablePrinter.printTable(conn, VIDEO_TABLE_NAME, MAX_ROWS);
                else {
                    ResultSet results = statement.executeQuery("SELECT * FROM " + VIDEO_TABLE_NAME
                            + " INNER JOIN " + BACK_END_TABLE_NAME + " on " + VIDEO_TABLE_NAME + ".id_backend = "
                            + BACK_END_TABLE_NAME + ".ID");
                    DBTablePrinter.printResultSet(results,MAX_ROWS);
                    
                }
                System.out.println("Enter the ID of the link you want to open. Enter 0 to exit.");
                int choice4= scanner.nextInt();
                if(choice4>0) {
                ResultSet resultSet=statement.executeQuery("SELECT * From "  + VIDEO_TABLE_NAME + " WHERE ID=" + choice4);
                    resultSet.next();
                    String url=resultSet.getString("Link");
                    openURL(url);
                }
                break;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    public void printPage(String choice,ArrayList<Data> dataSet) {
        Scanner scanner = new Scanner(System.in);
        try {
            statement=conn.createStatement();
            int backend_id =1;
            int page_id = 1;
            statement.execute("DROP TABLE IF EXISTS " + PAGE_TABLE);
            statement.execute("DROP TABLE IF EXISTS " + PAGE_BACK_END_TABLE);
            statement.execute("CREATE TABLE " + PAGE_TABLE + 
                    " (ID int NOT NULL PRIMARY KEY,GUID varchar(50), Title text, Description text, Date text, Link text,Backend_ID int)");
            statement.execute("CREATE TABLE " + PAGE_BACK_END_TABLE + 
                    " (ID int PRIMARY KEY, Slug text, Network text, State text, Tags text, Thumbnail_Compact text, Thumbnail_medium text, Thumbnail_large text)");

            for(Data data:dataSet) {
                if(data.getCategory().equals(choice)) {
                    statement.execute("INSERT INTO " + PAGE_TABLE + "(ID,GUID,Title,Description,Date,Link,Backend_ID) " + " Values(" + page_id++ + ",' " + data.getGuid() + "', '" + data.getTitle() +
                            "', '" + data.getDescription() + "', '" + data.getDate() + "', '" + data.getLink() + "', " + backend_id + ")");
                    statement.execute("INSERT INTO " + PAGE_BACK_END_TABLE + " Values(" + backend_id++ + ", '" + data.getIgn_slug() + "', '"
                            + data.getIgn_network() + "', '" + data.getIgn_state() + "', '" + data.getIgn_tags() + "', '" +
                            data.getIgn_thumbnail_compact() + "', '" + data.getIgn_thumbnail_medium() + "', '" + data.getIgn_thumbnail_large() + "')");   

                }
            }
            do {
                System.out.println("Would you like to view the back end part?(enter yes/no)");
                String choice2 = scanner.nextLine();
                if(choice2.toLowerCase().equals("no")) {
                    DBTablePrinter.printTable(conn, PAGE_TABLE);
                    break;
                }
                else if(choice2.toLowerCase().equals("yes")) {
                    ResultSet result=statement.executeQuery("Select * From " + PAGE_TABLE + " inner join " + PAGE_BACK_END_TABLE
                            + " on " + PAGE_TABLE + ".Backend_ID =" + PAGE_BACK_END_TABLE + ".ID");
                    DBTablePrinter.printResultSet(result);
                    break;
                }

                else {
                    System.out.println("Invalid Choice. Try Again."); 
                    continue;
                }
            }while(true);
            System.out.println("Enter the ID of the link you want to open. Enter 0 to exit.");
            int choice3= scanner.nextInt();
            if(choice3>0) {
            ResultSet resultSet=statement.executeQuery("SELECT * From "  + PAGE_TABLE + " WHERE ID=" + choice3);
                resultSet.next();
                String url=resultSet.getString("Link");
                openURL(url);
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean close() {
        try {
            if(conn!=null) {
                statement=conn.createStatement();
                statement.execute("DROP TABLE " + VIDEO_TABLE_NAME);
                statement.execute("DROP TABLE " + ARTICLE_TABLE_NAME);
                statement.execute("DROP TABLE " + BACK_END_TABLE_NAME);
                statement.close();
                conn.close();
                return true;
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
