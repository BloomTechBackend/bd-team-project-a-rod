package golfcalculator.activity;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.converters.HandicapCalculator;
import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.MinimumGamesNotPlayedException;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.GetHandicapRequest;
import golfcalculator.models.results.GetHandicapResult;

import javax.inject.Inject;

public class GetHandicapActivity implements RequestHandler<GetHandicapRequest, GetHandicapResult> {

    private UserDao userDao;
    private ScoreDao scoreDao;

    @Inject
    public GetHandicapActivity(UserDao userDao, ScoreDao scoreDao) {
        this.userDao = userDao;
        this.scoreDao = scoreDao;
    }

    @Override
    public GetHandicapResult handleRequest(GetHandicapRequest getHandicapRequest, Context context) {

        String userId = getHandicapRequest.getUserId();
        User user;
        try {
            user = userDao.getUser(userId);
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User account not found!");
        }

        if (user.getGamesPlayed() < 20) {
            throw new MinimumGamesNotPlayedException("Must play at least 20 games for handicap index!");
        }

        // Create HandicapResult with best 8 games of last 20 recently played
        return GetHandicapResult.builder()
                .withHandicapIndex(HandicapCalculator.calculateHandicapIndex(scoreDao.getLast20Games(userId)))
                .build();
    }
}
