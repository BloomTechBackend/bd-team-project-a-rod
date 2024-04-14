package golfcalculator.activity;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.converters.ModelConverter;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.EmailAlreadyExistsException;
import golfcalculator.exceptions.InvalidEmailException;
import golfcalculator.exceptions.InvalidUserNameException;
import golfcalculator.exceptions.UserIdAlreadyExistsException;
import golfcalculator.models.requests.CreateUserRequest;
import golfcalculator.models.results.CreateUserResult;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.inject.Inject;


public class CreateUserActivity implements RequestHandler<CreateUserRequest, CreateUserResult> {

    private final Logger log = LogManager.getLogger(CreateUserActivity.class);
    private final UserDao userDao;

    /**
     * This is the API endpoint for creating new user accounts.
     * @param userDao userDao to access the Users table.
     */
    @Inject
    public CreateUserActivity(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * This method is used to persist a new User object that will be stored on DynamoDB.
     * @param createUserRequest contains UserId and email that user wishes to use.
     * @param context The Lambda execution environment context object.
     * @exception InvalidUserNameException if userId doesn't follow regex pattern.
     * @exception InvalidEmailException if email doesn't follow regex pattern.
     * @exception UserIdAlreadyExistsException if userId already exists in Users table.
     * @exception EmailAlreadyExistsException if email already exists in Users table.
     * @return {@link CreateUserResult} returns a UserModel: userId, email
     */
    @Override
    public CreateUserResult handleRequest(final CreateUserRequest createUserRequest, Context context) {

        log.info("Received CreateUserRequest: {}", createUserRequest);
        String userId = createUserRequest.getId();
        String email = createUserRequest.getEmail();
        if (userId == null || email == null) {
            log.error("Username or email were left null in request: user {}, email {}", userId, email);
            throw new IllegalArgumentException("Username or email were left empty in request.");
        }

        if (!validateUserId(userId)) {
            log.error("InvalidUserNameException: Invalid userId requested {}", userId);
            throw new InvalidUserNameException("Invalid username: Please ensure your username is" +
                    "between 3 and 20 characters long and contains only letters and numbers.");
        }

        if (!validateEmail(email)) {
            log.error("InvalidEmailException: Invalid email requested {}", email);
            throw new InvalidEmailException("Invalid email: Please enter a valid email address with a" +
                    "format like example@domain.com. Ensure it includes a domain name and a top-level" +
                    "domain (like .com, .org, etc).");
        }

        if (!userDao.isUnusedUserId(userId)) {
            log.error("UserIdAlreadyExistsException: UserId already exists {}", userId);
            throw new UserIdAlreadyExistsException("User Id already in use!");
        }

        if (!userDao.isUnusedEmail(email)) {
            log.error("EmailAlreadyExistsException: Email already exists {}", email);
            throw new EmailAlreadyExistsException("Email already in use!");
        }

        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setGamesPlayed(0);
        userDao.saveUser(user);

        log.info("Saved new user: {}", user);

        return CreateUserResult.builder()
                .withUserModel(ModelConverter.toUserModel(user))
                .build();
    }

    /**
     * Regex pattern validator method for alphanumeric string that is 3 to 20 characters long.
     * @param userId the userId in CreateUserRequest.
     * @return true or false, whether regex pattern matches.
     */
    private boolean validateUserId(String userId) {
        String regex = "^[a-zA-Z0-9]{3,20}$";
        return userId.matches(regex);
    }

    /**
     * Regex pattern validator method for proper email following standard rules.
     * @param email email in CreateUserRequest.
     * @return true or false, whether regex pattern matches.
     */
    private boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }
}
