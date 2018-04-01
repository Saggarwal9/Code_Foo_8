import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

/**
 * Program that XML parses the IGN API and stores in a MySQL database through JDBC.
 * XML Parsed through DOM.
 * Added a basic interactive service targeted at the Non-Technologically Profound IGN's Audience.
 * Explanation of this service in the Repository's Readme.md
 * @author Shubham Aggarwal
 *
 */
public class Parse {
    /*
     * Sample Request format. The User can append his page number choice.
     */
    private static String sampleRequest = "https://ign-apis.herokuapp.com/content/feed.rss?page=";

    private static HttpURLConnection connection=null; //Performs basic HTTP operations without needing any additional libraries.

    /**
     * Static variables for XML Parsing.
     * If the XML API tags names are changed later, 
     * the values here can be changed for the program to work with the new API.
     */
    private static String ITEM_GUID = "guid";
    private static String ITEM_CATEGORY = "category";
    private static String ITEM_TITLE = "title";
    private static String ITEM_DESCRIPTION = "description";
    private static String ITEM_DATE= "pubDate";
    private static String ITEM_LINK = "link";
    private static String ITEM_SLUG = "ign:slug";
    private static String ITEM_NETWORK = "ign:networks";
    private static String ITEM_STATE = "ign:state";
    private static String ITEM_TAGS = "ign:tags";
    private static String ITEM_THUMBNAIL = "ign:thumbnail";
    private static String ATTRIBUTE_LINK= "link";
    private static int INDEX_COMPACT = 0;
    private static int INDEX_MEDIUM = 1;
    private static int INDEX_LARGE = 2;
    private static int BASE_DATA = 0; //Single Valued Data.

