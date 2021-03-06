# Prompt
a.  
Using this API, pull a list of content from the RSS feed. You must use the RSS feed endpoint, not the JSON feed.  

b.  
Build a service to pull and store the content from the API in a MySQL 5.7.X database. Explain how are you storing, normalizing, and indexing the data.  

c.  
Bonus: Build a service you think would make sense that uses your database. Defend your proposals.

-------------------------------------------------------------------------------------------------------------------------------------
**Note:** This is a product of four days of self-studying of database concepts and JDBC. I feel I still have a high room of improvement as I'm still in the process of understanding all these concepts. I would like to assert that with more time and some guidance, I can improve this program significantly. Due to my academic commitment, I'm unable to further improve this program right now.
-------------------------------------------------------------------------------------------------------------------------------------

# Compilation
**Windows**  
The program uses no arguments. It uses 3 JAR Libraries: OKHTTP-3.10.0 , my-sql-connector-java-5.1.46, my-sql-connector-java-5.1.46-bin.  
To compile in an IDE, include the jars in the build path and execute Parse.java (main class).  

To compile on terminal: javac -cp .;first.jar;second.jar;third.jar \*.java  
To execute: java -cp .;first.jar;second.jar;third.jar Parse  

(First: my-sql-connector-java-5.1.46-bin.jar , second:my-sql-connector-java-5.1.46.jar , third: okhttp-3.10.0.jar)  

**Linux**  
I don't have a linux machine, but on searching online, one can compile using : instead of ; in the above prompts.  
For instance, javac -cp .\:first.jar\:second.jar\:third.jar \*.java

# Requirements (IMPORTANT)  
Requires a one-time log-in to your MySQL service. The program (Weakly) encrypts the password and stores it in a "Credentials.txt" file, which is later used for auto log-in if the progam is executed again.  

**Other than that, for the service to output correctly on command prompt, please disable "text wrapping" on your command prompt and increase width size to minimum 5000.**   

On windows, it can be done by right clicking the command prompt window titlebar --> properties-->Layout-->Untick wrap text and increase width size.    

# Explanation of the service
I've built a service to cater to the non-technical part of IGN's audience, mainly the children and the early teenagers.  
I've built an easy to use interactive service based on user's written input.  
The user is first asked to log into a MySQL Service. (I wanted to avoid this step, but MySQL **requires log-in to their service** to do any querying.)  
After entering the correct user-id and password, the credentials are stored in a **Credentials.txt** file, which allows automatic log-in to MySQL next time the program is executed. The username and the password is **encrypted** before getting stored on Credentials.txt.  

The user is first asked to choose between videos or articles. After making his choice, the user gets prompted to enter a page number.  
This page number gets appended to the **RSS feed's sample request** and all the XML from that page gets parsed onto our main database.  
The user is then prompted to either view the entire database, or just view his choice (articles/videos) from the particular page he entered earlier.  
The user then gets to view his choice in a tabular structure.  
The user is then prompted to enter the **Row ID** of the article/video he wants to view, or enter 0 to go back to main menu. 

When the user exits, I decided to clean out the current database. This was a hard decision, but since I'm targetting non-technical audience, I made the following assumptions:  

a)  
I assumed that these group of people wouldn't want to look at the old articles/videos that they've already explored earlier again. Instead of asking them to explicitly clear the database, I've made it an intrinsic feature.   

b)  
I assumed that these group of people wouldn't know their way about using MySQL. By dropping the table, I'm preventing the space usage which they may not be able to delete, if they are aware that the table exists on the hard disc in the first place.  

*(My Operating System professor used to say, there are two type of software engineers: The smart one sells their bug in their softwares as features, others don't.)*

**POSSIBLE FUTURE ADDON** I wanted to add a bookmark list, that contains all the link that user might want to view later. (Just like Youtube's watch later feature). If I am done with all the other parts well before time, I'll try to implement this feature.

# Adding  
I parse the XML data and add them all into an ArrayList<Data> type, where data consist of the variables required to store all the type of data in the feed.  (Possible improvement here is to remove the intermediate ArrayList and directly add the parsed data to the table.)
  
I then divide the data into two two tables - mainly ign_videos and ign_articles depending on their categories. 

I have another table that holds the 'back-end data'. I've explained my design implementation in the **normalization section** of the documentation.

# Normalization 
I've decomposed the entire data into three structures, mainly :  
A) Front-End Articles  
B) Front-End Videos  
C) Back-End

