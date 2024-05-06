import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import praktikum.burger.pojo.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest extends BaseTest{
    UserObj userObj = new UserObj();
    private String bearerToken;

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Успешный запрос должен возвращать статус код - 200 и success: true")
    public void createUserWithAllData() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response response = userObj.createUser(user);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
        bearerToken = response.jsonPath().getString("accessToken");


    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Если пользователь существует должен возвращаться статус код - 403 Forbidden, success: false  и message: User already exists")
    public void createExistingUser() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response response = userObj.createUser(user);
        bearerToken = response.jsonPath().getString("accessToken");

        Response response_2 = userObj.createUser(user);
        response_2.then().assertThat().body("success", equalTo(false)).body("message", equalTo("User already exists")).and().statusCode(403);



    }
    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Если нет одного из полей вернется статус код - 403 Forbidden, success: false  и message: Email, password and name are required fields")
    public void createUserWithoutEmail() {
        String email = "";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response response = userObj.createUser(user);
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields")).and().statusCode(403);
        bearerToken = response.jsonPath().getString("accessToken");


    }
    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Если нет одного из полей вернется статус код - 403 Forbidden, success: false  и message: Email, password and name are required fields")
    public void createUserWithoutName() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = "";
        User user = new User(email, password, name);

        Response response = userObj.createUser(user);
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields")).and().statusCode(403);
        bearerToken = response.jsonPath().getString("accessToken");


    }
    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Если нет одного из полей вернется статус код - 403 Forbidden, success: false  и message: Email, password and name are required fields")
    public void createUserWithoutPassword() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = "";
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response response = userObj.createUser(user);
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields")).and().statusCode(403);
        bearerToken = response.jsonPath().getString("accessToken");


    }

    @After
    public void tearDown() {
        if (bearerToken != null) {

            Response deletion = userObj.delete(bearerToken);
            deletion.then().log().all().assertThat().statusCode(202).and().body("success", Matchers.is(true)).body("message", Matchers.is("User successfully removed"));

        }

    }
}
