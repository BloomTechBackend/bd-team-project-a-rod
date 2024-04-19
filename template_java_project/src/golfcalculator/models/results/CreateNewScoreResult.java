package golfcalculator.models.results;

import golfcalculator.models.ScoreModel;

/**
 * Result from user interaction with CreateNewScoreActivity API endpoint.
 */
public class CreateNewScoreResult {

    private ScoreModel scoreModel;
    private String error;
    private String errorMessage;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
