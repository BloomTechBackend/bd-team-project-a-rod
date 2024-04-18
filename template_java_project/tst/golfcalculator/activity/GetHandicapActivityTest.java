package golfcalculator.activity;

import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.MinimumGamesNotPlayedException;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.GetHandicapRequest;
import golfcalculator.models.results.GetHandicapResult;
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

public class GetHandicapActivityTest {

    @Mock
    private UserDao userDao;
    @Mock
    private ScoreDao scoreDao;
    @InjectMocks
    private GetHandicapActivity getHandicapActivity;

    private GetHandicapRequest validRequest;
    private String validId = "validId";
    private User validUser;
    private List<Score> testScores;
    private double expectedHandicapIndex = 7.0;

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

//    @Test
//    void handleRequest_unknownUserId_throwsUserNotFoundException() {
//
//        when(userDao.getUser(validId)).thenThrow(UserNotFoundException.class);
//
//        assertThrows(UserNotFoundException.class, () -> {
//            getHandicapActivity.handleRequest(validRequest, null);
//        });
//    }
//
//    @Test
//    void handleRequest_userIdGamesPlayedIsLessThan20_throwsMinimumGamesNotPlayedException() {
//
//        validUser.setGamesPlayed(19);
//        when(userDao.getUser(validId)).thenReturn(validUser);
//
//        assertThrows(MinimumGamesNotPlayedException.class, () -> {
//            getHandicapActivity.handleRequest(validRequest, null);
//        });
//    }
}
