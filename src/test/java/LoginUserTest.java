import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import praktikum.burger.pojo.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest extends BaseTest{
    UserObj userObj = new UserObj();
    private String bearerToken;

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Успешный запрос должен возвращать статус код - 200")
    public void loginUser() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response responseCreate = userObj.createUser(user);
        bearerToken = responseCreate.jsonPath().getString("accessToken");


        Response responseLogin = userObj.login(user);
        responseLogin.then().assertThat().body("success", equalTo(true)).and().statusCode(200);

    }

    @Test
    @DisplayName("Логин с неверным полем email")
    @Description("Если логин или пароль неверные или нет одного из полей, вернётся код ответа 401 Unauthorized, success: false  и message: email or password are incorrect")
    public void loginUserWithWrongEmail() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response responseCreate = userObj.createUser(user);
        bearerToken = responseCreate.jsonPath().getString("accessToken");
        user.setEmail(RandomStringUtils.random(8));

        Response responseLogin = userObj.login(user);
        responseLogin.then().assertThat().body("success", equalTo(false)).body("message", equalTo("email or password are incorrect")).and().statusCode(401);

    }
    @Test
    @DisplayName("Логин с неверным полем password")
    @Description("Если логин или пароль неверные или нет одного из полей, вернётся код ответа 401 Unauthorized, success: false  и message: email or password are incorrect")
    public void loginUserWithWrongPassword() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response responseCreate = userObj.createUser(user);
        bearerToken = responseCreate.jsonPath().getString("accessToken");
        user.setPassword(RandomStringUtils.random(8));

        Response responseLogin = userObj.login(user);
        responseLogin.then().assertThat().body("success", equalTo(false)).body("message", equalTo("email or password are incorrect")).and().statusCode(401);

    }

    @After
    public void tearDown() {
        if (bearerToken != null) {

            Response deletion = userObj.delete(bearerToken);
            deletion.then().log().all().assertThat().statusCode(202).and().body("success", Matchers.is(true)).body("message", Matchers.is("User successfully removed"));

        }

    }


}
