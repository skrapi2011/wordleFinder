import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/** Utils class for some functions
 *
 */
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
                words.add(line.toLowerCase(Locale.ROOT));
            }

            sc.close();
        }catch(FileNotFoundException e){
            System.err.println("File "+language+".txt not found");
        }
        return words;
    }

    /** Filters words based on green, yellow, and gray clues
     *
     * @param wordList List of words to filter
     * @param greens Map of positions to green letters
     * @param yellows Map of positions to yellow letters
     * @param grays Set of gray letters
     * @param minCounts Map of letters to minimum counts
     * @param maxCounts Map of letters to maximum counts
     * @return Filtered list of words
     */
    public static List<String> filterWords(
            List<String> wordList,
            Map<Integer, Character> greens,
            Map<Integer, Character> yellows,
            Set<Character> grays,
            Map<Character, Integer> minCounts,
            Map<Character, Integer> maxCounts
    ) {
        List<String> filteredWords = new ArrayList<>();

        for (String word : wordList) {
            if (isValidWord(word, greens, yellows, grays, minCounts, maxCounts)) {
                filteredWords.add(word);
            }
        }

        return filteredWords;
    }

    private static boolean isValidWord(
            String word,
            Map<Integer, Character> greens,
            Map<Integer, Character> yellows,
            Set<Character> grays,
            Map<Character, Integer> minCounts,
            Map<Character, Integer> maxCounts
    ) {
        Map<Character, Integer> wordCounts = new HashMap<>();
        char[] letters = word.toCharArray();

        // Check green letters and build wordCounts
        for (int i = 0; i < letters.length; i++) {
            char c = letters[i];
            wordCounts.put(c, wordCounts.getOrDefault(c, 0) + 1);

            if (greens.containsKey(i)) {
                if (greens.get(i) != c) {
                    return false;
                }
            }
        }

        // Check yellow letters
        for (Map.Entry<Integer, Character> entry : yellows.entrySet()) {
            int pos = entry.getKey();
            char c = entry.getValue();
            if (letters[pos] == c || !wordCounts.containsKey(c)) {
                return false;
            }
        }

        // Check gray letters
        for (char c : grays) {
            int countInWord = wordCounts.getOrDefault(c, 0);
            int maxCount = maxCounts.getOrDefault(c, 0);
            if (countInWord > maxCount) {
                return false;
            }
        }

        // Check minimum counts
        for (Map.Entry<Character, Integer> entry : minCounts.entrySet()) {
            char c = entry.getKey();
            int minCount = entry.getValue();
            if (wordCounts.getOrDefault(c, 0) < minCount) {
                return false;
            }
        }

        return true;
    }
}