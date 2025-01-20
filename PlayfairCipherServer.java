import java.io.*;
import java.net.*;
import java.util.*;

public class PlayfairCipherServer {

    private static char[][] matrix = new char[5][5];

    // Generate the 5x5 matrix based on the keyword
    private static void generateMatrix(String keyword) {
        keyword = keyword.toUpperCase().replace("J", "I");
        boolean[] used = new boolean[26];
        int row = 0, col = 0;

        for (char ch : keyword.toCharArray()) {
            if (!used[ch - 'A']) {
                matrix[row][col] = ch;
                used[ch - 'A'] = true;
                col++;
                if (col == 5) {
                    col = 0;
                    row++;
                }
            }
        }

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (ch == 'J') continue;
            if (!used[ch - 'A']) {
                matrix[row][col] = ch;
                used[ch - 'A'] = true;
                col++;
                if (col == 5) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    // Print the 5x5 matrix
    private static void printMatrix() {
        System.out.println("Generated Playfair Cipher Matrix:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Find the position of a character in the matrix
    private static int[] findPosition(char ch) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == ch) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // Encrypt or decrypt text
    private static String processText(String text, boolean encrypt) {
        List<String> pairs = prepareText(text);
        StringBuilder result = new StringBuilder();

        for (String pair : pairs) {
            char first = pair.charAt(0);
            char second = pair.charAt(1);

            int[] firstPos = findPosition(first);
            int[] secondPos = findPosition(second);

            if (firstPos[0] == secondPos[0]) { // Same row
                result.append(matrix[firstPos[0]][(firstPos[1] + (encrypt ? 4 : 1)) % 5]);
                result.append(matrix[secondPos[0]][(secondPos[1] + (encrypt ? 4 : 1)) % 5]);
            } else if (firstPos[1] == secondPos[1]) { // Same column
                result.append(matrix[(firstPos[0] + (encrypt ? 4 : 1)) % 5][firstPos[1]]);
                result.append(matrix[(secondPos[0] + (encrypt ? 4 : 1)) % 5][secondPos[1]]);
            } else { // Rectangle
                result.append(matrix[firstPos[0]][secondPos[1]]);
                result.append(matrix[secondPos[0]][firstPos[1]]);
            }
        }

        return result.toString();
    }

    // Prepare the plaintext by splitting into pairs
    private static List<String> prepareText(String text) {
        text = text.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", "");
        StringBuilder sb = new StringBuilder(text);

        for (int i = 0; i < sb.length() - 1; i += 2) {
            if (sb.charAt(i) == sb.charAt(i + 1)) {
                sb.insert(i + 1, 'X');
            }
        }

        if (sb.length() % 2 != 0) {
            sb.append('Z');
        }

        List<String> pairs = new ArrayList<>();
        for (int i = 0; i < sb.length(); i += 2) {
            pairs.add(sb.substring(i, i + 2));
        }

        return pairs;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is running on port 8080...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    // Receive the operation type, keyword, and text from the client
                    String operation = in.readLine(); // Either "encrypt" or "decrypt"
                    String keyword = in.readLine();
                    String text = in.readLine();

                    // Generate and display the matrix
                    generateMatrix(keyword);
                    printMatrix();

                    // Process the text based on the operation
                    boolean encrypt = operation.equalsIgnoreCase("encrypt");
                    String result = processText(text, encrypt);

                    // Send the result (ciphertext or decrypted text) back to the client
                    out.println(result);
                }

                System.out.println("Client disconnected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
