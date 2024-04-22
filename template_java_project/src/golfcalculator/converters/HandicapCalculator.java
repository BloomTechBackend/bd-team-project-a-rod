package golfcalculator.converters;

import golfcalculator.dynamodb.models.Score;

import java.util.List;
import java.util.stream.Collectors;

public class HandicapCalculator {

    /**
     * Plugs in values into standard Handicap Differential formula.
     * @param rawScore 18 round golf score.
     * @param courseRating Golf course's course rating.
     * @param slopeRating Golf course's slope rating.
     * @return calculated handicap differential.
     */
    public static double calculateHandicapDifferential(int rawScore, double courseRating, double slopeRating) {
        return (rawScore - courseRating) * 113 / slopeRating;
    }

    /**
     * Creates a list of the 8 lowest (lower is better) handicap differentials.
     * Then averages this list.
     * @param scores List of the user's last 20 golf scores.
     * @return average of their 8 best handicap differentials.
     */
    public static double calculateHandicapIndex(List<Score> scores) {

        double average = scores.stream()
                .mapToDouble(Score::getHandicapDifferential)
                .sorted()
                .limit(8)
                .average()
                .orElse(0);

        return Math.round(average * 10) / 10.0;
    }
}
