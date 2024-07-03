import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Utils {

    /** Loads given language
     *
     * @param language Language to load (ex. "EN")
     */
    public static List<String> loadLanguage(String language){
        List<String> words = new ArrayList<>();
        try{
            String path = "languages/"+language+".txt";
            Scanner sc = new Scanner(new File(path));
            String line;

            while(sc.hasNextLine()){
                line = sc.nextLine();
                words.add(line);
            }

            sc.close();
        }catch(FileNotFoundException e){
            System.err.println("File "+language+".txt not found");
        }
        return words;
    }



    // Input format check, must be length of 5, contain only letters or floor ( _ )
    public static boolean isValidFormat(String key){
        for(int i=0;i<key.length();i++){
            if(key.length() != 5){
                return false;
            }else if(
                    (key.charAt(i) >= 'a' && key.charAt(i) <= 'z')||
                            (key.charAt(i) >= 'A' && key.charAt(i) <= 'Z')||
                            (key.charAt(i)=='_')){
                return true;
            }
        }
        return false;
    }

    // Function returns strings that match regex chars at exact positions
    /*public static List<String> findGreenLetters(List<String> listToFind,char[] regex){

        List<String> resultTmp = new ArrayList<>();

        // For each word checks if regex syntax matches words,
        // if correctFound == correctCount then adding to result
        for(String s : listToFind){
            int i=0, correctFound = 0, correctCount = 0;

            for(char c : regex){
                if(isLetter(c)){
                    // char is a letter, so searching must match this to proceed
                    correctCount++;
                    if(s.charAt(i) == c) {
                        // char matches string char, correct
                        correctFound++;
                    }
                }
                i++;
            }
            // Checks if all letters matches string
            if (correctCount == correctFound){
                resultTmp.add(s);
            }
        }
        return resultTmp;
    }
    */

    // Function returns strings that match regex chars at exact positions
        public static List<String> findGreenLetters(List<String>listToFind, String regex){
        List<String> tmp = new ArrayList<>();
        for(String listWord : listToFind) {
            if(checkGreenMatching(listWord,regex))
                tmp.add(listWord);
        }
        return tmp;
    }

    // Checks if String matches green pattern
    public static boolean checkGreenMatching(String word, String pattern) {
        // porównanie liter słowa i wzorca pozycja po pozycji
        for (int i = 0; i < word.length(); i++) {
            char c1 = word.charAt(i);
            char c2 = pattern.charAt(i);
            // pominięcie znaku podłogi
            if (c2 == '_') {
                continue;
            }
            // jeśli litery są różne, zwróć false
            if (c1 != c2) {
                return false;
            }
        }
        // wszystkie litery są takie same, zwróć true
        return true;
    }

    // Function returns strings that match regex chars at any positions
    public static List<String> findYellow(List<String> listToFind,String regex){
        List<String> tmpList = new ArrayList<>();
        regex = regex.toLowerCase(Locale.ROOT);
        CharSequence seq = regex.replace("_","");
        int correctCount = seq.length();
        char[] regexArr = regex.toCharArray();

        for(String s : listToFind){
            int correctFound = 0, i=0;
            for(char c : regexArr){
                seq = c+"";
                // Checks if string contains searched char, but it must be at a different index
                if(s.contains(seq) && s.charAt(i) != c){
                    // Found one
                    correctFound++;
                }
                i++;
            }

            if(correctCount == correctFound){
                tmpList.add(s);
            }
        }

        if(tmpList.size() != 0) {
            return tmpList;
        }else{
            return listToFind;
        }
    }

    // Function returns strings that not contains any of regex letters
    public static List<String> findGray(List<String> listToFind, String regex){
        List<String> tmpList = new ArrayList<>();
        CharSequence seq = regex.replace("_","");
        int correctCount = seq.length();
        char[] regexHelp = regex.toCharArray();

        for(String s : listToFind){
            int correct = 0;
            for(char c : regexHelp){
                seq = c+"";
                if(!s.contains(seq)){
                    correct++;
                }
            }

            if(correctCount == correct){
                tmpList.add(s);
            }
        }
        if(tmpList.size() != 0) {
            return tmpList;
        }else{
            return listToFind;
        }
    }

}