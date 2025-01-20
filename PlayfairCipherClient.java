import java.io.*;
import java.net.*;
import java.util.Scanner;

public class PlayfairCipherClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Input the operation type (encrypt or decrypt)
            System.out.println("Enter operation (encrypt/decrypt):");
            String operation = scanner.nextLine();

            // Input the keyword
            System.out.println("Enter the keyword:");
            String keyword = scanner.nextLine();

            // Input the plaintext or ciphertext
            System.out.println("Enter the text:");
            String text = scanner.nextLine();

            // Send operation, keyword, and text to the server
            out.println(operation);
            out.println(keyword);
            out.println(text);

            // Receive the result (ciphertext or decrypted text) from the server
            String result = in.readLine();
            System.out.println("Result: " + result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
