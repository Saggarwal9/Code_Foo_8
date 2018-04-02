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
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The main class dealing with all MySQL Database Procedures. Adds and Print
 * data into three separate tables mainly Article table, Category Table and a
 * Back-end table.
 * 
 * @author Shubham Aggarwal
 *
 */
public class Database {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_NAME = "IGN_BACKEND";
    // Suppresses the SSL Warning.
    private static final String SUPPRESS_SSL = "?verifyServerCertificate=false&UseSSL=true&createDatabaseIfNotExist=true";
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME
            + SUPPRESS_SSL; // Connection string to the MySQL service.
    private static Connection conn;
    private Statement statement;
    private int MAX_ROWS = 0; // Change this to increase the number of rows getting printed.
    private static int BACKEND_ID = 1; // Extra column for joining Front end data base with the
                                       // backend.
    private static String BACK_END_TABLE_NAME = "ign_backend"; // Table for data under elements like
                                                               // ign_slur, ign_tags etc.
    private static String PAGE_TABLE = "ign_page"; // Temporary table to print front end data in a
                                                   // particular page.
    private static String PAGE_BACK_END_TABLE = "ign_page_backend"; // Temporary table to print back
                                                                    // end data in a particular
                                                                    // page.
    /*
     * Front end table.
     */
    private static int VIDEO_ID = 1; // Extra column making it easier for user to open a video link.
    private static int ARTICLE_ID = 1; // Extra column making it easier for user to open an article
                                       // link.
    private static String ARTICLE_TABLE_NAME = "ign_articles"; // Table for Articles
    private static String VIDEO_TABLE_NAME = "ign_videos"; // Table for videos

