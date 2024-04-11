package golfcalculator.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.UserIdAlreadyExistsException;
import golfcalculator.exceptions.UserNotFoundException;

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
     * Returns whether UserId is unused, meaning UserId can be used to create new User.
     * @param id the User ID.
     */
    public boolean isUnusedUserId(String id) {
        User user = loadUser(id);

        if (user != null) {
            return false;
        }
        return true;
    }

    public User getUser(String userId) {
        User user = loadUser(userId);
        if (user == null) {
            throw new UserNotFoundException("User Account not found!");
        }

        return user;
    }

    public void saveUser(User user) {
        dynamoDBMapper.save(user);
    }

    private User loadUser(String id) {
        return this.dynamoDBMapper.load(User.class, id);
    }
}
