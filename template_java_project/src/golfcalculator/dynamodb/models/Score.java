package golfcalculator.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Represents a record in the Scores table.
 */
@DynamoDBTable(tableName = "Scores")
public class Score {
    private String userId;
    private String dateTime;
    private int rawScore;
    private double courseRating;
    private double slopeRating;
    private double handicapDifferential;
    private String courseName;

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBRangeKey(attributeName = "dateTime")
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @DynamoDBAttribute(attributeName = "rawScore")
    public int getRawScore() {
        return rawScore;
    }

    public void setRawScore(int rawScore) {
        this.rawScore = rawScore;
    }

    @DynamoDBAttribute(attributeName = "courseRating")
    public double getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(double courseRating) {
        this.courseRating = courseRating;
    }

    @DynamoDBAttribute(attributeName = "slopeRating")
    public double getSlopeRating() {
        return slopeRating;
    }

    public void setSlopeRating(double slopeRating) {
        this.slopeRating = slopeRating;
    }

    @DynamoDBAttribute(attributeName = "handicapDifferential")
    public double getHandicapDifferential() {
        return handicapDifferential;
    }

    public void setHandicapDifferential(double handicapDifferential) {
        this.handicapDifferential = handicapDifferential;
    }

    @DynamoDBAttribute(attributeName = "courseName")
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
