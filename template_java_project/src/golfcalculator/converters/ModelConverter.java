package golfcalculator.converters;

import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.models.ScoreModel;
import golfcalculator.models.UserModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ModelConverter {

    /**
     * Convert User to UserModel
     * @param user the object to be converted
     * @return UserModel object
     */
    public static UserModel toUserModel(User user) {
        return UserModel.builder()
                .withUserId(user.getUserId())
                .withEmail(user.getEmail())
                .build();
    }

    /**
     * Convert Score to ScoreModel
     * @param score the object to be converted
     * @return ScoreModel object
     */
    public static ScoreModel toScoreModel(Score score) {

        ZonedDateTime parsedDate = ZonedDateTime.parse(score.getDateTime(), DateTimeFormatter.ISO_DATE_TIME);
        DateTimeFormatter userFriendlyFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.US);
        String userFriendlyDate = parsedDate.format(userFriendlyFormatter);

        return ScoreModel.builder()
                .withDateTime(userFriendlyDate)
                .withRawScore(score.getRawScore())
                // rounding differential to the nearest tenth
                .withHandicapDifferential(Math.round(score.getHandicapDifferential() * 10) / 10.0)
                .build();
    }

    /**
     * Leverages toScoreModel() to convert an entire List<Score>
     * @param scores scores to be converted
     * @return List<ScoreModel>
     */
    public static List<ScoreModel> toListScoreModel(List<Score> scores) {
        List<ScoreModel> scoreModels = new ArrayList<>();
        for (Score score : scores) {
            scoreModels.add(toScoreModel(score));
        }
        return scoreModels;
    }
}
