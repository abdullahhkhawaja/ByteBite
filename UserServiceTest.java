import domain.User;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import services.UserService;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserServiceTest {

    private final UserService controller = new UserService();

    // ================== SIGNUP TESTS ==================

    @Test
    public void testSignUpEmptyFields() throws SQLException {
        boolean result = controller.SignUp("", "", "");
        assertFalse(result);
    }

    @Test
    public void testSignUpEmailAlreadyExists() throws SQLException {
        String existingEmail = "test@test.com";
        boolean result = controller.CheckIfEmailAlreadyUsed(existingEmail);
        assertTrue(result);
    }

    @Test
    public void testSignUpSuccess() throws SQLException {
        String email = "newuser" + System.currentTimeMillis() + "@example.com";
        boolean result = controller.SignUp("New User", email, "password123");
        assertTrue(result);
    }

    // ================== LOGIN TESTS ==================

    @Test
    public void testLoginEmptyFields() throws SQLException {
        User result = controller.validateLogin("", "");
        assertNull(result);
    }

    @Test
    public void testLoginInvalidCredentials() throws SQLException {
        User result = controller.validateLogin("nonexistent@example.com", "wrongpass");
        assertNull(result);
    }

    @Test
    public void testLoginSuccess() throws SQLException {

        String userName = "successtestuser";
        String email = "successtest" +  System.currentTimeMillis() + "@user.com";
        String password = "0000";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        controller.SignUp(userName, email, hashedPassword);

        User result = controller.validateLogin(email, password);
        assertNotNull(result);
    }
}
