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


    public static List<String> findGreenLetters(List<String> listToFind, String regex) {
        regex = regex.toLowerCase(Locale.ROOT);
        List<String> tmp = new ArrayList<>();
        for (String listWord : listToFind) {
            if (checkGreenMatching(listWord, regex)) {
                tmp.add(listWord);
            }
        }
        return tmp;
    }

    public static boolean checkGreenMatching(String word, String pattern) {
        for (int i = 0; i < word.length(); i++) {
            char c1 = word.charAt(i);
            char c2 = pattern.charAt(i);
            if (c2 == '_') {
                continue;
            }
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }

    public static List<String> findYellow(List<String> listToFind, String regex) {
        List<String> tmpList = new ArrayList<>();
        regex = regex.toLowerCase(Locale.ROOT);
        CharSequence seq = regex.replace("_", "");
        int correctCount = seq.length();
        char[] regexArr = regex.toCharArray();

        for (String s : listToFind) {
            int correctFound = 0, i = 0;
            for (char c : regexArr) {
                seq = c + "";
                if (s.contains(seq) && s.charAt(i) != c) {
                    correctFound++;
                }
                i++;
            }

            if (correctCount == correctFound) {
                tmpList.add(s);
            }
        }

        return !tmpList.isEmpty() ? tmpList : listToFind;
    }

    public static List<String> findGray(List<String> listToFind, String regex) {
        List<String> tmpList = new ArrayList<>();
        CharSequence seq = regex.replace("_", "");
        int correctCount = seq.length();
        char[] regexHelp = regex.toCharArray();

        for (String s : listToFind) {
            int correct = 0;
            for (char c : regexHelp) {
                seq = c + "";
                if (!s.contains(seq)) {
                    correct++;
                }
            }

            if (correctCount == correct) {
                tmpList.add(s);
            }
        }

        return !tmpList.isEmpty() ? tmpList : listToFind;
    }

}