**Both the front-end tables contains the following columns:**
ID, GUID(Primary Key), Title, Description, Date, Link, ID_Backend.  
The ID is an extra column added to make it easy for users to open the link.  
ID_backend is the foreign key to connect to the back-end table.

**The back-end table contains the following columns:**
ID(Primary key), Slug, Network, State, Tags, Thumbnail Compact, Thumbnail Medium, Thumbnail Large.  

I wanted to eliminate state and network columns, since most of it is redundant data (IGN || Published). However, I kept those rows *hoping for an **edge case** where IGN decides to trick us and actually posts something else other than 'IGN' and 'Published' under their columns. However this being IGN's RSS feed, will IGN ever do that? Find out in the next episode of IGN Ball z.*  

**Why did I split articles and categories into two seperate tables?**  
1) I felt it would be a good decision to eliminate the category column and rather present with the user of what explicitly they'll like to view. This was a personal choice as I prefer articles over videos and other people like me might prefer to have this option. *Sorry IGN Video makers, I love them articles.*  
2) Most of the video links leads to an IGN 404 page.

**Why do I have a separate back-end table?**  
As a general user based service, I felt that there would be no use displaying Slug, Tags etc. details to the user, as they're mostly concerned with the title, description and link. If, however the user is interested in the back-end details, the program will cater to his needs and provide him a front-end and back-end combined table by inner joining.

**Why do I have GUID when I have already have an ID?**  
Making GUID primary key was essential in preventing duplicate data. 

# Program structure
1) **Parse.java** contains the main method. This class is responsible for retrieving username/password,encrypting and storing it on the file. This class is also responsible for **GET** connection to the sample request of the RSS feed and **DOM** parsing it. The data is stored in an **ArraryList** of data object. Parse is also responsible for taking the user's preference for printing data.

2) **Data.java** contains all possible data variables that can be retrieved from parsing the RSS feed.  

3) **CryptoUtils.java** contains the **encrypt()** and the **decrypt()** method used to encrypt/decrypt username and password inputted by the user. The class uses a 2-layer encryption using **PBEWithMD5AndDES Encryption**. This is an optional add-on service. I felt it would go nicely with the theme. Possible improvement here would be to change from 2 layer encryption to 3 layer encryption.

4) **DBTablePrinter.java** contains the methods to print the database. The owner(Hami) has allowed free distribution of this code. For more information, check out the github page for DBTablePrinter: https://github.com/htorun/dbtableprinter.  

5) **UI.java** contains the methods that check whether the given username and password is correct. If it is correct, then it creates a file(if it doesn't exist) and using the encrypt function, stores the encrypted username and password to a textfile called **Credentials.txt"**.

6) **Database.java** contains all the database related functions like opening the database, creating tables, inserting data, calling DBTablePrinter methods for the given query, dropping tables and closing the database. It also has a printPage method responsible for printing page specific data (and not the entire table).  

# Future features, changes I would implement  
A) I would like to completely remove the MySQL Log-in dependancy in the start. I tried searching for methods online, but due to my limited knowledge and time, I couldn't implement this.  

B) If the log-in dependancy is impossible to remove, I would like to store the username, password as environment variables and not as a text file or make the encryption triple layer rather than current double layer.

C) I would like to add **Concurrency** to this program. After logging-in, I would concurrently create the Database and dynamically add them while the data is getting parsed. This is an example of classic **Consumer-Producer** problem, which can be implemented to make the program significantly more efficient. In this case, the Parsing would be the producer. I'll have a queue for producer data, and the database will wait until that queue is full. When the queue is full, the producer will signal the consumer to start adding to the database. In this case, I would like to have multiple consumers, adding onto two seperate tables and then when the threads are done parsing, join the two temporary tables into the main front-end table.  

D) I admit that I didn't follow a systematic approach while building the service. The code inside **Database.java** is redundant at many places due to my indecisiveness of which feature to keep or not.  

E) Adding the bookmark system.

F) The DBTablePrinter used works perfectly most of the times, however, there are cases when the table appears to be compressed. I was unable to fix this.

