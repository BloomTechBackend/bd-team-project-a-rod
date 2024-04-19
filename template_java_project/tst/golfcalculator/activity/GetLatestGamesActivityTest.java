package golfcalculator.activity;

import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.MinimumGamesNotPlayedException;
import golfcalculator.exceptions.UnexpectedServerQueryException;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.GetLatestGamesRequest;
import golfcalculator.models.results.GetLatestGamesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetLatestGamesActivityTest {

    @Mock
    private UserDao userDao;
    @Mock
    private ScoreDao scoreDao;
    @InjectMocks
    private GetLatestGamesActivity getLatestGamesActivity;

    private GetLatestGamesRequest request;
    private GetLatestGamesRequest nullRequest;
    private String userId = "userId";
    private User user;
    private List<Score> last5Scores;
    private String illegalStateException = "IllegalStateException";
    private String illegalStateExceptionMessage = "Cannot leave User ID blank!";
    private String userNotFoundException = "UserNotFoundException";
    private String userNotFoundExceptionMessage = "User account not found!";
    private String minimumGamesNotPlayedException = "MinimumGamesNotPlayedException";
    private String minimumGamesNotPlayedExceptionMessage = "Must play at least 1 game to see latest games!";
    private String unexpectedServerQueryException = "UnexpectedServerQueryException";
    private String unexpectedServerQueryExceptionMessage = "Server did not return expected amount of games.";

    @BeforeEach
    private void setUp() {
        initMocks(this);

        request = GetLatestGamesRequest.builder()
                .withUserId(userId)
                .build();

        user = new User();
        user.setUserId(userId);
        user.setGamesPlayed(5);

        last5Scores = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Score newScore = new Score();
            newScore.setUserId(userId);
            newScore.setDateTime("2024-04-14T04:24:39.928823Z");
            newScore.setHandicapDifferential(i * 2);
            last5Scores.add(newScore);
        }
    }

    @Test
    void handleRequest_validRequest_returnsExpectedResult() {
        when(userDao.getUser(userId)).thenReturn(user);
        when(scoreDao.getLatest5Games(userId, user.getGamesPlayed())).thenReturn(last5Scores);

        int listSize = last5Scores.size();
        GetLatestGamesResult result = getLatestGamesActivity.handleRequest(request, null);
        assertEquals(listSize, result.getScoreModels().size());
    }

    @Test
    void handleRequest_requestIsNull_throwsIllegalStateException() {

        GetLatestGamesResult result = getLatestGamesActivity.handleRequest(nullRequest, null);

        assertEquals(illegalStateException, result.getError());
        assertEquals(illegalStateExceptionMessage, result.getErrorMessage());
        assertNull(result.getScoreModels());
    }

    @Test
    void handleRequest_userIdNotFound_throwsUserNotFoundException() {
        when(userDao.getUser(userId)).thenThrow(UserNotFoundException.class);

        GetLatestGamesResult result = getLatestGamesActivity.handleRequest(request, null);

        assertEquals(userNotFoundException, result.getError());
        assertEquals(userNotFoundExceptionMessage, result.getErrorMessage());
        assertNull(result.getScoreModels());
    }

    @Test
    void handleRequest_hasNotPlayedAGame_throwsMinimumGamesNotPlayedException() {
        user.setGamesPlayed(0);
        when(userDao.getUser(userId)).thenReturn(user);

        GetLatestGamesResult result = getLatestGamesActivity.handleRequest(request, null);

        assertEquals(minimumGamesNotPlayedException, result.getError());
        assertEquals(minimumGamesNotPlayedExceptionMessage, result.getErrorMessage());
        assertNull(result.getScoreModels());
    }

    @Test
    void handleRequest_serverQueryError_throwsUnexpectedServerQueryException() {
        when(userDao.getUser(anyString())).thenReturn(user);
        when(scoreDao.getLatest5Games(anyString(), anyInt())).thenThrow(UnexpectedServerQueryException.class);

        GetLatestGamesResult result = getLatestGamesActivity.handleRequest(request, null);

        assertEquals(unexpectedServerQueryException, result.getError());
        assertEquals(unexpectedServerQueryExceptionMessage, result.getErrorMessage());
        assertNull(result.getScoreModels());
    }
}
