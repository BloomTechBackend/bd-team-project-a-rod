package golfcalculator.models.results;

import golfcalculator.models.requests.GetHandicapRequest;

/**
 * Result from user interaction with GetHandicapActivity API endpoint.
 */
public class GetHandicapResult {
    private double handicapIndex;
    private String error;
    private String errorMessage;

    public GetHandicapResult(Builder builder) {
        this.handicapIndex = builder.handicapIndex;
        this.error = builder.error;
        this.errorMessage = builder.errorMessage;
    }

    public static Builder builder() {return new Builder();}
    public static class Builder {
        private double handicapIndex;
        private String error;
        private String errorMessage;

        public Builder withHandicapIndex(double handicapIndex) {
            this.handicapIndex = handicapIndex;
            return this;
        }

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public Builder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public GetHandicapResult build() {return new GetHandicapResult(this);}
    }

    public double getHandicapIndex() {
        return handicapIndex;
    }

    public void setHandicapIndex(double handicapIndex) {
        this.handicapIndex = handicapIndex;
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
