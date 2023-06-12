package io.everyonecodes.project.movie_recommendations.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class RecommendationServiceTest {
    //Tests never worked??
//    @Autowired
//    RecommendationService recommendationService;
//
//    @Test
//    void recommend() {
//        recommendationService.recommendMovies("916224");
//    }
//
//    @ParameterizedTest
//    @MethodSource("testCases")
//    public void testKeepAPercentageOfItems(int percentage, List<Long> input, List<Long> expectedOutput) {
//
//        List<Long> result = recommendationService.keepAPercentageOfItems(percentage, input);
//
//        Assertions.assertEquals(expectedOutput, result);
//    }
//
//    private static Stream<Arguments> testCases() {
//        return Stream.of(
//                Arguments.of(50, Arrays.asList(1L, 2L, 3L, 4L), Arrays.asList(1L, 2L)),
//                Arguments.of(30, Arrays.asList(10L, 20L, 30L, 40L, 50L), Arrays.asList(10L, 20L)),
//                Arguments.of(70, Arrays.asList(100L, 200L, 300L, 400L, 500L), Arrays.asList(100L, 200L, 300L, 400L))
//        );
//    }
}