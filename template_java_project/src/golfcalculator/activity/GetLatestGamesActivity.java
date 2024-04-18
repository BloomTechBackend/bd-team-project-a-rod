package golfcalculator.activity;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.converters.ModelConverter;
import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.MinimumGamesNotPlayedException;
import golfcalculator.exceptions.UnexpectedServerQueryException;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.GetLatestGamesRequest;
import golfcalculator.models.results.GetLatestGamesResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class GetLatestGamesActivity implements RequestHandler<GetLatestGamesRequest, GetLatestGamesResult> {

    private final Logger log = LogManager.getLogger(GetLatestGamesActivity.class);
    private UserDao userDao;
    private ScoreDao scoreDao;

    /**
     * API endpoint in charge of returning latest 5 game scores info to user.
     * @param userDao userDao to access Users table.
     * @param scoreDao scoreDao to access Scores table.
     */
    @Inject
    public GetLatestGamesActivity(UserDao userDao, ScoreDao scoreDao) {
        this.userDao = userDao;
        this.scoreDao = scoreDao;
    }

    /**
     * Uses userId in {@link GetLatestGamesRequest} to validate user and retrieve latest games.
     * @param getLatestGamesRequest contains the userId.
     * @param context The Lambda execution environment context object.
     * @return GetLatestGamesResult, which is a List<ScoreModel> containing: userId, dateTime, handicapDifferential.
     */
    @Override
    public GetLatestGamesResult handleRequest(GetLatestGamesRequest getLatestGamesRequest, Context context) {

        if (getLatestGamesRequest == null || getLatestGamesRequest.getUserId() == null) {
            log.error("Request is null!");
            throw new IllegalStateException("Cannot leave User ID blank!");
        }

        log.info("GetLatestGamesRequest received: userID {}", getLatestGamesRequest.getUserId());

        String userId = getLatestGamesRequest.getUserId();
        User user;
        try {
            user = userDao.getUser(userId);
        } catch (UserNotFoundException ex) {
            log.error("User account not found {}", userId);
            throw new UserNotFoundException("User account not found!");
        }

        int gamesPlayed = user.getGamesPlayed();
        if (gamesPlayed == 0) {
            log.error("MinimumGamesNotPlayedException. Must play at least 1 game. gamesPlayed = {}", user.getGamesPlayed());
            throw new MinimumGamesNotPlayedException("Must play at least 1 game to see latest games!");
        }

        List<Score> scores;

        try {
            scores = scoreDao.getLatest5Games(userId, gamesPlayed);
        } catch (UnexpectedServerQueryException ex) {
            log.error("Server did not return between 1 and 5 games");
            throw new UnexpectedServerQueryException("Server did not return expected amount of games.");
        }

        log.info("Scores successfully returned to user");
        return GetLatestGamesResult.builder().withScoreModels(ModelConverter.toListScoreModel(scores))
                .build();
    }
}
