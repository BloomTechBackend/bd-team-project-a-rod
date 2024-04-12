package golfcalculator.converters;

import golfcalculator.dynamodb.models.Score;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HandicapCalculatorTest {

    private List<Score> testScores;

    @BeforeEach
    private void setUp() {
        testScores = new ArrayList<>();
        // handicapIndex should be 7
        int currDifferential = 0;
        for (int i = 0; i < 20; i++) {
            Score score = new Score();
            score.setHandicapDifferential(currDifferential);
            currDifferential += 2;
            testScores.add(score);
        }
    }

    @Test
    void calculateHandicapDifferential_returnsCorrectDifferential() {
        int rawScore = 90;
        double courseRating = 72;
        double slopeRating = 113;

        double expectedHandicapDifferential = 18.0;

        double actualHandicapDifferential = HandicapCalculator.calculateHandicapDifferential(rawScore,
                courseRating, slopeRating);

        assertEquals(expectedHandicapDifferential, actualHandicapDifferential, 0.001);
    }

    @Test
    void calculateHandicapIndex_returnsCorrectIndex() {
        double expectedHandicapIndex = 7.0;

        double actualHandicapIndex = HandicapCalculator.calculateHandicapIndex(testScores);

        assertEquals(expectedHandicapIndex, actualHandicapIndex, 0.001);
    }
}
