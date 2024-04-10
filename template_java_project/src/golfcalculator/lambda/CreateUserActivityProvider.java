package golfcalculator.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dagger.internal.DaggerCollections;
import golfcalculator.dependency.ServiceComponent;
import golfcalculator.models.requests.CreateUserRequest;
import golfcalculator.models.results.CreateUserResult;

public class CreateUserActivityProvider implements RequestHandler<CreateUserRequest, CreateUserResult> {

    private static ServiceComponent serviceComponent;

    public CreateUserActivityProvider() {
    }

    @Override
    public CreateUserResult handleRequest(CreateUserRequest input, Context context) {
        return null;
    }

    private ServiceComponent getServiceComponent() {

        if (serviceComponent == null) {
            serviceComponent = null;
        }

        return null;
    }
}
