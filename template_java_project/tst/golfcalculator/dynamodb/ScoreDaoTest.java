package golfcalculator.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import golfcalculator.dynamodb.models.Score;
import golfcalculator.exceptions.UnexpectedServerQueryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScoreDaoTest {

    @Mock
    private DynamoDBMapper mockDynamoDBMapper;

    @Mock
    private PaginatedQueryList<Score> mockPaginatedQueryList;

    @InjectMocks
    private ScoreDao mockScoreDao;

    private String validId = "validId";

    @BeforeEach
    private void setUp() {
        initMocks(this);
    }

    @Test
    void getLast20Games_serverReturnsExpectedResult() {

        int expectedHandicapGamesCount = 20;
        List<Score> expectedHandicapScores = new ArrayList<>();
        for (int i = 0; i < expectedHandicapGamesCount; i++) {
            expectedHandicapScores.add(new Score());
        }

        when(mockDynamoDBMapper.query(eq(Score.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mockPaginatedQueryList);
        when(mockPaginatedQueryList.size()).thenReturn(expectedHandicapScores.size());
        when(mockPaginatedQueryList.toArray()).thenReturn(expectedHandicapScores.toArray(new Score[0]));

        List<Score> result = mockScoreDao.getLast20Games(validId);

        verify(mockDynamoDBMapper).query(eq(Score.class), any(DynamoDBQueryExpression.class));
        verify(mockPaginatedQueryList).size();
        verify(mockPaginatedQueryList).toArray();
        assertEquals(expectedHandicapGamesCount, result.size());
    }

    @Test
    void getLast20Games_serverReturnsUnexpectedAmount_throwsUnexpectedServerQueryException() {
        int unexpectedHandicapGamesCount = 19;
        List<Score> unexpectedHandicapScores = new ArrayList<>();
        for (int i = 0; i < unexpectedHandicapGamesCount; i++) {
            unexpectedHandicapScores.add(new Score());
        }

        when(mockDynamoDBMapper.query(eq(Score.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mockPaginatedQueryList);
        when(mockPaginatedQueryList.size()).thenReturn(unexpectedHandicapScores.size());
        when(mockPaginatedQueryList.toArray()).thenReturn(unexpectedHandicapScores.toArray());
        
        assertThrows(UnexpectedServerQueryException.class, () -> {
            List<Score> result = mockScoreDao.getLast20Games(validId);
        });
    }

    @Test
    void getLatest5Games_serverReturnsExpectedResult() {
        List<Score> expectedLatestGames = new ArrayList<>();
        int expectedSize = 5;
        int testInputAboveMax = 10;
        for (int i = 0; i < expectedSize; i++) {
            expectedLatestGames.add(new Score());
        }

        when(mockDynamoDBMapper.query(eq(Score.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mockPaginatedQueryList);
        when(mockPaginatedQueryList.size()).thenReturn(expectedLatestGames.size());
        when(mockPaginatedQueryList.toArray()).thenReturn(expectedLatestGames.toArray());

        List<Score> result = mockScoreDao.getLatest5Games(validId, testInputAboveMax);

        verify(mockDynamoDBMapper).query(eq(Score.class), any(DynamoDBQueryExpression.class));
        verify(mockPaginatedQueryList, times(2)).size();
        verify(mockPaginatedQueryList).toArray();
    }

    @Test
    void getLatest5Games_serverReturnsUnexpectedResult_throwsUnexpectedServerQueryException() {
        List<Score> unexpectedLatestGames = new ArrayList<>();
        int unexpectedSize = 6;
        for (int i = 0; i < unexpectedSize; i++) {
            unexpectedLatestGames.add(new Score());
        }

        when(mockDynamoDBMapper.query(eq(Score.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mockPaginatedQueryList);
        when(mockPaginatedQueryList.size()).thenReturn(unexpectedSize);

        assertThrows(UnexpectedServerQueryException.class, () -> {
            List<Score> result = mockScoreDao.getLatest5Games(validId, unexpectedSize);
        });
    }
}
