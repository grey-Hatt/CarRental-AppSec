package login;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

/** Checks whether a customer username is already registered. */
public class CustomerExists {

    static boolean isExists(String username) {
        try (BufferedReader reader = Files.newBufferedReader(CustomerStore.FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 3);
                if (data.length == 3 && data[0].equals(username)) {
                    return true;
                }
            }
        } catch (NoSuchFileException e) {
            // nobody has registered yet
        } catch (IOException e) {
            System.out.println("Error reading customer file: " + e.getMessage());
        }
        return false;
    }
}
