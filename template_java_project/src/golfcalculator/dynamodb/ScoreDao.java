package golfcalculator.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.exceptions.UnexpectedServerQueryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreDao {

    private final Logger log = LogManager.getLogger(ScoreDao.class);
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Instantiates a ScoreDao object.
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with Scores table.
     */
    @Inject
    public ScoreDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Stores a new Score to Scores table.
     * @param newScore the latest user Score submission to be saved.
     */
    public void saveNewScore(Score newScore) {
        dynamoDBMapper.save(newScore);
    }

    /**
     * Used for GetHandicapActivity endpoint.
     * DynamoDB query to retrieve last 20 Scores of User.
     * dateTime is the sort key, sorted in descending order.
     * @param userId the partition key for Scores table.
     * @return List of last 20 Scores
     */
    public List<Score> getLast20Games(String userId) {
        DynamoDBQueryExpression<Score> queryExpression = new DynamoDBQueryExpression<Score>()
                .withKeyConditionExpression("userId = :v_userId")
                .withExpressionAttributeValues(Map.of(":v_userId", new AttributeValue().withS(userId)))
                .withScanIndexForward(false) // For descending order by dateTime
                .withLimit(20);

        PaginatedQueryList<Score> result = dynamoDBMapper.query(Score.class, queryExpression);

        if (result.size() != 20) {
            log.error("Server result size unexpected. Size = {}", result.size());
            throw new UnexpectedServerQueryException("Server did not return the expected 20 required games!");
        }

        return new ArrayList<>(result);
    }

    /**
     * Used for GetLatestGamesActivity endpoint.
     * DynamoDB query to retrieve latest games.
     * dateTime is the sort key, sorted in descending order.
     * @param userId the partition key of Scores table.
     * @param amount the total gamesPlayed by user, retrieved from Users table.
     * @return a List of Scores of size() >= 1 && size() <= 5.
     */
    public List<Score> getLatest5Games(String userId, int amount) {

        int max5Amount = amount > 5 ? 5 : amount;
        DynamoDBQueryExpression<Score> queryExpression = new DynamoDBQueryExpression<Score>()
                .withKeyConditionExpression("userId = :v_userId")
                .withExpressionAttributeValues(Map.of(":v_userId", new AttributeValue().withS(userId)))
                .withScanIndexForward(false) // descending order by datetime
                .withLimit(max5Amount);

        PaginatedQueryList<Score> result = dynamoDBMapper.query(Score.class, queryExpression);

        if (result.size() < 1 || result.size() > 5) {
            log.error("Server result size unexpected. Size = {}", result.size());
            throw new UnexpectedServerQueryException("Server did not return the expected amount of games.");
        }
        return new ArrayList<>(result);
    }
}
