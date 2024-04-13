package golfcalculator.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.UserNotFoundException;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

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
     * Returns whether UserId is unused, making it good to make a new account.
     * Checks Users table in DynamoDB.
     * @param id the User ID, and partition key in Users table.
     * @return true/false
     */
    public boolean isUnusedUserId(String id) {
        User user = loadUser(id);

        if (user != null) {
            return false;
        }
        return true;
    }

    /**
     * Returns whether email is unused, making it good for new account registration.
     * Queries Users table's EmailIndex GSI.
     * @param email email for new user, EmailIndex GSI partition key of Users table.
     * @return true/false
     */
    public boolean isUnusedEmail(String email) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":email", new AttributeValue().withS(email));

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withIndexName("EmailIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("email = :email")
                .withExpressionAttributeValues(eav);

        PaginatedQueryList<User> result = dynamoDBMapper.query(User.class, queryExpression);

        return result.isEmpty();
    }

    /**
     * Retrieves user from Users table.
     * @param userId userId is the partition key for Users table.
     * @exception UserNotFoundException if user account is not found.
     * @return {@link User} object instance
     */
    public User getUser(String userId) {
        User user = loadUser(userId);
        if (user == null) {
            throw new UserNotFoundException("User Account not found!");
        }

        return user;
    }

    /**
     * Saves or updates user to Users table.
     * @param user user to be saved or updated.
     */
    public void saveUser(User user) {
        dynamoDBMapper.save(user);
    }

    /**
     * Helper function to retrieve users from Users table.
     * @param id partition key for Users table.
     * @return requested {@link User} , could be null if user doesn't exist.
     */
    private User loadUser(String id) {
        return this.dynamoDBMapper.load(User.class, id);
    }
}
