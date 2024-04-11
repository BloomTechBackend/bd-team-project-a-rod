package golfcalculator.models.results;

import golfcalculator.models.requests.GetHandicapRequest;

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
}
