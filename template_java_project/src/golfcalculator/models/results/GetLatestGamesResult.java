package golfcalculator.models.results;

import golfcalculator.models.ScoreModel;

import java.util.List;

/**
 * Result from user interaction with GetLatestGamesActivity API endpoint.
 */
public class GetLatestGamesResult {
    private List<ScoreModel> scoreModels;
    private String error;
    private String errorMessage;

    public GetLatestGamesResult(Builder builder) {
        this.scoreModels = builder.scoreModels;
    }
    public static Builder builder() {return new Builder();}
    public static class Builder {
        private List<ScoreModel> scoreModels;

        public Builder withScoreModels(List<ScoreModel> scoreModels) {
            this.scoreModels = scoreModels;
            return this;
        }

        public GetLatestGamesResult build() {return new GetLatestGamesResult(this);}
    }

    public List<ScoreModel> getScoreModels() {
        return scoreModels;
    }

    public void setScoreModels(List<ScoreModel> scoreModels) {
        this.scoreModels = scoreModels;
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
