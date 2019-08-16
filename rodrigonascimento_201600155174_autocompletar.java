import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class Trie {

    char letter;
    boolean isWord;
    Trie[] children;

    public Trie() {

        this.letter = ' ';
        this.isWord = false;
        this.children = new Trie[26];
    }

    public Trie(char letter) {

        this.letter = letter;
        this.isWord = false;
        this.children = new Trie[26];
    }
}

public class rodrigonascimento_201600155174_autocompletar {

    public static Trie root = new Trie();

    /**
     * Inserts a key into a Trie.
     * 
     * @param key   Key to be inserted.
     */
    static void insert(String key) 
    {
        char currentChar;
        Trie currentTrie = root;
        int length = key.length(); 
        int index;
        
        // Iterates through the letters in the word
        for (int indexInThekey = 0; indexInThekey < length; indexInThekey++) 
        {
            currentChar = key.charAt(indexInThekey);
            index = currentChar - 'a';
            if (currentTrie.children[index] == null) 
                currentTrie.children[index] = new Trie(currentChar);
       
            // Goes down the tree
            currentTrie = currentTrie.children[index]; 
        } 
       
        // Marks last node as word
        currentTrie.isWord = true; 
    } 

    /**
     * Searchs the Trie for compatible words.
     * 
     * @param word  Word to be searched.
     * @return      Every word found.
     */
    static StringBuilder search(String word) {

        int charIndex;
        Trie currentTrie = root;
        char currentChar;
        StringBuilder foundWords = new StringBuilder();
        StringBuilder prefix = new StringBuilder();
        int wordLength = word.length();

        for (int i = 0; i < wordLength; i++) {

            currentChar = word.charAt(i);
            charIndex = currentChar - 'a';

            if (currentTrie.children[charIndex] != null) {

                prefix.append(currentChar);

                if (currentTrie.children[charIndex].isWord)
                    foundWords.append(prefix + ",");

                currentTrie = currentTrie.children[charIndex];
            } else {
                break;
            }
        }

        if (prefix.length() > 0)
            foundWords.append(getAllWords(currentTrie, new StringBuilder(), prefix, prefix.length()));

        if (foundWords.length() == 0)
            foundWords.append('-');
        else
            foundWords.deleteCharAt(foundWords.length() - 1);   // There will be a ',' at the end

        return foundWords;
    }
    
    /**
     * Gets every word in the root's children.
     * 
     * @param root      Root of the Trie.
     * @param output    The method's output.
     * @param prefix    The Trie's prefix.
     * @param depth     Maximum depth of the tree that will be traversed.
     * @return          Every word found in the Trie.
     */
    public static StringBuilder getAllWords(Trie root, StringBuilder output, StringBuilder prefix, int depth) {

        if (root == null)
            return new StringBuilder();

        if (depth == 0)
            return output;

        for (int i = 0; i < root.children.length; i++) {

            if (root.children[i] != null) {

                prefix.append(root.children[i].letter);

                if (root.children[i].isWord)
                    output.append(prefix.toString() + ",");

                getAllWords(root.children[i], output, prefix, --depth);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

        return output;
    }

    /**
     * Writes content to file.
     * @param fileName  Name of the file (with extension) to be writen.
     * @param content   Content to be writen on the file.
     * @throws FileNotFoundException
     */
    private static void writeToFile(String fileName, String content) throws FileNotFoundException {

        try(FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.print(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        
        try (FileInputStream inputStream = new FileInputStream(args[0])) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Inserts keys into Trie
            int numberOfInsertions = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numberOfInsertions; i++) {
                insert(reader.readLine());
            }

            // Retrieves words from Trie
            String searchedWord;
            StringBuilder foundWords = new StringBuilder();
            int numberOfSearches = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numberOfSearches; i++) {

                foundWords = new StringBuilder();

                searchedWord = reader.readLine();

                foundWords = search(searchedWord);

                writeToFile(args[1], searchedWord + ":" + foundWords + "\n");
                
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}