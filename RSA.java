import java.io.*;
import java.net.*;
import java.math.*;
import java.security.SecureRandom;
import java.util.Scanner;

public class RSA{
	private static BigInteger n,d,e;
	
	public static void main(String[] args) {
		
		
		int bitLength = 1024;
		
		SecureRandom random = new SecureRandom();
		
		BigInteger p = new BigInteger(bitLength/2,100,random);
		BigInteger q = new BigInteger(bitLength/2,100,random);
		
		n = p.multiply(q);
		
		BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		e = new BigInteger("65537");
		d = e.modInverse(phi);
		
		String message = "Hello";
		
		BigInteger plaintext = new BigInteger(message.getBytes());
		
		BigInteger encrypted = plaintext.modPow(e,n);
		
		System.out.println(encrypted);
		
		BigInteger decrypted = encrypted.modPow(d,n);
		
		String decryptmessage = new String(decrypted.toByteArray());
		
		System.out.println(decryptmessage);
	}
}