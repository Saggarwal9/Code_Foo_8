
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * Class used to accept user's MySQL Credentials.
 */
public class UI {
    private static String username="";
    private static String password = "";
    CryptoUtil c1=new CryptoUtil();
    private static String SECRET_KEY="TooMuchWater"; //I rate my program 7.8. (out of 100?)

    String FILENAME = "Credentials.txt"; //Encrypted username, password stored here.


    public boolean input(Database d1) {
        boolean flag=false;
        Scanner scanner= null;
        String ch;
        File file = new File(FILENAME);
        if(file.isFile()) { //Credentials.txt already exist --> Already logged in once.
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                username=c1.decrypt(SECRET_KEY, scanner.nextLine());
                password=c1.decrypt(SECRET_KEY, scanner.nextLine());
                flag=d1.open(username,password);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }else { //First time login.
            scanner= new Scanner(System.in);
            FileWriter fw = null;
            BufferedWriter bw = null;
            do {
                System.out.println("Enter MySQL Username");
                username=scanner.nextLine();
                System.out.println("Enter MySQL Password");
                password= scanner.nextLine();
                if(d1.open(username,password)==true) {
                    try {
                        d1.open(username,password);
                        try {
                            fw = new FileWriter(file, false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } 
                        bw = new BufferedWriter(fw);
                        bw.write(c1.encrypt(SECRET_KEY, username)); //Write encrypted username to file.
                        bw.newLine(); //Seperates username, password for making it easier to read in Java.
                        bw.write(c1.encrypt(SECRET_KEY, password)); //Write encrypted password to file.
                        flag=true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            bw.close(); 
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                else {
                    do {
                        System.out.println("Wrong input. Try again? Enter yes or no.");
                        try {
                            ch=scanner.nextLine();
                            if(ch.toLowerCase().equals("yes") || ch.toLowerCase().equals("no"))
                                break;
                            else {
                                System.out.println("Wrong Input. Try again. Enter no to exit");
                                continue;
                            }
                        }catch(InputMismatchException e) {
                            System.out.println("Wrong Input. Try again. Enter no to exit");
                            continue;
                        }


                    }while(true);

                }

            }while(!ch.toLowerCase().equals("no"));
            
        }
        return flag;
    }

}
