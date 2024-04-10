package golfcalculator.activity;

import golfcalculator.dynamodb.ScoreDao;
import golfcalculator.dynamodb.UserDao;

import javax.inject.Inject;

public class GetHandicapActivity {

    private UserDao userDao;
    private ScoreDao scoreDao;

    @Inject
    public GetHandicapActivity(UserDao userDao, ScoreDao scoreDao) {
        this.userDao = userDao;
        this.scoreDao = scoreDao;
    }
}