    /**
     * Makes a 'GET' HTTP request with the link provided in the parameter.
     * @param link is the sample request to pull the RSS data.
     * @return the InputStream for the RSS data.
     */
    public static InputStream getRequest(String link) {
        URL url = null;
        //HttpURLConnection connection = null; 
        InputStream stream=null;

        try {
            url=new URL(link);
        }catch(MalformedURLException e) {
            System.out.println("Invalid Link.");
            e.printStackTrace();
        }

        try {
            connection=(HttpURLConnection) url.openConnection(); //openConnection returns Connection object. Cast to HttpUrlConnection required.
            connection.setRequestMethod("GET"); //Get type request.
            connection.setRequestProperty("Content-Type", "application/rss+xml; charset=utf-8"); //Request header
            connection.setConnectTimeout(30000); //30 Seconds to make a connection.
            connection.setReadTimeout(5000);  //Waits for 5 seconds after reading completion.
            stream=connection.getInputStream();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    /**
     * Retrieves the XML and parses it.
     * @param sampleRequest contains the request page from which the XML is inputed.
     * @return ArrayList of all the data from the sampleRequest page.
     */
    public static ArrayList<Data> retrieveData(String sampleRequest){
        ArrayList<Data> dataSet=new ArrayList<Data>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder=null; //DOM Tree builder.
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) { //Shouldn't go here.
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc=dBuilder.parse(getRequest(sampleRequest)); //Builds a sample document of the XML in the sampleRequest link.
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        doc.getDocumentElement().normalize();//The normalize() method removes empty Text nodes, and joins adjacent Text nodes.

        NodeList itemsList = doc.getDocumentElement().getElementsByTagName("item"); //NodeList of data under every <item> created.
        for(int i=0;i<itemsList.getLength();i++) {
            Data data=new Data();
            Element item= (Element)itemsList.item(i); //Retrieves the i-th item from the itemsList and casts to Element.
            /**
             * DATA ASSIGNMENT.
             */
            data.setGuid(item.getElementsByTagName(ITEM_GUID).item(BASE_DATA).getTextContent());
            data.setCategory(item.getElementsByTagName(ITEM_CATEGORY).item(BASE_DATA).getTextContent());
            data.setTitle(item.getElementsByTagName(ITEM_TITLE).item(BASE_DATA).getTextContent());
            data.setDescription(item.getElementsByTagName(ITEM_DESCRIPTION).item(BASE_DATA).getTextContent());
            data.setDate(item.getElementsByTagName(ITEM_DATE).item(BASE_DATA).getTextContent());
            data.setLink(item.getElementsByTagName(ITEM_LINK).item(BASE_DATA).getTextContent());
            data.setIgn_slug(item.getElementsByTagName(ITEM_SLUG).item(BASE_DATA).getTextContent());
            data.setIgn_network(item.getElementsByTagName(ITEM_NETWORK).item(BASE_DATA).getTextContent());
            data.setIgn_state(item.getElementsByTagName(ITEM_STATE).item(BASE_DATA).getTextContent());
            data.setIgn_tags(item.getElementsByTagName(ITEM_TAGS).item(BASE_DATA).getTextContent());
            data.setIgn_thumbnail_compact(((Element) item.getElementsByTagName(ITEM_THUMBNAIL).item(INDEX_COMPACT)).getAttribute(ATTRIBUTE_LINK));
            data.setIgn_thumbnail_medium(((Element) item.getElementsByTagName(ITEM_THUMBNAIL).item(INDEX_MEDIUM)).getAttribute(ATTRIBUTE_LINK));
            data.setIgn_thumbnail_large(((Element) item.getElementsByTagName(ITEM_THUMBNAIL).item(INDEX_LARGE)).getAttribute(ATTRIBUTE_LINK));
            dataSet.add(data); //That's a lot of data. Add it to our final list. 
        }

        return dataSet;
    }

    /*
     * Initiates the service. Requires the User to Log in using its MySQL Credentials. 
     * The credentials are encrypted and stored in a file. 
     */
    public static void main(String[] args) {
        System.out.println("Welcome to IGN Articles and Video Databases");
        System.out.println("MySQL 5.6 Log-In required.");
        Scanner scanner=new Scanner(System.in);
        ArrayList<Data> dataSet=null;
        String choice=""; //First choice of Article/Video.
        String choice2; //Second choice of Full Database or the particular page.
        int pg;
        Database d1=new Database();
        UI ui=new UI();
        if(ui.input(d1)) {
            System.out.println("Log in successful"); //Correct Credentials entered. 
            do {
                System.out.println("What would you like to view? (enter article or video or exit).");
                try {
                    choice=scanner.nextLine();
                }catch(InputMismatchException e) {
                    System.out.println("Invalid Choice. Try again");
                    continue;
                }
                if(!choice.equals("article") && !choice.equals("video") && !choice.equals("exit")) {
                    System.out.println("Invalid entry");
                    continue;
                }
                if(choice.toLowerCase().equals("exit"))
                    break;
                do {
                    System.out.println("Select a page number. ( 1 - 20 )");
                    try {
                        pg=scanner.nextInt();
                        if(pg>0 && pg <=20)
                            break;
                        else {
                            System.out.println("Invalid entry");
                            continue;
                        }
                    }catch(InputMismatchException e) {
                        System.out.println("Invalid entry");
                        scanner.nextLine();
                        continue;
                    }
                }while(true);
                scanner.nextLine(); //Scanner's weird behavior.
                dataSet = retrieveData(sampleRequest+Integer.toString(pg)); //Appended the page number.
                for(Data data:dataSet) {
                    d1.insertData(data); //Inserting the data in our overall database.
                }
                //Asks the user whether it would prefer to view the whole database or the page mentioned.
                do {
                    System.out.println("Would you like to view the " + choice + " in the entire database(E) or the particular page(P) or go back(B).");
                    try {
                        choice2=scanner.nextLine();
                        if(choice2.toLowerCase().equals("e")) { //Entire Article / Category Database
                            d1.printData(choice);
                            break;
                        }
                        else if(choice2.toLowerCase().equals("p")){ //The Article / Category at the particular page.
                            d1.printPage(choice,dataSet);
                            break;
                        }
                        else if(choice2.toLowerCase().equals("b")) { //Go back.
                            System.out.println("Going back");
                            break;
                        }
                        else {
                            System.out.println("Invalid Choice. Try again");
                            continue;
                        }
                    }catch(InputMismatchException e) { //If user 'accidentally' enters a wrong type of input.
                        System.out.println("Invalid Choice. Try again");
                        continue;
                    }

                }while(true);
                dataSet.clear();

            }while(!choice.toLowerCase().equals("exit"));
        }
        d1.close();
    }

}
