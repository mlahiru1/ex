import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;

public class RSA_Server {
    private static final int PORT = 12345;

    private static BigInteger n, d, e;

    public static void main(String[] args) throws Exception {
        // Generate RSA keys
        int bitLength = 1024;
        SecureRandom random = new SecureRandom();
        BigInteger p = new BigInteger(bitLength / 2, 100, random);
        BigInteger q = new BigInteger(bitLength / 2, 100, random);
        n = p.multiply(q); // n = p x q
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)); //(p-1)x(q-1)
        e = new BigInteger("65537"); // Common choice for e
        d = e.modInverse(phi);

        System.out.println("Server is running...");
        System.out.println("Public Key: (e = " + e + ", n = " + n + ")");
        System.out.println("Private Key: (d = " + d + ", n = " + n + ")");

        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket socket = serverSocket.accept();
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Client connected.");

			int m=0;
            while(m != 2){
				// Send public key to the client
				out.writeObject(e);
				out.writeObject(n);
				out.flush();

				// Receive encrypted message from the client
				BigInteger encryptedMessage = (BigInteger) in.readObject();
				System.out.println("Encrypted message received: " + encryptedMessage);

				// Decrypt the message
				BigInteger decryptedMessage = encryptedMessage.modPow(d, n);
				String message = new String(decryptedMessage.toByteArray());
				System.out.println("Decrypted message: " + message);

				// Respond to the client
				String response = "Message received: " + "Hello, what is your request?";
				out.writeObject(response);
			}
        }
    }
}