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

        List<Double> best8HandicapDifferentials = scores.stream()
                .map(Score::getHandicapDifferential)
                .sorted((a, b) -> Double.compare(a, b))
                .limit(8)
                .collect(Collectors.toList());

        double sum = 0;
        for (double d : best8HandicapDifferentials) {
            sum += d;
        }
        return sum / 8;
    }
}
