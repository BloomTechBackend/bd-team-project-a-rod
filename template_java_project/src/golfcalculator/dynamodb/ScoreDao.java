package golfcalculator.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import golfcalculator.dynamodb.models.Score;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class ScoreDao {

    private DynamoDBMapper dynamoDBMapper;

    @Inject
    public ScoreDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void saveNewScore(Score newScore) {
        dynamoDBMapper.save(newScore);
    }

    public List<Score> getLast20Games(String userId) {
        DynamoDBQueryExpression<Score> queryExpression = new DynamoDBQueryExpression<Score>()
                .withKeyConditionExpression("userId = :v_userId")
                .withExpressionAttributeValues(Map.of(":v_userId", new AttributeValue().withS(userId)))
                .withScanIndexForward(false) // For descending order by dateTime
                .withLimit(20);

        return dynamoDBMapper.query(Score.class, queryExpression);
    }

    // amount is at least 1 and at most 5
    public List<Score> getLatest5Games(String userId, int amount) {

        int max5Amount = amount > 5 ? 5 : amount;
        DynamoDBQueryExpression<Score> queryExpression = new DynamoDBQueryExpression<Score>()
                .withKeyConditionExpression("userId = :v_userId")
                .withExpressionAttributeValues(Map.of(":v_userId", new AttributeValue().withS(userId)))
                .withScanIndexForward(false)
                .withLimit(max5Amount);

        return dynamoDBMapper.query(Score.class, queryExpression);
    }
}
