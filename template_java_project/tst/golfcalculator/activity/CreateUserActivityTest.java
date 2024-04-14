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
        invalidRequest.setId(invalidId);

        assertThrows(InvalidUserNameException.class, () -> {
            createUserActivity.handleRequest(invalidRequest, null);
        });
    }

    @Test
    void handleRequest_invalidEmail_throwsInvalidEmailException() {

        CreateUserRequest invalidRequest = validRequest;
        invalidRequest.setEmail(invalidEmail);

        assertThrows(InvalidEmailException.class, () -> {
            createUserActivity.handleRequest(invalidRequest, null);
        });
    }

    @Test
    void handleRequest_nullUserId_throwsIllegalArgumentException() {

        CreateUserRequest nullUserRequest = CreateUserRequest.builder()
                .withEmail(validEmail)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            createUserActivity.handleRequest(nullUserRequest, null);
        });
    }

    @Test
    void handleRequest_nullEmail_throwsIllegalArgumentException() {

        CreateUserRequest nullEmailRequest = CreateUserRequest.builder()
                .withUserId(validId)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            createUserActivity.handleRequest(nullEmailRequest, null);
        });
    }

    @Test
    void handleRequest_userIdAlreadyInUse_throwsUserIdAlreadyExistsException() {

        when(userDao.isUnusedUserId(validId)).thenReturn(false);

        assertThrows(UserIdAlreadyExistsException.class, () -> {
            createUserActivity.handleRequest(validRequest, null);
        });
    }

    @Test
    void handleRequest_emailAlreadyInUse_throwsEmailAlreadyExistsException() {

        when(userDao.isUnusedUserId(validId)).thenReturn(true);
        when(userDao.isUnusedEmail(validEmail)).thenReturn(false);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            createUserActivity.handleRequest(validRequest, null);
        });
    }
}
