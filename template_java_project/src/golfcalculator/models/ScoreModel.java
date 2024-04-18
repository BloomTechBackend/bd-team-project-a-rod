package golfcalculator.models;

import java.util.Objects;

/**
 * Model of Score for data exposure to user.
 */
public class ScoreModel {
    private String dateTime;
    private int rawScore;
    private double handicapDifferential;

    public ScoreModel() {
    }

    public ScoreModel(Builder builder) {
        this.dateTime = builder.dateTime;
        this.rawScore = builder.rawScore;
        this.handicapDifferential = builder.handicapDifferential;
    }

    public static Builder builder() {return new Builder(); }

    public static final class Builder {
        private String dateTime;
        private int rawScore;
        private double handicapDifferential;

        public Builder withDateTime(String dateTimeToUse) {
            this.dateTime = dateTimeToUse;
            return this;
        }

        public Builder withRawScore(int rawScore) {
            this.rawScore = rawScore;
            return this;
        }

        public Builder withHandicapDifferential(Double handicapToUse) {
            this.handicapDifferential = handicapToUse;
            return this;
        }

        public ScoreModel build() {return new ScoreModel(this);}
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getRawScore() {
        return rawScore;
    }

    public void setRawScore(int rawScore) {
        this.rawScore = rawScore;
    }

    public double getHandicapDifferential() {
        return handicapDifferential;
    }

    public void setHandicapDifferential(double handicapDifferential) {
        this.handicapDifferential = handicapDifferential;
    }

    @Override
    public String toString() {
        return "Date: " + dateTime +
                ", Score: " + rawScore +
                ", Differential: " + handicapDifferential + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreModel that = (ScoreModel) o;
        return getRawScore() == that.getRawScore() && Double.compare(that.getHandicapDifferential(), getHandicapDifferential()) == 0 && Objects.equals(getDateTime(), that.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDateTime(), getRawScore(), getHandicapDifferential());
    }
}
