package golfcalculator.activity;

import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.MinimumGamesNotPlayedException;
import golfcalculator.exceptions.UnexpectedServerQueryException;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.GetHandicapRequest;
import golfcalculator.models.results.GetHandicapResult;
import golfcalculator.models.results.GetLatestGamesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetHandicapActivityTest {

    @Mock
    private UserDao userDao;
    @Mock
    private ScoreDao scoreDao;
    @InjectMocks
    private GetHandicapActivity getHandicapActivity;

    private GetHandicapRequest validRequest;
    private GetHandicapRequest nullRequest;
    private String validId = "validId";
    private User validUser;
    private List<Score> testScores;
    private double expectedHandicapIndex = 7.0;
    private String userNotFoundException = "UserNotFoundException";
    private String userNotFoundExceptionMessage = "User account not found!";
    private String minimumGamesNotPlayedException = "MinimumGamesNotPlayedException";
    private String minimumGamesNotPlayedExceptionMessage = "Must play at least 20 games for handicap index!";
    private String illegalStateException = "IllegalStateException";
    private String illegalStateExceptionMessage = "Cannot leave User ID blank!";
    private String unexpectedServerQueryException = "UnexpectedServerQueryException";
    private String unexpectedServerQueryExceptionMessage = "Server did not return expected 20 games";

    @BeforeEach
    private void setUp() {
        initMocks(this);

        validRequest = GetHandicapRequest.builder()
                .withUserId(validId)
                .build();

        validUser = new User();
        validUser.setUserId(validId);
        validUser.setGamesPlayed(20);

        testScores = new ArrayList<>();
        // handicapIndex should be 7
        int currDifferential = 0;
        for (int i = 0; i < 20; i++) {
            Score score = new Score();
            score.setHandicapDifferential(currDifferential);
            currDifferential += 2;
            testScores.add(score);
        }
    }

    @Test
    void handleRequest_validRequest_expectedResult() {
        when(userDao.getUser(validId)).thenReturn(validUser);
        when(scoreDao.getLast20Games(validId)).thenReturn(testScores);

        GetHandicapResult result = getHandicapActivity.handleRequest(validRequest, null);

        assertEquals(expectedHandicapIndex, result.getHandicapIndex());
    }

    @Test
    void handleRequest_userIdNotProvided_throwsIllegalStateException() {

        GetHandicapResult result = getHandicapActivity.handleRequest(nullRequest, null);

        assertEquals(illegalStateException, result.getError());
        assertEquals(illegalStateExceptionMessage, result.getErrorMessage());
        assertEquals(0, result.getHandicapIndex());
    }

    @Test
    void handleRequest_unknownUserId_throwsUserNotFoundException() {

        when(userDao.getUser(validId)).thenThrow(UserNotFoundException.class);

        GetHandicapResult result = getHandicapActivity.handleRequest(validRequest, null);

        assertEquals(userNotFoundException, result.getError());
        assertEquals(userNotFoundExceptionMessage, result.getErrorMessage());
        assertEquals(0, result.getHandicapIndex());
    }

    @Test
    void handleRequest_userIdGamesPlayedIsLessThan20_throwsMinimumGamesNotPlayedException() {

        validUser.setGamesPlayed(19);
        when(userDao.getUser(validId)).thenReturn(validUser);

        GetHandicapResult result = getHandicapActivity.handleRequest(validRequest, null);

        assertEquals(minimumGamesNotPlayedException, result.getError());
        assertEquals(minimumGamesNotPlayedExceptionMessage, result.getErrorMessage());
        assertEquals(0, result.getHandicapIndex());
    }

    @Test
    void handleRequest_unexpectedServerQuery_throwsUnexpectedServerQueryException() {

        when(scoreDao.getLast20Games(anyString())).thenThrow(UnexpectedServerQueryException.class);
        when(userDao.getUser(anyString())).thenReturn(validUser);

        GetHandicapResult result = getHandicapActivity.handleRequest(validRequest, null);

        assertEquals(unexpectedServerQueryException, result.getError());
        assertEquals(unexpectedServerQueryExceptionMessage, result.getErrorMessage());
        assertEquals(0, result.getHandicapIndex());
    }
}
