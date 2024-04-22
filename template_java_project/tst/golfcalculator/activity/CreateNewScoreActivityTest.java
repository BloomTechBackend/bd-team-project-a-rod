package golfcalculator.activity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.CreateNewScoreRequest;
import golfcalculator.models.results.CreateNewScoreResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CreateNewScoreActivityTest {

    @Mock
    private UserDao userDao;
    @Mock
    private ScoreDao scoreDao;
    @InjectMocks
    private CreateNewScoreActivity createNewScoreActivity;

    private CreateNewScoreRequest validCreateNewScoreRequest;
    private User validUser;
    private String validUserId = "validUser";
    double validScoreResultIndex;
    private String illegalStateException = "IllegalStateException";
    private String illegalStateExceptionMessage = "Please fill in required fields!";
    private String userNotFoundException = "UserNotFoundException";
    private String userNotFoundExceptionMessage = "Could not find User account!";

    @BeforeEach
    private void setUp() {
        initMocks(this);
        validCreateNewScoreRequest = CreateNewScoreRequest.builder()
                .withUserId(validUserId)
                .withRawScore(83)
                .withCourseRating(93.2)
                .withSlopeRating(111.5)
                .withCourseName("Wind Parks")
                .build();

        validScoreResultIndex = -10.34;

        validUser = new User();
        validUser.setUserId(validUserId);
        validUser.setEmail("valid@email.com");
        validUser.setGamesPlayed(3);
    }

    @Test
    void handleRequest_validRequest_createsExpectedScoreResult() {
        when(userDao.getUser(validUserId)).thenReturn(validUser);

        int newGamesPlayed = validUser.getGamesPlayed() + 1;
        ArgumentCaptor<Score> scoreCaptor = ArgumentCaptor.forClass(Score.class);

        CreateNewScoreResult scoreResult = createNewScoreActivity.handleRequest(validCreateNewScoreRequest, null);

        verify(userDao).saveUser(validUser);
        verify(scoreDao).saveNewScore(scoreCaptor.capture());
        assertEquals(newGamesPlayed, validUser.getGamesPlayed());
        assertNotNull(scoreResult.getScoreModel().getDateTime());
        assertEquals(validScoreResultIndex, scoreResult.getScoreModel().getHandicapDifferential(), 0.1);
    }

    @Test
    void handleRequest_incorrectUserId_throwsUserNotFoundException() {
        String nonExistentUserId = "nonExistendUserId";
        CreateNewScoreRequest badUserIdRequest = validCreateNewScoreRequest;
        badUserIdRequest.setUserId(nonExistentUserId);

        when(userDao.getUser(nonExistentUserId)).thenThrow(new UserNotFoundException());

        CreateNewScoreResult result = createNewScoreActivity.handleRequest(badUserIdRequest, null);

        verify(userDao).getUser(nonExistentUserId);
        verifyNoInteractions(scoreDao);
        assertEquals(userNotFoundException, result.getError());
        assertEquals(userNotFoundExceptionMessage, result.getErrorMessage());
        assertNull(result.getScoreModel());
    }

    @Test
    void handleRequest_rawScoreMissing_throwsIllegalStateException() {
        CreateNewScoreRequest noRawScore = validCreateNewScoreRequest;
        noRawScore.setRawScore(0);

        CreateNewScoreResult result = createNewScoreActivity.handleRequest(noRawScore, null);

        assertEquals(illegalStateException, result.getError());
        assertEquals(illegalStateExceptionMessage, result.getErrorMessage());
        assertNull(result.getScoreModel());
    }

    @Test
    void handleRequest_dynamoDBException_throwsUserNotFoundException() {

        when(userDao.getUser(anyString())).thenThrow(DynamoDBMappingException.class);

        CreateNewScoreResult result = createNewScoreActivity.handleRequest(validCreateNewScoreRequest, null);

        assertEquals(userNotFoundException, result.getError());
        assertEquals(userNotFoundExceptionMessage, result.getErrorMessage());
        assertNull(result.getScoreModel());
    }
}
