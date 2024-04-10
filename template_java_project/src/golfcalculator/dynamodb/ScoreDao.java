package golfcalculator.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import golfcalculator.dynamodb.models.Score;

import javax.inject.Inject;

public class ScoreDao {

    private DynamoDBMapper dynamoDBMapper;

    @Inject
    public ScoreDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void saveNewScore(Score newScore) {
        dynamoDBMapper.save(newScore);
    }
}
