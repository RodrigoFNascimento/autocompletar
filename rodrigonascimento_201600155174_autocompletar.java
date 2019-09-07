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

    public Trie(int alphabetSize) {

        this.letter = ' ';
        this.isWord = false;
        this.children = new Trie[alphabetSize];
    }

    public Trie(char letter, int alphabetSize) {

        this.letter = letter;
        this.isWord = false;
        this.children = new Trie[alphabetSize];
    }
}

public class rodrigonascimento_201600155174_autocompletar {

    public static int ALPHABET_SIZE = 26;

    public static Trie root = new Trie(ALPHABET_SIZE);

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
                currentTrie.children[index] = new Trie(currentChar, ALPHABET_SIZE);
       
            // Goes down the tree
            currentTrie = currentTrie.children[index]; 
        } 
       
        // Marks last node as word
        currentTrie.isWord = true; 
    } 

    /**
     * Searchs the trie for word suggestions.
     * 
     * @param root  Root of the trie to be searched.
     * @param word  Word to be searched.
     * @return      Words found separated by a comma.
     */
    public static StringBuilder search(Trie root, String word) {

        Trie currentTrie = root;
        char currentChar = ' ';
        int charIndex = 0;
        StringBuilder prefix = new StringBuilder();
        StringBuilder output = new StringBuilder();

        // Iterates through the word
        for (int i = 0; i < word.length(); i++) {

            currentChar = word.charAt(i);
            charIndex = currentChar - 'a';

            // Goes down the trie
            currentTrie = currentTrie.children[charIndex];

            // Gets the words from the trie
            if (currentTrie != null)
                output.append(getWords(currentTrie, new StringBuilder(), prefix, i + 1));
            else
                break;
        }

        // If no word is found on the trie, the output will be empty
        if (output.length() == 0)
            output.append('-');
        else
            output.deleteCharAt(output.length() - 1);   // Removes the traling comma

        return output;
    }

    /**
     * Gets every word in a trie, starting at the root until the desired depth.
     * 
     * @param root      Root of the trie.
     * @param output    Every word found separated by a comma.
     * @param prefix    Prefix of all words.
     * @param depth     Maximum depth of the trie that should be traversed.
     * @return          Every word found separated by a comma.
     */
    public static StringBuilder getWords(Trie root, StringBuilder output, StringBuilder prefix, int depth) {

        if (root == null || depth < 0)
            return new StringBuilder();

        prefix.append(root.letter);

        if (root.isWord && (depth == 0 || depth == 1))
            output.append(prefix).append(",");

        if (depth > 0) {

            // Iterates over the children
            for (int i = 0; i < root.children.length; i++) {

                if (root.children[i] != null) {
    
                    // Recurs down the tree
                    getWords(root.children[i], output, prefix, depth - 1);

                    // Deletes all the letters added through the recursion
                    // in order to clear the word and move on to the next sibling
                    if (prefix.length() > 0)
                        prefix.deleteCharAt(prefix.length() - 1);
                }
            }
        }

        return output;
    }
    
    /**
     * Writes content to file.
     * 
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

                foundWords = search(root, searchedWord);

                writeToFile(args[1], searchedWord + ":" + foundWords + "\n");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}