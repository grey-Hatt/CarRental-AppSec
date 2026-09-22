package login;

/** Admin account check. */
public class AdminLogin {

    public static boolean admin(String username, String password) {

        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("Admin Login Success");
            return true;
        } else {
            System.out.println("Admin Login Failed");
            return false;
        }

    }
}
