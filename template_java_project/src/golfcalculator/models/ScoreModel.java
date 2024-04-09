package golfcalculator.models;

import java.util.Objects;

public class ScoreModel {

    private String userId;
    private String dateTime;
    private double handicapIndex;

    public ScoreModel() {
    }

    public ScoreModel(Builder builder) {
        this.userId = builder.userId;
        this.dateTime = builder.dateTime;
        this.handicapIndex = builder.handicapIndex;
    }

    public static Builder builder() {return new Builder(); }

    public static final class Builder {
        private String userId;
        private String dateTime;
        private double handicapIndex;

        public Builder withUserId(String idToUse) {
            this.userId = idToUse;
            return this;
        }

        public Builder withDateTime(String dateTimeToUse) {
            this.dateTime = dateTimeToUse;
            return this;
        }

        public Builder withHandicapIndex(Double handicapToUse) {
            this.handicapIndex = handicapToUse;
            return this;
        }

        public ScoreModel builder() {return new ScoreModel(this);}
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

    public double getHandicapIndex() {
        return handicapIndex;
    }

    public void setHandicapIndex(double handicapIndex) {
        this.handicapIndex = handicapIndex;
    }

    @Override
    public String toString() {
        return "ScoreModel{" +
                "userId='" + userId + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", handicapIndex=" + handicapIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreModel that = (ScoreModel) o;
        return Double.compare(that.getHandicapIndex(), getHandicapIndex()) == 0 && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getDateTime(), that.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getDateTime(), getHandicapIndex());
    }
}
