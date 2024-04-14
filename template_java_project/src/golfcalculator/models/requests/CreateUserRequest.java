package golfcalculator.models.requests;


import java.util.Objects;

/**
 * Request for CreateUserActivity API endpoint.
 */
public class CreateUserRequest {
    private String userId;
    private String email;

    public CreateUserRequest() {
    }

    public CreateUserRequest(Builder builder) {
        this.userId = builder.id;
        this.email = builder.email;
    }

    public static Builder builder() {return new Builder();}
    public static final class Builder {
        private String id;
        private String email;

        public Builder withUserId(String idToUse) {
            this.id = idToUse;
            return this;
        }

        public Builder withEmail(String emailToUse) {
            this.email = emailToUse;
            return this;
        }

        public CreateUserRequest build() {
            return new CreateUserRequest(this);
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserRequest that = (CreateUserRequest) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail());
    }
}
