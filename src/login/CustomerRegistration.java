package login;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/** Saves a new customer account to the customer file. */
public class CustomerRegistration {

    public static boolean register(String username, String email, String password) {
        if (CustomerExists.isExists(username)) {
            return false;
        }

        try {
            Files.createDirectories(CustomerStore.FILE.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(CustomerStore.FILE, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                bw.write(username + "," + email + "," + password);
                bw.newLine();
            }
            System.out.println("Customer Registered Successfully");
            System.out.println("Saved to file.");
            return true;

        } catch (IOException e) {
            System.out.println("Error writing to file.");
            return false;
        }
    }
}
