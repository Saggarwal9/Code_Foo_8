
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class UI {
    private static String username="";
    private static String password = "";
    CryptoUtil c1=new CryptoUtil();
    private static String SECRET_KEY="IGN";

    String FILENAME = "Credentials.txt";


    public boolean input(Database d1) {
        boolean flag=false;
        Scanner scanner= null;
        String ch;
        File file = new File(FILENAME);
        if(file.isFile()) {
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                username=c1.decrypt(SECRET_KEY, scanner.nextLine());
                password=c1.decrypt(SECRET_KEY, scanner.nextLine());
                flag=d1.open(username,password);
                scanner.close();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }else {
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
                        bw.write(c1.encrypt(SECRET_KEY, username));
                        bw.newLine();
                        bw.write(c1.encrypt(SECRET_KEY, password));
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
                    System.out.println("Wrong input. Try again? Y or N.");
                    ch=scanner.nextLine();
                }

            }while(!ch.toLowerCase().equals("n"));
            scanner.close();

        }
        return flag;
    }

}
