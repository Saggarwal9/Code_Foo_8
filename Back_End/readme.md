# Prompt
a.  
Using this API, pull a list of content from the RSS feed. You must use the RSS feed endpoint, not the JSON feed.  

b.  
Build a service to pull and store the content from the API in a MySQL 5.7.X database. Explain how are you storing, normalizing, and indexing the data.  

c.  
Bonus: Build a service you think would make sense that uses your database. Defend your proposals.  

# Compilation
**Windows**  
The program uses no arguments. It uses 3 JAR Libraries: OKHTTP-3.10.0 , my-sql-connector-java-5.1.46, my-sql-connector-java-5.1.46-bin.  
To compile in an IDE, include the jars in the build path and execute Parse.java (main class).  

To compile on terminal: javac -cp .;my-sql-connector-java-5.1.46.jar;my-sql-connector-java-5.1.46-bin.jar;okhttp-3.10.0.jar \*.java 

To execute on terminal: java -cp .;my-sql-connector-java-5.1.46.jar;my-sql-connector-java-5.1.46-bin.jar;okhttp-3.10.0.jar Parse  

**Linux**  
I don't have a linux machine, but on searching online, we have to compile using : instead of ; in the above prompts.  

# Requirements (IMPORTANT)  
Requires a one-time log-in to your MySQL service. The program (Weakly) encrypts the password and stores it in a "Credentials.txt" file, which is later used for auto log-in if the progam is executed again.  

**Other than that, for the service to output correctly on command prompt, please disable "text wrapping" on your command prompt and increase width size to minimum 1000.**   

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

When the user exits, I decided to clean out the current database. This was a hard decision, but since I'm targetting non-technical people, I made the following assumptions:  

a)  
I assumed that these group of people wouldn't want to look at the old articles/videos that they've already explored earlier again. Instead of asking them to explicitly clear the database, I've made it an intrinsic feature.   

b)  
I assumed that these group of people wouldn't know their way about using MySQL. By dropping the table, I'm preventing the space usage which they may not be able to delete, if they are aware of it in the first place.  

*(My Operating System professor used to say, there are two type of software engineers: The smart one sells their bug in their softwares as features, others don't.)*

**POSSIBLE FUTURE ADDON** I wanted to add a bookmark list, that contains all the link that user might want to view later. (Just like Youtube's watch later feature). If I am done with all the other parts well before time, I'll try to implement this feature.

# Explanation

# Future Features, changes I would implement  
A) I would like to completely remove the MySQL Log-in dependancy in the start. I tried searching for methods online, but due to my limited knowledge and time, I couldn't implement this.  

B) If the log-in dependancy is impossible to remove, I would like to store the username, password as environment variables and not as a text file or make the encryption triple layer rather than current double layer.

C) I would like to add **Concurrency** to this program. After logging-in, I would concurrently create the Database and dynamically add them while the data is getting parsed. This is an example of classic **Consumer-Producer** problem, which can be implemented to make the program significantly efficient. In this case, the Parsing would be the producer. I'll have a queue for producer data, and the database will wait until that queue is full. When the queue is full, the producer will signal the consumer to start adding to the database. In this case, I would like to have multiple consumers, adding onto two seperate tables and then when the threads are done parsing, join the two temporary tables into the main front-end table.  

D) I admit that I didn't follow a systematic approach while building the service. The code inside **Database.java** is redundant at many places due to my indecisiveness of which feature to keep or not.  

E) Like I mentioned above, I would like to add a Bookmark system. 

F) The DBTablePrinter used works perfectly most of the times, however, in cases where the command prompt dimensions are small or the total table rows are less than 10, the table doesn't print as expected. 

