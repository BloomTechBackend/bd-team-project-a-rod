package golfcalculator.converters;

import golfcalculator.dynamodb.models.Score;

import java.util.List;
import java.util.stream.Collectors;

public class HandicapCalculator {

    public static double calculateHandicapDifferential(int rawScore, double courseRating, double slopeRating) {
        return (rawScore - courseRating) * 113 / slopeRating;
    }

    public static double calculateHandicapIndex(List<Score> scores) {

        List<Double> top8HandicapDifferentials = scores.stream()
                .map(Score::getHandicapDifferential)
                .sorted((a, b) -> Double.compare(b, a))
                .limit(8)
                .collect(Collectors.toList());

        double sum = 0;
        for (double d : top8HandicapDifferentials) {
            sum += d;
        }
        return sum / 8;
    }
}
