package golfcalculator.activity;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.converters.HandicapCalculator;
import golfcalculator.converters.ModelConverter;
import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.UserNotFoundException;
import golfcalculator.models.requests.CreateNewScoreRequest;
import golfcalculator.models.results.CreateNewScoreResult;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CreateNewScoreActivity implements RequestHandler<CreateNewScoreRequest, CreateNewScoreResult> {

    private UserDao userDao;
    private ScoreDao scoreDao;

    @Inject
    public CreateNewScoreActivity(UserDao userDao, ScoreDao scoreDao) {
        this.userDao = userDao;
        this.scoreDao = scoreDao;
    }

    @Override
    public CreateNewScoreResult handleRequest(CreateNewScoreRequest createNewScoreRequest, Context context) {
        String userId = createNewScoreRequest.getUserId();
        User user;
        try {
            user = userDao.getUser(userId);
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("Could not find User account!");
        }

        // increment user gamesPlayed
        user.setGamesPlayed(user.getGamesPlayed() + 1);
        userDao.saveUser(user);

        // gathering all Score attributes
        // generating UTC datetime String
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime utcNow = now.withZoneSameInstant(ZoneOffset.UTC);
        String dateTime = utcNow.format(DateTimeFormatter.ISO_DATE_TIME);


        int rawScore = createNewScoreRequest.getRawScore();
        double courseRating = createNewScoreRequest.getCourseRating();
        double slopeRating = createNewScoreRequest.getSlopeRating();
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

        return CreateNewScoreResult.builder()
                .withScoreModel(ModelConverter.toScoreModel(newScore))
                .build();
    }
}
