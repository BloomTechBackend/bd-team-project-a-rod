package golfcalculator.converters;

import golfcalculator.dynamodb.models.User;
import golfcalculator.models.UserModel;

public class ModelConverter {

    /**
     * Convert User to UserModel
     * @param user the object to be converted
     * @return UserModel object
     */
    public static UserModel toUserModel(User user) {
        return UserModel.builder()
                .withUserId(user.getUserId())
                .withEmail(user.getEmail())
                .build();
    }
}
