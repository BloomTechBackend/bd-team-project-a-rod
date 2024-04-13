package golfcalculator.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserDaoTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    private PaginatedQueryList<User> mockPaginatedQueryList;

    @InjectMocks
    private UserDao userDao;

    private User validUser;
    private String validId = "validId";
    private String validEmail = "valid@email.com";

    @BeforeEach
    private void setUp() {
        initMocks(this);

        validUser = new User();
        validUser.setUserId(validId);
        validUser.setEmail(validEmail);
    }

    @Test
    void isUnusedUserId_userAlreadyExists_returnsFalse() {
        when(dynamoDBMapper.load(User.class, validId)).thenReturn(validUser);

        assertFalse(userDao.isUnusedUserId(validId));
    }

    @Test
    void isUnusedUserId_userDoesNotExist_returnsTrue() {
        when(dynamoDBMapper.load(User.class, validId)).thenReturn(null);

        assertTrue(userDao.isUnusedUserId(validId));
    }

    @Test
    void isUnusedEmail_emailDoesNotExist_returnsTrue() {
        when(dynamoDBMapper.query(eq(User.class), any(DynamoDBQueryExpression.class))).thenReturn(mockPaginatedQueryList);
        when(mockPaginatedQueryList.isEmpty()).thenReturn(true);

        boolean result = userDao.isUnusedEmail(validEmail);
        assertTrue(result);
        verify(dynamoDBMapper).query(eq(User.class), any(DynamoDBQueryExpression.class));
        verify(mockPaginatedQueryList).isEmpty();
    }

    @Test
    void isUnusedEmail_emailAlreadyExists_returnsFalse() {
        when(dynamoDBMapper.query(eq(User.class), any(DynamoDBQueryExpression.class))).thenReturn(mockPaginatedQueryList);
        when(mockPaginatedQueryList.isEmpty()).thenReturn(false);

        boolean result = userDao.isUnusedEmail(validEmail);
        assertFalse(result);
        verify(dynamoDBMapper).query(eq(User.class), any(DynamoDBQueryExpression.class));
        verify(mockPaginatedQueryList).isEmpty();
    }

    @Test
    void getUser_userExists_returnsUser() {
        when(dynamoDBMapper.load(eq(User.class), anyString())).thenReturn(validUser);

        assertEquals(validUser, userDao.getUser(validId));
    }

    @Test
    void getUser_userDoesNotExist_throwsUserNotFoundException() {
        when(dynamoDBMapper.load(eq(User.class), anyString())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userDao.getUser(validId);
        });
    }
}
