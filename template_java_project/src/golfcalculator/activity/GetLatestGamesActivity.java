package golfcalculator.activity;

import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;

import javax.inject.Inject;

public class GetLatestGamesActivity {

    private UserDao userDao;
    private ScoreDao scoreDao;

    @Inject
    public GetLatestGamesActivity(UserDao userDao, ScoreDao scoreDao) {
        this.userDao = userDao;
        this.scoreDao = scoreDao;
    }
}
