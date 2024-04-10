package golfcalculator.activity;

import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;

import javax.inject.Inject;

public class CreateNewScoreActivity {

    private UserDao userDao;
    private ScoreDao scoreDao;

    @Inject
    public CreateNewScoreActivity(UserDao userDao, ScoreDao scoreDao) {
        this.userDao = userDao;
        this.scoreDao = scoreDao;
    }

//    public ActivityNameResult handleRequest(ActivityNameRequest request, Context context) {
//        // Capture the current date and time as close to the action as possible
//        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//
//        // Now, you might either set this dateTime in the request object,
//        // Or directly use it in the creation of an object that will be stored in DynamoDB
//        request.setDateTime(dateTime); // Assuming your request object has a dateTime field
//
//        // Process the request...
//
//        // For example, create a new Score entry to store in DynamoDB
//        Score newScore = new Score(request.getScore(), dateTime);
//        // Code to save newScore to DynamoDB...
//
//        // Generate and return the result
//        return new ActivityNameResult(/* parameters */);
//    }
}
