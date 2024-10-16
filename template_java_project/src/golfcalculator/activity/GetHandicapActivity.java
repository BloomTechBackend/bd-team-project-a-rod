package golfcalculator.activity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.converters.HandicapCalculator;
import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.MinimumGamesNotPlayedException;
import golfcalculator.exceptions.UnexpectedServerQueryException;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.GetHandicapRequest;
import golfcalculator.models.results.GetHandicapResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class GetHandicapActivity implements RequestHandler<GetHandicapRequest, GetHandicapResult> {

    private final Logger log = LogManager.getLogger(GetHandicapActivity.class);
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

        GetHandicapResult errorResult = GetHandicapResult.builder().build();

        if (getHandicapRequest == null || getHandicapRequest.getUserId() == null || getHandicapRequest.getUserId().equals("")) {
            log.error("Request or User Id is null!");
            errorResult.setError("IllegalStateException");
            errorResult.setErrorMessage("Cannot leave User ID blank!");
            return errorResult;
            //throw new IllegalStateException("Cannot leave User ID blank!");
        }
        log.info("Request received: {}", getHandicapRequest.getUserId());

        String userId = getHandicapRequest.getUserId();
        User user;
        try {
            user = userDao.getUser(userId);
        } catch (UserNotFoundException ex) {
            log.error("User account not found {}", userId, ex);
            errorResult.setError("UserNotFoundException");
            errorResult.setErrorMessage("User account not found!");
            return errorResult;
            //throw new UserNotFoundException("User account not found!");
        } catch (DynamoDBMappingException e) {
            log.error("DynamoDBMappingException: User account not found!");
            errorResult.setError("UserNotFoundException");
            errorResult.setErrorMessage("User account not found!");
            return errorResult;
        }

        if (user.getGamesPlayed() < 20) {
            log.error("MinimumGamesNotPlayedException: gamesPlayed = {}", user.getGamesPlayed());
            errorResult.setError("MinimumGamesNotPlayedException");
            errorResult.setErrorMessage("Must play at least 20 games for handicap index!");
            return errorResult;
            //throw new MinimumGamesNotPlayedException("Must play at least 20 games for handicap index!");
        }

        // Create HandicapResult using HandicapCalculator to calculate Handicap Index
        List<Score> scores;
        try {
            scores = scoreDao.getLast20Games(userId);
        } catch (UnexpectedServerQueryException ex) {
            log.error("Server did not return expected 20 games", ex);
            errorResult.setError("UnexpectedServerQueryException");
            errorResult.setErrorMessage("Server did not return expected 20 games");
            return errorResult;
            //throw new UnexpectedServerQueryException("Server did not return expected 20 games");
        }

        double handicapIndex = HandicapCalculator.calculateHandicapIndex(scores);
        log.info("Response successfully created. Handicap Index = {}", handicapIndex);

        return GetHandicapResult.builder()
                .withHandicapIndex(handicapIndex)
                .build();
    }
}
