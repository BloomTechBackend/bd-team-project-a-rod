package golfcalculator.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import golfcalculator.dependency.DaggerServiceComponent;
import golfcalculator.dependency.ServiceComponent;
import golfcalculator.models.requests.GetHandicapRequest;
import golfcalculator.models.results.GetHandicapResult;

public class GetHandicapActivityProvider implements RequestHandler<GetHandicapRequest, GetHandicapResult> {

    private static ServiceComponent serviceComponent;

    @Override
    public GetHandicapResult handleRequest(GetHandicapRequest getHandicapRequest, Context context) {
        return getServiceComponent().provideGetHandicapActivity().handleRequest(getHandicapRequest, context);
    }

    private ServiceComponent getServiceComponent() {
        if (serviceComponent == null) {
            serviceComponent = DaggerServiceComponent.create();
        }
        return serviceComponent;
    }
}
