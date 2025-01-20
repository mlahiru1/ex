import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class RSA_Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Connected to server.");
			
			int m=0;
            while(m != 2){
				
				// Receive public key from the server
				BigInteger e = (BigInteger) in.readObject();
				BigInteger n = (BigInteger) in.readObject();
				System.out.println("Public key received: (e = " + e + ", n = " + n + ")");
				
				
				// Message to send
				Scanner obj2 = new Scanner(System.in);
				System.out.println("\n\nEnter String here : ");
				String message = obj2.nextLine();
				System.out.println("Original message: " + message);

				// Encrypt the message
				BigInteger plaintext = new BigInteger(message.getBytes());
				BigInteger encryptedMessage = plaintext.modPow(e, n);
				System.out.println("Encrypted message: " + plaintext);

				// Send encrypted message to the server
				out.writeObject(encryptedMessage);

				// Receive response from the server
				String response = (String) in.readObject();
				System.out.println("Response from server: " + response);
				
				Scanner obj = new Scanner(System.in);
				System.out.println("1. Encrypt again \n 2. Exit");
				int dec = obj.nextInt();
				m= dec;
			}
        }
    }
}