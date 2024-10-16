package golfcalculator.models.results;

import golfcalculator.models.UserModel;

/**
 * Result from user interaction with CreateUserActivity API endpoint.
 */
public class CreateUserResult {

    private UserModel userModel;
    private String error;
    private String errorMessage;

    public CreateUserResult(Builder builder) {
        this.userModel = builder.userModel;
    }

    public static Builder builder() {return new Builder();}
    public static class Builder {
        private UserModel userModel;

        public Builder withUserModel(UserModel userModelToUse) {
            this.userModel = userModelToUse;
            return this;
        }

        public CreateUserResult build() {return new CreateUserResult(this); }
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
