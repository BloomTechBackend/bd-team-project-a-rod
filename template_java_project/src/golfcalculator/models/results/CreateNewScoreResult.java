package golfcalculator.models.results;

import golfcalculator.models.ScoreModel;

/**
 * Result from user interaction with CreateNewScoreActivity API endpoint.
 */
public class CreateNewScoreResult {

    private ScoreModel scoreModel;

    public CreateNewScoreResult(Builder builder) {
        this.scoreModel = builder.scoreModel;
    }

    public static Builder builder() {return new Builder();}

    public static class Builder {
        private ScoreModel scoreModel;

        public Builder withScoreModel(ScoreModel scoreModel) {
            this.scoreModel = scoreModel;
            return this;
        }

        public CreateNewScoreResult build() {return new CreateNewScoreResult(this);}
    }

    public ScoreModel getScoreModel() {
        return scoreModel;
    }

    public void setScoreModel(ScoreModel scoreModel) {
        this.scoreModel = scoreModel;
    }
}
