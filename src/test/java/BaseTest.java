import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import praktikum.burger.pojo.User;

public class BaseTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

    }


}
