package tests;

import dto.ReviewRequests;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReviewTest extends BaseTest {
    private static String reviewId;
    private static String accessToken;

    @BeforeAll
    public static void setupToken() {
        accessToken = new BaseTest().loginAccessToken("MYuser@example.com", "MYuser");
    }

    @Test
    @Order(1)
    public void testCreateReview() {
        ReviewRequests review = ReviewRequests.builder()
                .authorId("07381c49-14f1-4568-a609-09a02db7b0d0")
                .recipientId("268e57f4-92eb-4c3d-bf25-4838368dbb4b")
                .projectId("919bb2b5-8bc0-4f46-8462-98e74c402091")
                .rating(5)
                .text("Great experience working together, highly recommend!")
                .isPublic(true)
                .build();

        Response response = postRequest("/reviews", 200, review, accessToken);

        System.out.println("Create Response: " + response.asString());

        assertEquals(200, response.statusCode());

        reviewId = response.jsonPath().getString("id");
        System.out.println("Review ID after creation: " + reviewId);
        assertNotNull(reviewId, "Review ID is null!");
    }

    @Test
    @Order(2)
    public void testGetReviewById() {
        System.out.println("Fetching Review ID: " + reviewId);
        assertNotNull(reviewId, "Review ID is null, creation might have failed!");

        Response response = getRequest("/reviews/" + reviewId, 200, accessToken);
        System.out.println("Get Response: " + response.asString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @Order(3)
    public void testUpdateReview() {
        System.out.println("Updating Review ID: " + reviewId);
        assertNotNull(reviewId, "Review ID is null, update failed!");

        ReviewRequests updatedReview = ReviewRequests.builder()
                .rating(5)
                .text("Updated review text")
                .isPublic(true)
                .build();
        Response response = patchRequest("/reviews/" + reviewId, updatedReview, 200, accessToken);
        System.out.println("Update Response: " + response.asString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @Order(4)
    public void testDeleteReview() {
        System.out.println("Deleting Review ID: " + reviewId);
        assertNotNull(reviewId, "Review ID is null, delete failed!");
        Response response = deleteRequest("/reviews/" + reviewId, 204, accessToken);
        System.out.println("Delete Response: " + response.statusCode());
        assertEquals(204, response.statusCode());
    }

}
