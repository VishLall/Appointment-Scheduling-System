package helper;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**This class is for user Logger feature */
public class Logger {
    /**This method creates user logger.*/
    public static void userLogger(String username, boolean validUser) throws IOException {
        String filename = "login_activity.txt";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy kk:mm:ss");
        String logTime = LocalDateTime.now().format(dtf);
        FileWriter fw = new FileWriter(filename, true);
        PrintWriter file = new PrintWriter(fw);
        if (validUser){
            file.println("User: "+username+" successfully logged in at "+logTime);
        }else{
            file.println("User: "+username+" failed to login at "+logTime);
        }
        file.close();
    }
}
