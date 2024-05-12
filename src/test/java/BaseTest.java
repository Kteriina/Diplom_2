import client.UserClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import praktikum.burger.pojo.User;

import java.util.Objects;

public class BaseTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    protected static String bearerToken;
    UserClient userClient = new UserClient();
    User user;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        user = new User(email, password, name);

    }
    @After
    public void tearDown() {
        if ((!Objects.equals(bearerToken, "")) && (bearerToken != null)) {

            Response deletion = userClient.delete(bearerToken);
            deletion.then().log().all().assertThat().statusCode(202).and().body("success", Matchers.is(true)).body("message", Matchers.is("User successfully removed"));


        }

    }



}
