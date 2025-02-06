import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Utils {

    /**
     * Loads words based on the provided language code (e.g. "PL" or "EN").
     * @param language the language code
     * @return a list of words in lowercase
     */
    public static List<String> loadLanguage(String language) {
        List<String> words = new ArrayList<>();
        try {
            String path = "languages/" + language + ".txt";
            Scanner sc = new Scanner(new File(path));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                words.add(line.toLowerCase(Locale.ROOT));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("File " + language + ".txt not found");
        }
        return words;
    }

    /**
     * Checks whether a character is a letter (including Polish diacritics).
     * @param c the character to check
     * @return true if the character is a letter, false otherwise
     */
    public static boolean isLetter(char c) {
        return (
                (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
                        c == 'ą' || c == 'ć' || c == 'ę' || c == 'ł' ||
                        c == 'ń' || c == 'ó' || c == 'ś' || c == 'ź' || c == 'ż'
        );
    }

    /**
     * Returns a list of words where each 'green' letter in the regex
     * is present in that exact position in the word.
     * Underscores ('_') in the regex represent positions without restrictions.
     *
     * @param listToFind the list of candidate words
     * @param regex a 5-character array containing letters or underscores
     * @return the filtered list of matching words
     */
    public static List<String> findGreenLetters(List<String> listToFind, char[] regex) {
        List<String> result = new ArrayList<>();

        for (String word : listToFind) {
            int index = 0, matchedCount = 0, totalRequired = 0;

            for (char c : regex) {
                if (isLetter(c)) {
                    totalRequired++;
                    if (word.charAt(index) == c) {
                        matchedCount++;
                    }
                }
                index++;
            }
            if (matchedCount == totalRequired) {
                result.add(word);
            }
        }
        return result;
    }

    /**
     * Returns a list of words where letters in the given regex
     * (ignoring underscores) appear in the word but not in the same positions.
     * Underscores represent positions without restrictions.
     *
     * @param listToFind the list of candidate words
     * @param regex a 5-character String with letters or underscores
     * @return the filtered list of matching words
     */
    public static List<String> findYellow(List<String> listToFind, String regex) {
        List<String> tempList = new ArrayList<>();
        String lettersOnly = regex.replace("_", "");
        int requiredCount = lettersOnly.length();
        char[] regexArray = regex.toCharArray();

        for (String word : listToFind) {
            int found = 0, index = 0;
            for (char c : regexArray) {
                if (c != '_') {
                    // This letter must be in the word but not in the same position
                    if (word.contains(String.valueOf(c)) && word.charAt(index) != c) {
                        found++;
                    }
                }
                index++;
            }
            if (found == requiredCount) {
                tempList.add(word);
            }
        }
        return tempList.isEmpty() ? listToFind : tempList;
    }

    /**
     * Returns a list of words that satisfy the letter count constraints deduced from the guess.
     *
     * @param listToFind the list of candidate words
     * @param grayRegex a String containing letters guessed as gray
     * @param nonGray a String containing the letters that were marked as green or yellow (without underscores)
     * @return the filtered list of matching words
     */
    public static List<String> findGray(List<String> listToFind, String grayRegex, String nonGray) {
        List<String> tempList = new ArrayList<>();

        Map<Character, Integer> requiredCounts = new HashMap<>();
        for (char c : nonGray.toCharArray()) {
            requiredCounts.put(c, requiredCounts.getOrDefault(c, 0) + 1);
        }
        
        Set<Character> lettersToCheck = new HashSet<>();
        for (char c : (nonGray + grayRegex).toCharArray()) {
            lettersToCheck.add(c);
        }

        for (String word : listToFind) {
            boolean valid = true;
            for (char letter : lettersToCheck) {
                int countInWord = countOccurrences(word, letter);
                int required = requiredCounts.getOrDefault(letter, 0);
                if (countInWord != required) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                tempList.add(word);
            }
        }
        return tempList.isEmpty() ? listToFind : tempList;
    }

    /**
     * Helper method to count occurrences of a given letter in a word.
     */
    private static int countOccurrences(String word, char letter) {
        int count = 0;
        for (char c : word.toCharArray()) {
            if (c == letter) {
                count++;
            }
        }
        return count;
    }
}
