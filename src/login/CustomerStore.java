package login;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Where the customer accounts are stored (one line per customer: username,email,password). */
final class CustomerStore {

    static final Path FILE = Paths.get("data", "customers.txt");

    private CustomerStore() {
    }
}
