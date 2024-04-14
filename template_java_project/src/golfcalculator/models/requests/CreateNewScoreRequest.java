package golfcalculator.models.requests;

import java.util.Objects;

/**
 * Request for CreateNewScoreActivity API endpoint
 */
public class CreateNewScoreRequest {

    private String userId;
    private int rawScore;
    private double courseRating;
    private double slopeRating;
    private String courseName;

    public CreateNewScoreRequest() {
    }

    public CreateNewScoreRequest(Builder builder) {
        this.userId = builder.userId;
        this.rawScore = builder.rawScore;
        this.courseRating = builder.courseRating;
        this.slopeRating = builder.slopeRating;
        this.courseName = builder.courseName;
    }

    public static Builder builder() {return new Builder();}
    public static class Builder {
        private String userId;
        private int rawScore;
        private double courseRating = 72.0;
        private double slopeRating = 113.0;
        private String courseName;

        public Builder withUserId(String idToUse) {
            this.userId = idToUse;
            return this;
        }
        public Builder withRawScore(int scoreToUse) {
            this.rawScore = scoreToUse;
            return this;
        }
        public Builder withCourseRating(double courseRating) {
            if (courseRating > 0) {
                this.courseRating = courseRating;
            }
            return this;
        }
        public Builder withSlopeRating(double slopeRating) {
            if (slopeRating > 0) {
                this.slopeRating = slopeRating;
            }
            return this;
        }
        public Builder withCourseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public CreateNewScoreRequest build() {
            return new CreateNewScoreRequest(this);}
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRawScore() {
        return rawScore;
    }

    public void setRawScore(int rawScore) {
        this.rawScore = rawScore;
    }

    public double getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(double courseRating) {
        this.courseRating = courseRating;
    }

    public double getSlopeRating() {
        return slopeRating;
    }

    public void setSlopeRating(double slopeRating) {
        this.slopeRating = slopeRating;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "CreateNewScoreRequest{" +
                "userId='" + userId + '\'' +
                ", rawScore=" + rawScore +
                ", courseRating=" + courseRating +
                ", slopeRating=" + slopeRating +
                ", courseName='" + courseName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateNewScoreRequest that = (CreateNewScoreRequest) o;
        return getRawScore() == that.getRawScore() && Double.compare(that.getCourseRating(), getCourseRating()) == 0 && Double.compare(that.getSlopeRating(), getSlopeRating()) == 0 && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getCourseName(), that.getCourseName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRawScore(), getCourseRating(), getSlopeRating(), getCourseName());
    }
}
