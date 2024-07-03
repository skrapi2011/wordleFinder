import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Utils {

    /** Loads given language
     *
     * @param language Language to load (ex. "EN")
     */
    public static void loadLanguage(String language){
        try{
            String path = "languages/"+language+".txt";
            Scanner sc = new Scanner(new File(path));
            String line;

            while(sc.hasNextLine()){
                line = sc.nextLine();
                Main.wordList.add(line);
            }

            sc.close();
        }catch(FileNotFoundException e){
            System.err.println("File "+language+".txt not found");
        }
    }




}