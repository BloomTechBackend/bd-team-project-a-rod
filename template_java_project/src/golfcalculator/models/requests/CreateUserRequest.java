package golfcalculator.models.requests;

import com.amazonaws.services.dynamodbv2.xspec.S;

import java.util.Objects;

public class CreateUserRequest {
    private String id;
    private String email;

    public CreateUserRequest() {
    }

    public CreateUserRequest(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
    }

    public static Builder builder() {return new Builder();}
    public static final class Builder {
        private String id;
        private String email;

        public Builder withId(String idToUse) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserRequest that = (CreateUserRequest) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail());
    }
}
