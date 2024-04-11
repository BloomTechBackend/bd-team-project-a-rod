package golfcalculator.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.dependency.DaggerServiceComponent;
import golfcalculator.dependency.ServiceComponent;
import golfcalculator.models.requests.GetLatestGamesRequest;
import golfcalculator.models.results.GetLatestGamesResult;

public class GetLatestGamesActivityProvider implements RequestHandler<GetLatestGamesRequest, GetLatestGamesResult> {

    private static ServiceComponent serviceComponent;

    @Override
    public GetLatestGamesResult handleRequest(GetLatestGamesRequest getLatestGamesRequest, Context context) {
        return getServiceComponent().provideGetLatestGamesActivity().handleRequest(getLatestGamesRequest, context);
    }

    private ServiceComponent getServiceComponent() {
        if (serviceComponent == null) {
            serviceComponent = DaggerServiceComponent.create();
        }
        return serviceComponent;
    }
}
