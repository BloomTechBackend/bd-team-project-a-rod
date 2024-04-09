package golfcalculator.dependency;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DaoModule {

    private AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2")
            .build();
    @Provides
    @Singleton
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB);
    }
}
