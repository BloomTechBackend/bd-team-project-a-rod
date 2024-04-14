package golfcalculator.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.dependency.DaggerServiceComponent;
import golfcalculator.dependency.ServiceComponent;
import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;
import golfcalculator.models.requests.CreateNewScoreRequest;
import golfcalculator.models.results.CreateNewScoreResult;

/**
 * API endpoint for CreateNewScoreActivity
 */
public class CreateNewScoreActivityProvider implements RequestHandler<CreateNewScoreRequest, CreateNewScoreResult> {

    private static ServiceComponent serviceComponent;

    public CreateNewScoreActivityProvider() {
    }
    @Override
    public CreateNewScoreResult handleRequest(CreateNewScoreRequest createNewScoreRequest, Context context) {
        return getServiceComponent().provideCreateNewScoreActivity().handleRequest(createNewScoreRequest, context);
    }

    private ServiceComponent getServiceComponent() {
        if (serviceComponent == null) {
            serviceComponent = DaggerServiceComponent.create();
        }
        return serviceComponent;
    }
}
