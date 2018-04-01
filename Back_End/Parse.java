import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

public class Parse {
    /*
     * Sample Request format.
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
            doc=dBuilder.parse(getRequest(sampleRequest));
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
            dataSet.add(data);
        }

        return dataSet;
    }


    public static void main(String[] args) {

        System.out.println("Welcome to IGN Articles and Video Databases");
        System.out.println("MySQL 5.6 Log-In required.");
        Scanner scanner=new Scanner(System.in);
        ArrayList<Data> dataSet=null;
        String choice;
        String choice2;
        int pg;
        Database d1=new Database();
        UI ui=new UI();
        if(ui.input(d1)) {
            System.out.println("Log in successful");
            do {
                System.out.println("What would you like to view? (enter article or video or exit).");
                choice=scanner.nextLine();
                if(!choice.equals("article") && !choice.equals("video") && !choice.equals("exit")) {
                    System.out.println("Invalid entry");
                    continue;
                }
                if(choice.toLowerCase().equals("exit"))
                    break;
                System.out.println("Select a page number.");
                pg=scanner.nextInt();
                scanner.nextLine(); //Scanner's weird behavior.
                dataSet = retrieveData(sampleRequest+Integer.toString(pg));
                for(Data data:dataSet) {
                    d1.insertData(data);
                }
                System.out.println("Would you like to view the " + choice + " in the entire database(E) or the particular page(P) or go back(B).");
                choice2=scanner.nextLine();
                do {
                    if(choice2.toLowerCase().equals("e")) {
                        d1.printData(choice);
                        break;
                    }
                    else if(choice2.toLowerCase().equals("p")){
                        d1.printPage(choice,dataSet);
                        break;
                    }
                    else if(choice2.toLowerCase().equals("b")) {
                        System.out.println("Going back");
                        break;
                    }
                    else {
                        System.out.println("Invalid Choice. Try again");
                        continue;
                    }
                }while(true);
                dataSet.clear();

            }while(!choice.toLowerCase().equals("exit"));
        }


        d1.close();
        scanner.close();


    }

}