    /**
     * Add on service where user can open a desired article's/video's link.
     * 
     * @param url
     *            contains the link of the article/video.
     */
    public static void openURL(String url) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URL(url).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens the connection between JDBC and MySQL
     * 
     * @param username is the username for MySQL login
     * 
     * @param password is the password for MySql login
     */
    public boolean open(String username, String password) {
        try {
            try {
                Class.forName(JDBC_DRIVER).newInstance(); // Gets MySQL Driver
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }

        try {
            // Tries connecting with the MySQL service with the credentials provided.
            conn = DriverManager.getConnection(CONNECTION_STRING, username, password);
            statement = conn.createStatement(); // Use to execute all Sql Queries.

            /*
             * This is a step which I thought twice before adding. Since I'm directing this
             * application to the non-technologically profound audience, I felt it would be
             * a better step to clean up all previous data for them and provide them with
             * new data every time a connection is opened. If the user uses this application
             * a lot, then the database containing irrelevant data for the user might
             * consume high amount of space.
             */
            statement.execute("DROP TABLE IF EXISTS " + VIDEO_TABLE_NAME);
            statement.execute("DROP TABLE IF EXISTS " + ARTICLE_TABLE_NAME);
            statement.execute("DROP TABLE IF EXISTS " + BACK_END_TABLE_NAME);
            createTables();
        } catch (SQLException e) {
            return false;
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public void createTables() {

        try {
            /*
             * I've normalized the data in this way. First, I've two separate 'FRONT-END'
             * tables, i.e table containing only ID(For link opening purpose), GUID(Primary
             * Key - Prevents duplicate data) , Title, Description, Date, Link,
             * Backend_id(Join purposes). I've one 'BACK-END' table, i.e table containing
             * ID(Join Purposes), Slug, Network, State, Tags, Thumbnails (Compact, Medium,
             * Large). One front-end table contains details of Articles and other contains
             * details of videos. I call them front-end because it's the 'relevant' data for
             * an everyday user. All the other data, may not be of much use. However, I've
             * provided a choice for the user to access this backend table too if need be.
             */
            statement.execute("CREATE TABLE IF NOT EXISTS " + ARTICLE_TABLE_NAME
                    + " (ID int, GUID varchar(50) NOT NULL PRIMARY KEY, Title text, Description text, "
                    + "Date text, Link text, id_backend varchar(3))");
            statement.execute("CREATE TABLE IF NOT EXISTS " + VIDEO_TABLE_NAME
                    + " (ID int, GUID varchar(50) NOT NULL PRIMARY KEY, Title text, Description text, "
                    + "Date text, Link text, id_backend  varchar(3))");
            statement.execute("CREATE TABLE IF NOT EXISTS " + BACK_END_TABLE_NAME
                    + " (ID int PRIMARY KEY, Slug text, Network text, State text, Tags text, "
                    + "Thumbnail_compact text, Thumbnail_Medium text, Thumbnail_Large text)");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Gets the RSS feed's data from the data object and puts it in the relevant
     * table. Duplicate entries are ignored.
     * 
     * @param data
     *            encapsulates the data under <item> tag in the RSS feed.
     */
    public void insertData(Data data) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            // Normalized into three tables.
            if (data.getCategory().equals("article")) { // add to article table
                statement.execute("INSERT INTO " + ARTICLE_TABLE_NAME + " Values(" + ARTICLE_ID++
                        + ", '" + data.getGuid() + "', '" + data.getTitle()
                        + "', '" + data.getDescription() + "', '" + data.getDate() + "', '" + data
                                .getLink() + "', '" + BACKEND_ID + "')");
            } else if (data.getCategory().equals("video")) { // add to video table
                statement.execute("INSERT INTO " + VIDEO_TABLE_NAME + " Values(" + VIDEO_ID++
                        + ", '" + data.getGuid() + "', '" + data.getTitle()
                        + "', '" + data.getDescription() + "', '" + data.getDate() + "', '" + data
                                .getLink() + "', '" + BACKEND_ID + "')");

            }
            // add to backend table.
            statement.execute("INSERT INTO " + BACK_END_TABLE_NAME + " VALUES(" + BACKEND_ID++
                    + ", '" + data.getIgn_slug() + "', '"
                    + data.getIgn_network() + "', '" + data.getIgn_state() + "', '" + data
                            .getIgn_tags() + "', '" + data.getIgn_thumbnail_compact() + "', '"
                    + data.getIgn_thumbnail_medium() + "', '" + data.getIgn_thumbnail_large()
                    + "')");

        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            // duplicate entries warning.
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Prints the relevant table as per the user's choice.
     * 
     * @param choice
     *            contains the choice of article or video.
     */
    public void printData(String choice) {
        MAX_ROWS += 10;
        String choice2 = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to view the back end data? (Enter yes or no)");
        do {
            try {
                choice2 = scanner.nextLine();
                if (choice2.toLowerCase().equals("yes") || choice2.toLowerCase().equals("no"))
                    // Front end - Back end join?
                    break;
                else {
                    System.out.println("Invalid choice. Please enter either yes or no.");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }
        } while (true);
        try {
            statement = conn.createStatement();
            switch (choice) {
            case "article":
                if (choice2.equals("no")) {
                    ResultSet results = statement.executeQuery("SELECT * FROM " + ARTICLE_TABLE_NAME
                            + " ORDER BY ID");
                    DBTablePrinter.printResultSet(results, MAX_ROWS);
                }
                // DBTablePrinter.printTable(conn, ARTICLE_TABLE_NAME, MAX_ROWS); //print only
                // article front-end
                else { // print the complete article table. (Front end + back end)
                    ResultSet results = statement.executeQuery("SELECT " + ARTICLE_TABLE_NAME
                            + ".ID, GUID, Title, Description, Date, "
                            + "Link, Slug,Network, State, Tags,Thumbnail_compact, Thumbnail_medium, Thumbnail_large "
                            + "FROM " + ARTICLE_TABLE_NAME
                            + " INNER JOIN " + BACK_END_TABLE_NAME + " on " + ARTICLE_TABLE_NAME
                            + ".id_backend = "
                            + BACK_END_TABLE_NAME + ".ID ORDER BY " + ARTICLE_TABLE_NAME + ".ID");
                    DBTablePrinter.printResultSet(results, MAX_ROWS);

                }
                do {
                    System.out.println(
                            "Enter the ID of the link you want to open. "
                                    + "Enter 0 to go back to Main Menu.");
                    try { // User can input the ID to open a link.
                        int choice3 = scanner.nextInt();
                        if (choice3 > 0) {
                            ResultSet resultSet = statement.executeQuery("SELECT * From "
                                    + ARTICLE_TABLE_NAME + " WHERE ID=" + choice3);
                            resultSet.next();
                            String url = resultSet.getString("Link");
                            openURL(url);
                        } else
                            break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid Input");
                        scanner.nextLine();
                        continue;
                    } catch (SQLException e) {
                        System.out.println("Out of Range. Try again.");
                        continue;
                    }
                } while (true);
                break;
            case "video":
                if (choice2.toLowerCase().equals("no")) { // prints only front end video table.
                    ResultSet results = statement.executeQuery("SELECT * FROM " + VIDEO_TABLE_NAME
                            + " ORDER BY ID");
                    DBTablePrinter.printResultSet(results, MAX_ROWS);
                } else { // print the complete video table. (Front end + back end)
                    ResultSet results = statement.executeQuery("SELECT " + VIDEO_TABLE_NAME
                            + ".ID, GUID, Title, Description, Date," +
                            "Link, Slug,Network, State, Tags,Thumbnail_compact, Thumbnail_medium, Thumbnail_large FROM "
                            + VIDEO_TABLE_NAME
                            + " INNER JOIN " + BACK_END_TABLE_NAME + " on " + VIDEO_TABLE_NAME
                            + ".id_backend = "
                            + BACK_END_TABLE_NAME + ".ID ORDER BY " + VIDEO_TABLE_NAME + ".ID");
                    DBTablePrinter.printResultSet(results, MAX_ROWS);

                }
                do {
                    System.out.println(
                            "Enter the ID of the link you want to open. "
                                    + "Enter 0 to go back to Main Menu.");
                    try {
                        int choice4 = scanner.nextInt();
                        if (choice4 > 0) {
                            ResultSet resultSet = statement.executeQuery("SELECT * From "
                                    + VIDEO_TABLE_NAME + " WHERE ID=" + choice4);
                            resultSet.next();
                            String url = resultSet.getString("Link");
                            openURL(url);
                        } else
                            break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid Input");
                        scanner.nextLine();
                        continue;
                    } catch (SQLException e) {
                        System.out.println("Out of Range. Try again.");
                        continue;
                    }
                } while (true);
                break;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * Parses a particular page from the RSS Feed and stores the data into a
     * temporary table.
     * 
     * @param choice
     *            is the choice for article/video.
     * @param dataSet
     *            is the data of that particular page.
     */
    public void printPage(String choice, ArrayList<Data> dataSet) {
        Scanner scanner = new Scanner(System.in);
        try {
            statement = conn.createStatement();
            int backend_id = 1; // Back-end id for join purposes.
            int page_id = 1; // Link opening purpose.
            /*
             * Precautionary steps.
             */
            statement.execute("DROP TABLE IF EXISTS " + PAGE_TABLE);
            statement.execute("DROP TABLE IF EXISTS " + PAGE_BACK_END_TABLE);
            /*
             * Creates the front-end and back-end table.
             */
            statement.execute("CREATE TABLE " + PAGE_TABLE +
                    " (ID int NOT NULL PRIMARY KEY,GUID varchar(50), Title text, "
                    + "Description text, Date text, Link text,Backend_ID int)");
            statement.execute("CREATE TABLE " + PAGE_BACK_END_TABLE +
                    " (ID int PRIMARY KEY, Slug text, Network text, State text, Tags text, "
                    + "Thumbnail_Compact text, Thumbnail_medium text, Thumbnail_large text)");

            for (Data data : dataSet) { // Data insertion into the relevant tables.
                if (data.getCategory().equals(choice)) {
                    statement.execute("INSERT INTO " + PAGE_TABLE
                            + "(ID,GUID,Title,Description,Date,Link,Backend_ID) " + " Values("
                            + page_id++ + ",' " + data.getGuid() + "', '" + data.getTitle() +
                            "', '" + data.getDescription() + "', '" + data.getDate() + "', '" + data
                                    .getLink() + "', " + backend_id + ")");
                    statement.execute("INSERT INTO " + PAGE_BACK_END_TABLE + " Values("
                            + backend_id++ + ", '" + data.getIgn_slug() + "', '"
                            + data.getIgn_network() + "', '" + data.getIgn_state() + "', '" + data
                                    .getIgn_tags() + "', '" +
                            data.getIgn_thumbnail_compact() + "', '" + data
                                    .getIgn_thumbnail_medium() + "', '" + data
                                            .getIgn_thumbnail_large() + "')");

                }
            }
            do {
                System.out.println("Would you like to view the back end part?(enter yes/no)");
                try {// Choice for back-end join.
                    String choice2 = scanner.nextLine();
                    if (choice2.toLowerCase().equals("no")) {
                        ResultSet results = statement.executeQuery("SELECT * FROM " + PAGE_TABLE);
                        DBTablePrinter.printResultSet(results);
                        break;
                    } else if (choice2.toLowerCase().equals("yes")) { // print after joining
                                                                      // front-end
                                                                      // and back-end.
                        ResultSet result = statement.executeQuery("Select " + PAGE_TABLE
                                + ".id, GUID, Title, Description, "
                                + "Date,Link,Slug,Network,State,Tags,Thumbnail_compact,"
                                + "Thumbnail_medium, Thumbnail_large From " + PAGE_TABLE
                                + " inner join " + PAGE_BACK_END_TABLE
                                + " on " + PAGE_TABLE + ".Backend_ID =" + PAGE_BACK_END_TABLE
                                + ".ID");
                        DBTablePrinter.printResultSet(result);
                        break;
                    }

                    else {
                        System.out.println("Invalid Choice. Try Again.");
                        continue;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Choice. Try Again.");
                    continue;
                }
            } while (true);
            do {
                System.out.println(
                        "Enter the ID of the link you want to open. Enter 0 to go back to main menu.");
                try {
                    int choice3 = scanner.nextInt(); // Open link of the user choice.
                    if (choice3 > 0) {
                        ResultSet resultSet = statement.executeQuery("SELECT * From " + PAGE_TABLE
                                + " WHERE ID=" + choice3);
                        resultSet.next();
                        String url = resultSet.getString("Link");
                        openURL(url);
                    } else
                        break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input");
                    scanner.nextLine();
                    continue;
                } catch (SQLException e) {
                    System.out.println("Out of Range. Try again.");
                    continue;
                }
            } while (true);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * (Finally) Closes the database connections.
     * 
     * @return true/false depending on connection closed successfully or not.
     */
    public boolean close() {
        try {
            if (conn != null) {
                statement = conn.createStatement();
                statement.close();
                conn.close();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
