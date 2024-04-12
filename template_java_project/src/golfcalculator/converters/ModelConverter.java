package golfcalculator.converters;

import golfcalculator.dynamodb.models.Score;
import golfcalculator.dynamodb.models.User;
import golfcalculator.models.ScoreModel;
import golfcalculator.models.UserModel;

import java.util.ArrayList;
import java.util.List;

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
        return ScoreModel.builder()
                .withUserId(score.getUserId())
                .withDateTime(score.getDateTime())
                .withHandicapDifferential(score.getHandicapDifferential())
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
