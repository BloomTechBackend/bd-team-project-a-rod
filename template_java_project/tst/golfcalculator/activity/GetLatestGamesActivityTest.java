package golfcalculator.activity;

import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.MinimumGamesNotPlayedException;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.GetLatestGamesRequest;
import golfcalculator.models.results.GetLatestGamesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private String userId = "userId";
    private User user;
    private List<Score> last5Scores;

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
    void handleRequest_userIdNotFound_throwsUserNotFoundException() {
        when(userDao.getUser(userId)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> {
            getLatestGamesActivity.handleRequest(request, null);
        });
    }

    @Test
    void handleRequest_hasNotPlayedAGame_throwsMinimumGamesNotPlayedException() {
        user.setGamesPlayed(0);
        when(userDao.getUser(userId)).thenReturn(user);

        assertThrows(MinimumGamesNotPlayedException.class, () -> {
            getLatestGamesActivity.handleRequest(request, null);
        });
    }
}
