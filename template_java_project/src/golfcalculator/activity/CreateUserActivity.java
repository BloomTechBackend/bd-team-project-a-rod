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

    private final Logger log = LogManager.getLogger();
    private final UserDao userDao;

    @Inject
    public CreateUserActivity(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public CreateUserResult handleRequest(final CreateUserRequest createUserRequest, Context context) {
        log.info("Received CreateUserRequest {} ", createUserRequest);
        String userId = createUserRequest.getId();
        String email = createUserRequest.getEmail();

        // TODO: Complete verification logic
        // Need more fitting exception
        // Need to implement regex for valid username and emails
        if (!validateUserId(userId)) {
            throw new InvalidUserNameException("Invalid username: Please ensure your username is" +
                    "between 3 and 20 characters long and contains only letters and numbers.");
        }

        if (!validateEmail(email)) {
            throw new InvalidEmailException("Invalid email: Please enter a valid email address with a" +
                    "format like example@domain.com. Ensure it includes a domain name and a top-level" +
                    "domain (like .com, .org, etc).");
        }

        if (!userDao.isUnusedUserId(userId)) {
            throw new UserIdAlreadyExistsException("User Id already in use!");
        }

        if (!userDao.isUnusedEmail(email)) {
            throw new EmailAlreadyExistsException("Email already in use!");
        }

        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setGamesPlayed(0);
        userDao.saveUser(user);

        return CreateUserResult.builder()
                .withUserModel(ModelConverter.toUserModel(user))
                .build();
    }

    private boolean validateUserId(String userId) {
        // Regex for alphanumeric string that is 3 to 20 characters long
        String regex = "^[a-zA-Z0-9]{3,20}$";

        return userId.matches(regex);
    }

    private boolean validateEmail(String email) {

        // allowed characters at least 1, now we need optional dot, but it must be followed by allowed characters
        // @
        // allowed characters first followed by optional dot at least once
        // finally end with 2-7 allowed characters after last dot if it exists.
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }
}
