package golfcalculator.activity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.converters.HandicapCalculator;
import golfcalculator.converters.ModelConverter;
import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.ScoreModel;
import golfcalculator.models.requests.CreateNewScoreRequest;
import golfcalculator.models.results.CreateNewScoreResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Implementation of the CreateNewScoreActivity for the GolfCalulcator's CreateNewScore API.
 *
 * This API allows the customer to add a new golf score associated with their account.
 */
public class CreateNewScoreActivity implements RequestHandler<CreateNewScoreRequest, CreateNewScoreResult> {

    private final Logger log = LogManager.getLogger(CreateNewScoreActivity.class);
    private UserDao userDao;
    private ScoreDao scoreDao;

    /**
     * Instantiates a new CreateNewScoreActivity object.
     *
     * @param userDao UserDao to access the Users table.
     * @param scoreDao ScoreDao to access the Scores table.
     */
    @Inject
    public CreateNewScoreActivity(UserDao userDao, ScoreDao scoreDao) {
        this.userDao = userDao;
        this.scoreDao = scoreDao;
    }

    /**
     * This method handles the incoming request by persisting a new Score
     * with the provided userId, rawScore, courseRating, slopeRating, and courseName.
     *
     * It calculates the handicapDifferential using score data, then stores this attribute.
     * It also stores the UTC dateTime.
     *
     * It then returns a result object containing: userId, handicapDifferential, and dateTime.
     *
     * @exception IllegalArgumentException if Raw Score isn't provided in CreateNewScoreRequest.
     * @exception UserNotFoundException if userId doesn't exist in Users table.
     * @param createNewScoreRequest The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return createNewScoreResult result object containing the API defined {@link ScoreModel}
     */
    @Override
    public CreateNewScoreResult handleRequest(CreateNewScoreRequest createNewScoreRequest, Context context) {

        CreateNewScoreResult errorResult = CreateNewScoreResult.builder().build();

        double defaultCourseRating = 72.0;
        double defaultSlopeRating = 113.0;

        // Create accurate logging taking account nuances of createNewScoreRequest
        String logMessage = "Received new score request: ";
        if (createNewScoreRequest == null || createNewScoreRequest.getUserId() == null ||
                createNewScoreRequest.getUserId().equals("") || createNewScoreRequest.getRawScore() == 0) {
            log.error(logMessage + "request is null!");
            errorResult.setError("IllegalStateException");
            errorResult.setErrorMessage("Please fill in required fields!");
            return errorResult;
            //throw new IllegalStateException("Error: Empty request!");
        }
        logMessage += "userId = " + createNewScoreRequest.getUserId() + ", rawScore = "
                + createNewScoreRequest.getRawScore();
        logMessage += ", courseRating = ";
        if (createNewScoreRequest.getCourseRating() != 0.0) {
            logMessage += createNewScoreRequest.getCourseRating();
        } else {
            logMessage += "DEFAULT (72.0)";
        }
        logMessage += ", slopeRating = ";
        if (createNewScoreRequest.getSlopeRating() != 0.0) {
            logMessage += createNewScoreRequest.getSlopeRating();
        } else {
            logMessage += "DEFAULT (113.0)";
        }
        logMessage += ", courseName = ";
        if (createNewScoreRequest.getCourseName() != null) {
            logMessage += createNewScoreRequest.getCourseName();
        } else {
            logMessage += "null";
        }
        log.info(logMessage);

        String userId = createNewScoreRequest.getUserId();
        User user;
        try {
            user = userDao.getUser(userId);
        } catch (UserNotFoundException ex) {
            log.error("Could not find User account from request: {}", createNewScoreRequest, ex);
            errorResult.setError("UserNotFoundException");
            errorResult.setErrorMessage("Could not find User account!");
            return errorResult;
            //throw new UserNotFoundException("Could not find User account!");
        } catch (DynamoDBMappingException ex) {
            log.error(ex);
            errorResult.setError("UserNotFoundException");
            errorResult.setErrorMessage("Could not find User account!");
            return errorResult;
        }

        log.info("User was found: ", userId);

        // increment user gamesPlayed
        user.setGamesPlayed(user.getGamesPlayed() + 1);
        userDao.saveUser(user);

        // gathering all Score attributes
        // generating UTC datetime String
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime utcNow = now.withZoneSameInstant(ZoneOffset.UTC);
        String dateTime = utcNow.format(DateTimeFormatter.ISO_DATE_TIME);

        int rawScore = createNewScoreRequest.getRawScore();

        // Want to make it optional for user to have to choose course and slope ratings
        // That way they can be lazy if they don't care about accuracy
        double courseRating = createNewScoreRequest.getCourseRating() > 0 ?
                createNewScoreRequest.getCourseRating() : defaultCourseRating;
        double slopeRating = createNewScoreRequest.getSlopeRating() > 0 ?
                createNewScoreRequest.getSlopeRating() : defaultSlopeRating;

        String courseName = createNewScoreRequest.getCourseName();

        // Get handicapDifferential
        double handicapDifferential = HandicapCalculator.calculateHandicapDifferential(rawScore,
                courseRating, slopeRating);

        // Create newScore
        Score newScore = new Score();
        newScore.setUserId(userId);
        newScore.setDateTime(dateTime);
        newScore.setRawScore(rawScore);
        newScore.setCourseRating(courseRating);
        newScore.setSlopeRating(slopeRating);
        newScore.setHandicapDifferential(handicapDifferential);
        newScore.setCourseName(courseName);

        scoreDao.saveNewScore(newScore);
        log.info("New score saved: {}", newScore);

        return CreateNewScoreResult.builder()
                .withScoreModel(ModelConverter.toScoreModel(newScore))
                .build();
    }
}
