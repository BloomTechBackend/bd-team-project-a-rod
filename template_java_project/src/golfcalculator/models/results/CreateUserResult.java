package golfcalculator.models.results;

import golfcalculator.models.UserModel;

public class CreateUserResult {

    private UserModel userModel;

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
}
