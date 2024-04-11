package golfcalculator.activity;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.converters.ModelConverter;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.dynamodb.models.User;
import golfcalculator.exceptions.EmailAlreadyExistsException;
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
        if (userId == null || email == null) {
            throw new EmailAlreadyExistsException("Email is null!");
        }

        if (!userDao.isUnusedUserId(userId)) {
            throw new UserIdAlreadyExistsException("User Id already exists!");
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
}
