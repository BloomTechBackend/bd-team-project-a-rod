package golfcalculator.models;

import java.util.Objects;

/**
 * Model of Score for data exposure to user.
 */
public class ScoreModel {

    private String userId;
    private String dateTime;
    private double handicapDifferential;

    public ScoreModel() {
    }

    public ScoreModel(Builder builder) {
        this.userId = builder.userId;
        this.dateTime = builder.dateTime;
        this.handicapDifferential = builder.handicapDifferential;
    }

    public static Builder builder() {return new Builder(); }

    public static final class Builder {
        private String userId;
        private String dateTime;
        private double handicapDifferential;

        public Builder withUserId(String idToUse) {
            this.userId = idToUse;
            return this;
        }

        public Builder withDateTime(String dateTimeToUse) {
            this.dateTime = dateTimeToUse;
            return this;
        }

        public Builder withHandicapDifferential(Double handicapToUse) {
            this.handicapDifferential = handicapToUse;
            return this;
        }

        public ScoreModel build() {return new ScoreModel(this);}
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getHandicapDifferential() {
        return handicapDifferential;
    }

    public void setHandicapDifferential(double handicapDifferential) {
        this.handicapDifferential = handicapDifferential;
    }

    @Override
    public String toString() {
        return "ScoreModel{" +
                "userId='" + userId + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", handicapDifferential=" + handicapDifferential +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreModel that = (ScoreModel) o;
        return Double.compare(that.getHandicapDifferential(), getHandicapDifferential()) == 0 && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getDateTime(), that.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getDateTime(), getHandicapDifferential());
    }
}
