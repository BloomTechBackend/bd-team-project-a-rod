package golfcalculator.models.requests;

import com.amazonaws.services.dynamodbv2.model.Get;

import java.util.Objects;

/**
 * Request for GetHandicapActivity API endpoint.
 */
public class GetHandicapRequest {
    private String userId;

    public GetHandicapRequest() {
    }

    public GetHandicapRequest(Builder builder) {
        this.userId = builder.userId;
    }

    public static Builder builder() {return new Builder();}
    public static class Builder {
        private String userId;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public GetHandicapRequest build() {return new GetHandicapRequest(this);}
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GetHandicapRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetHandicapRequest that = (GetHandicapRequest) o;
        return Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }
}
