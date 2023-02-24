import java.math.*;
import java.util.Scanner;
import java.util.HashMap;

public class Encrypt {
    private static final BigInteger E = new BigInteger("3"); // Public exponent
    private BigInteger p = new BigInteger("3"); // First prime factor of n
    private BigInteger q = new BigInteger("11"); // Second prime factor of n
    private BigInteger n; // Modulus
    private BigInteger d; // Private exponent

    public Encrypt(BigInteger p, BigInteger q) {
        this.p = p;
        System.out.println("p is " + this.p);
        this.q = q;
        System.out.println("q is " + this.q);
        this.n = p.multiply(q);
        System.out.println("n is " + this.n);
        this.d = E.modInverse((p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)));
        System.out.println("d is " + this.d);
    }

    public String encrypt(String message) {
        String numbers = convertToNumbers(message);
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < numbers.length(); i += 2) {
            String pair = numbers.substring(i, i + 2);
            BigInteger m = new BigInteger(pair);
            BigInteger c = m.modPow(E, n);
            System.out.println("this is c, " + c + " and m: " + m);
            ciphertext.append(String.format("%02d", c)); // Add leading zeros
            ciphertext.append(" ");
        }
        return ciphertext.toString();
    }

    private String decrypt(String ciphertext) {
        HashMap<String, String> alphabetMap = new HashMap<>(); // hasmap to store values 01-26 and value 32 for space.
        for (int i = 1; i <= 26; i++) {
            String key = String.format("%02d", i);
            String value = Character.toString((char) (i + 64));
            alphabetMap.put(key, value);
        }
        alphabetMap.put("32", " ");
        StringBuilder output = new StringBuilder();
        String[] pairs = ciphertext.split(" ");
        for (String pair : pairs) {
            if (pair.isEmpty()) {
                continue;
            }
            BigInteger c = new BigInteger(pair);
            BigInteger m = c.modPow(d, n);
            System.out.println(m);
            // int number = m.intValue();
            // System.out.println(number);
            // Convert back to number
            String key = String.format("%02d", m);
            String character = alphabetMap.get(key);
            // System.out.println(number + " number");
            // System.out.println(cOut + " Character");
            output.append(character);
        }
        return output.toString(); // convert the numbers to the original message

    }

    private String convertToNumbers(String input) {
        StringBuilder numbers = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int number;
            if (c == ' ') {
                number = 32;
            } else {
                number = (int) c - 64; // 'A' is represented by 01, B by 02, etc.
            }
            numbers.append(String.format("%02d", number)); // Add leading zeros
        }
        return numbers.toString();
    }

    /*
     * 
     * This is probably unnescary code. Can just go straight through when using the
     * decryption.
     * 
     * private String convertToString(String numbers) {
     * String output = "";
     * numbers = numbers.replaceAll("[^\\d ]", ""); // remove all non-digit and
     * non-space characters
     * for (int i = 0; i < numbers.length(); i += 2) {
     * String number = numbers.substring(i, i + 2);
     * if (number.equals("32")) {
     * int code = 32 - 64; // Convert 32 back to space
     * output += (char) code;
     * } else if (number.startsWith("0")) {
     * int code = Integer.parseInt(String.valueOf(number.charAt(1))) + 64;
     * output += (char) code;
     * } else {
     * int code = Integer.parseInt(number) + 64;
     * output += (char) code;
     * }
     * }
     * return output;
     * }
     */
    public static void main(String[] args) {
        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            // Get user input for whether to encrypt or decrypt
            System.out.print("Enter 'e' to encrypt or 'd' to decrypt, press 'q' to quit: ");
            input = scanner.nextLine();

            if (input.equals("e")) {
                // Example message to encrypt
                System.out.print("Enter message to encrypt: ");
                String message = scanner.nextLine();

                // Create an Encrypt object with two prime numbers p and q
                Encrypt encryptor = new Encrypt(new BigInteger("3"), new BigInteger("11"));

                // Encrypt the message using RSA encryption
                String ciphertext = encryptor.encrypt(message);
                System.out.println("Encrypted message: " + ciphertext);
            } else if (input.equals("d")) {
                // Example ciphertext to decrypt
                System.out.println("Enter ciphertext to decrypt: ");
                String ciphertext = scanner.nextLine();

                // Create an Encrypt object with two prime numbers p and q
                Encrypt encryptor = new Encrypt(new BigInteger("3"), new BigInteger("11"));

                // Decrypt the ciphertext
                String plaintext = encryptor.decrypt(ciphertext);
                System.out.println("Decrypted message: " + plaintext);
            } else if (!input.equals("q")) {
                System.out.println("Invalid input. Please enter 'e' or 'd'.");
            }
        } while (!input.equals("q"));

        // Close the scanner
        scanner.close();
    }

}