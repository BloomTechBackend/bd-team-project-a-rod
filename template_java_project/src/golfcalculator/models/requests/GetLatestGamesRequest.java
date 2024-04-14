package golfcalculator.models.requests;

import java.util.Objects;

/**
 * Request for GetLatestGamesActivity API endpoint.
 */
public class GetLatestGamesRequest {
    private String userId;

    public GetLatestGamesRequest(Builder builder) {
        this.userId = builder.userId;
    }

    public static Builder builder() {return new Builder();}
    public static class Builder {
        private String userId;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public GetLatestGamesRequest build() {return new GetLatestGamesRequest(this);}
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GetLatestGamesRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetLatestGamesRequest that = (GetLatestGamesRequest) o;
        return Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }
}
