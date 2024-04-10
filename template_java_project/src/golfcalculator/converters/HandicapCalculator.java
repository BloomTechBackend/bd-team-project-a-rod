package golfcalculator.converters;

public class HandicapCalculator {

    public static double calculateHandicapDifferential(int rawScore, double courseRating, double slopeRating) {
        return (rawScore - courseRating) * 113 / slopeRating;
    }
}
