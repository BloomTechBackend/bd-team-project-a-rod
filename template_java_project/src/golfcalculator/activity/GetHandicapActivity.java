package golfcalculator.activity;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.converters.HandicapCalculator;
import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.MinimumGamesNotPlayedException;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.GetHandicapRequest;
import golfcalculator.models.results.GetHandicapResult;

import javax.inject.Inject;
import java.util.List;

public class GetHandicapActivity implements RequestHandler<GetHandicapRequest, GetHandicapResult> {

    private UserDao userDao;
    private ScoreDao scoreDao;

    /**
     * The API endpoint in charge of returning calculated Handicap Index for user.
     * @param userDao userDao to access the Users table.
     * @param scoreDao scoreDao to access the Scores table.
     */
    @Inject
    public GetHandicapActivity(UserDao userDao, ScoreDao scoreDao) {
        this.userDao = userDao;
        this.scoreDao = scoreDao;
    }

    /**
     * This method uses userId from {@link GetHandicapRequest} to retrieve last 20 scores, then calculate Handicap
     * Index from them.
     * @param getHandicapRequest The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @exception UserNotFoundException if userDao cannot find requested account with the userId.
     * @exception MinimumGamesNotPlayedException if user has not played at least 20 games, which is the minimum
     * needed to calculate a Handicap Index using the official guidelines.
     * @return GetHandicapResult which only contains a double handicapIndex.
     */
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

        // Create HandicapResult using HandicapCalculator to calculate Handicap Index
        List<Score> scores = scoreDao.getLast20Games(userId);
        return GetHandicapResult.builder()
                .withHandicapIndex(HandicapCalculator.calculateHandicapIndex(scores))
                .build();
    }
}
