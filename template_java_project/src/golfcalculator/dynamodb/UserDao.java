package golfcalculator.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.UserIdAlreadyExistsException;

import javax.inject.Inject;

public class UserDao {

    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Instantiates a UserDao object.
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the Users table
     */
    @Inject
    public UserDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Returns whether UserId is valid, meaning UserId is not in use.
     * @param id the User ID.
     */
    public void isValidUserId(String id) {
        User user = loadUser(id);

        if (user != null) {
            throw new UserIdAlreadyExistsException("User ID already exists!");
        }
    }

    public void saveUser(User user) {
        dynamoDBMapper.save(user);
    }

    private User loadUser(String id) {
        return this.dynamoDBMapper.load(User.class, id);
    }
}
