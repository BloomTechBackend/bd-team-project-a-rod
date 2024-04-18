package golfcalculator.models.results;

import golfcalculator.models.requests.GetHandicapRequest;

/**
 * Result from user interaction with GetHandicapActivity API endpoint.
 */
public class GetHandicapResult {
    private double handicapIndex;

    public GetHandicapResult(Builder builder) {
        this.handicapIndex = builder.handicapIndex;
    }

    public static Builder builder() {return new Builder();}
    public static class Builder {
        private double handicapIndex;

        public Builder withHandicapIndex(double handicapIndex) {
            this.handicapIndex = handicapIndex;
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

    @Override
    public String toString() {
        return "Handicap Index: " + handicapIndex;
    }
}
