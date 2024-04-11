package golfcalculator.activity;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.converters.ModelConverter;
import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.MinimumGamesNotPlayedException;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.GetLatestGamesRequest;
import golfcalculator.models.results.GetLatestGamesResult;

import javax.inject.Inject;
import java.util.List;

public class GetLatestGamesActivity implements RequestHandler<GetLatestGamesRequest, GetLatestGamesResult> {

    private UserDao userDao;
    private ScoreDao scoreDao;

    @Inject
    public GetLatestGamesActivity(UserDao userDao, ScoreDao scoreDao) {
        this.userDao = userDao;
        this.scoreDao = scoreDao;
    }

    @Override
    public GetLatestGamesResult handleRequest(GetLatestGamesRequest getLatestGamesRequest, Context context) {
        String userId = getLatestGamesRequest.getUserId();
        User user;
        try {
            user = userDao.getUser(userId);
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User account not found!");
        }

        int gamesPlayed = user.getGamesPlayed();
        if (gamesPlayed == 0) {
            throw new MinimumGamesNotPlayedException("Must play at least 1 game to see latest games!");
        }

        List<Score> scores = scoreDao.getLatest5Games(userId, gamesPlayed);
        return GetLatestGamesResult.builder().withScoreModels(ModelConverter.toListScoreModel(scores))
                .build();
    }
}
