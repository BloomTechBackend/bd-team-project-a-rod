package golfcalculator.models;

import java.util.Objects;

/**
 * Model of User for data exposure to user.
 */
public class UserModel {
    private String userId;
    private String email;

    public UserModel() {
    }

    public UserModel(Builder builder) {
        this.userId = builder.userId;
        this.email = builder.email;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String userId;
        private String email;

        public Builder withUserId(String idToUse) {
            this.userId = idToUse;
            return this;
        }

        public Builder withEmail(String emailToUse) {
            this.email = emailToUse;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
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
        return "User ID: " + userId +
                ", Email: " + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(getUserId(), userModel.getUserId()) && Objects.equals(getEmail(), userModel.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail());
    }
}
