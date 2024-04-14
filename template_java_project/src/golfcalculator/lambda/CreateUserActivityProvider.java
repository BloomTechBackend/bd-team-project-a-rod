package golfcalculator.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dagger.internal.DaggerCollections;
import golfcalculator.dependency.DaggerServiceComponent;
import golfcalculator.dependency.ServiceComponent;
import golfcalculator.models.requests.CreateUserRequest;
import golfcalculator.models.results.CreateUserResult;

/**
 * API endpoint for CreateUserActivity
 */
public class CreateUserActivityProvider implements RequestHandler<CreateUserRequest, CreateUserResult> {

    private static ServiceComponent serviceComponent;

    public CreateUserActivityProvider() {
    }

    @Override
    public CreateUserResult handleRequest(CreateUserRequest createUserRequest, Context context) {
        return getServiceComponent().provideCreateUserActivity().handleRequest(createUserRequest, context);
    }

    private ServiceComponent getServiceComponent() {
        if (serviceComponent == null) {
            serviceComponent = DaggerServiceComponent.create();
        }
        return serviceComponent;
    }
}
