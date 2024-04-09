package golfcalculator.dependency;

import dagger.Component;
import golfcalculator.activity.CreateNewScoreActivity;
import golfcalculator.activity.CreateUserActivity;
import golfcalculator.activity.GetHandicapActivity;
import golfcalculator.activity.GetLatestGamesActivity;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DaoModule.class})
public interface ServiceComponent {
    CreateNewScoreActivity provideCreateNewScoreActivity();
    CreateUserActivity provideCreateUserActivity();
    GetHandicapActivity provideGetHandicapActivity();
    GetLatestGamesActivity provideGetLatestGamesActivity();
}
