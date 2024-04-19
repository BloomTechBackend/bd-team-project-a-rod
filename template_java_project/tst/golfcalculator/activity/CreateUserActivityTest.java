package golfcalculator.activity;

import golfcalculator.converters.ModelConverter;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.EmailAlreadyExistsException;
import golfcalculator.exceptions.InvalidEmailException;
import golfcalculator.exceptions.InvalidUserNameException;
import golfcalculator.exceptions.UserIdAlreadyExistsException;
import golfcalculator.models.requests.CreateUserRequest;
import golfcalculator.models.results.CreateUserResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CreateUserActivityTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private CreateUserActivity createUserActivity;

    private CreateUserRequest validRequest;
    private String validId = "validId";
    private String invalidId = "@arod99";
    private String validEmail = "valid@email.com";
    private String invalidEmail = "joe@.com";
    private User validUser;
    private String illegalStateException = "IllegalStateException";
    private String illegalStateExceptionMessage = "Please fill in required fields.";
    private String invalidUserNameException = "InvalidUserNameException";
    private String invalidUserNameExceptionMessage = "Invalid username: Please ensure your username is" +
            "between 3 and 20 characters long and contains only letters and numbers.";
    private String invalidEmailException = "InvalidEmailException";
    private String invalidEmailExceptionMessage = "Invalid email: Please enter a valid email address with a" +
            "format like example@domain.com. Ensure it includes a domain name and a top-level" +
            "domain (like .com, .org, etc).";
    private String userIdAlreadyExistsException = "UserIdAlreadyExistsException";
    private String userIdAlreadyExistsExceptionMessage = "User Id already in use!";
    private String emailAlreadyExistsException = "EmailAlreadyExistsException";
    private String emailAlreadyExistsExceptionMessage = "Email already in use!";

    @BeforeEach
    private void setUp() {
        initMocks(this);

        validRequest = CreateUserRequest.builder()
                .withUserId(validId)
                .withEmail(validEmail)
                .build();

        validUser = new User();
        validUser.setUserId(validId);
        validUser.setEmail(validEmail);
        validUser.setGamesPlayed(0);
    }

    @Test
    void handleRequest_validRequest_returnsExpectedResult() {

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userDao.isUnusedUserId(validId)).thenReturn(true);
        when(userDao.isUnusedEmail(validEmail)).thenReturn(true);

        CreateUserResult result = createUserActivity.handleRequest(validRequest, null);

        assertEquals(validId, result.getUserModel().getUserId());
        assertEquals(validEmail, result.getUserModel().getEmail());
        verify(userDao).saveUser(userCaptor.capture());
    }

    @Test
    void handleRequest_invalidUserId_throwsInvalidUserNameException() {

        CreateUserRequest invalidRequest = validRequest;
        invalidRequest.setUserId(invalidId);

        CreateUserResult result = createUserActivity.handleRequest(validRequest, null);

        assertEquals(invalidUserNameException, result.getError());
        assertEquals(invalidUserNameExceptionMessage, result.getErrorMessage());
    }

    @Test
    void handleRequest_invalidEmail_throwsInvalidEmailException() {

        CreateUserRequest invalidRequest = validRequest;
        invalidRequest.setEmail(invalidEmail);

        CreateUserResult result = createUserActivity.handleRequest(invalidRequest, null);

        assertEquals(invalidEmailException, result.getError());
        assertEquals(invalidEmailExceptionMessage, result.getErrorMessage());
    }

    @Test
    void handleRequest_nullUserId_throwsIllegalArgumentException() {

        CreateUserRequest nullUserRequest = CreateUserRequest.builder()
                .withEmail(validEmail)
                .build();

        CreateUserResult result = createUserActivity.handleRequest(nullUserRequest, null);

        assertEquals(illegalStateException, result.getError());
        assertEquals(illegalStateExceptionMessage, result.getErrorMessage());
    }

    @Test
    void handleRequest_nullEmail_throwsIllegalArgumentException() {

        CreateUserRequest nullEmailRequest = CreateUserRequest.builder()
                .withUserId(validId)
                .build();

        CreateUserResult result = createUserActivity.handleRequest(nullEmailRequest, null);

        assertEquals(illegalStateException, result.getError());
        assertEquals(illegalStateExceptionMessage, result.getErrorMessage());
    }

    @Test
    void handleRequest_userIdAlreadyInUse_throwsUserIdAlreadyExistsException() {

        when(userDao.isUnusedUserId(validId)).thenReturn(false);

        CreateUserResult result = createUserActivity.handleRequest(validRequest, null);

        assertEquals(userIdAlreadyExistsException, result.getError());
        assertEquals(userIdAlreadyExistsExceptionMessage, result.getErrorMessage());
    }

    @Test
    void handleRequest_emailAlreadyInUse_throwsEmailAlreadyExistsException() {

        when(userDao.isUnusedUserId(validId)).thenReturn(true);
        when(userDao.isUnusedEmail(validEmail)).thenReturn(false);

        CreateUserResult result = createUserActivity.handleRequest(validRequest, null);

        assertEquals(emailAlreadyExistsException, result.getError());
        assertEquals(emailAlreadyExistsExceptionMessage, result.getErrorMessage());
    }
}
