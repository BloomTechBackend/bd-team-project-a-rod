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

        try {
            userDao.isValidUserId(createUserRequest.getId());
        } catch (UserIdAlreadyExistsException ex) {
            throw new UserIdAlreadyExistsException("User Id already exists!");
        }

        // TODO: Fix this, we need better logic to check if valid email maybe GSI thing
        if (createUserRequest.getEmail() == null) {
            throw new EmailAlreadyExistsException("Email is null!");
        }

        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        userDao.saveUser(user);

        return CreateUserResult.builder()
                .withUserModel(ModelConverter.toUserModel(user))
                .build();
    }
}
