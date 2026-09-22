package login;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

/** Checks a customer's username and password against the customer file. */
public class CustomerLogin {

    public static boolean validateCustomer(String username, String password) {
        try (BufferedReader reader = Files.newBufferedReader(CustomerStore.FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 3);
                if (data.length == 3 && data[0].equals(username) && data[2].equals(password)) {
                    System.out.println("Login Success");
                    return true;
                }
            }
        } catch (NoSuchFileException e) {
            System.out.println("No customers registered yet.");
        } catch (IOException e) {
            System.out.println("Error reading customer data.");
        }
        System.out.println("Login Failed");
        return false;
    }
}
