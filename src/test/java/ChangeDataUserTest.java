import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import praktikum.burger.pojo.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeDataUserTest extends BaseTest{
    UserObj userObj = new UserObj();
    private String bearerToken;

    @Test
    @DisplayName("Изменение данных пользователя c авторизацией")
    @Description("Успешный запрос должен возвращать статус код - 200 и success: true")
    public void changeDataUserWithAuthorization() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response responseCreate = userObj.createUser(user);
        bearerToken = responseCreate.jsonPath().getString("accessToken");

        user.setPassword(RandomStringUtils.random(8));
        user.setEmail(RandomStringUtils.random(8) + "@yandex.ru");
        user.setName(RandomStringUtils.random(8));


        Response responseChange = userObj.changeData(bearerToken, user);
        responseChange.then().assertThat().body("success", equalTo(true)).and().statusCode(200);

    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Если выполнить запрос без авторизации, вернётся код ответа 401 Unauthorized, success: false  и message: You should be authorised")
    public void changeDataUserWithoutAuthorization() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response responseCreate = userObj.createUser(user);
        bearerToken = responseCreate.jsonPath().getString("accessToken");

        user.setPassword(RandomStringUtils.random(8));
        user.setEmail(RandomStringUtils.random(8) + "@yandex.ru");
        user.setName(RandomStringUtils.random(8));


        Response responseChange = userObj.changeData(null, user);
        responseChange.then().assertThat().body("success", equalTo(false)).body("message", equalTo("You should be authorised")).and().statusCode(401);

    }

    @Test
    @DisplayName("Изменение поля email на тоже самое значение")
    @Description("Если передать почту, которая уже используется, вернётся код ответа 403 Forbidden, success: false  и message: User with such email already exists")
    public void changeUserWhenEmailStayTheSame() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response responseCreate = userObj.createUser(user);
        bearerToken = responseCreate.jsonPath().getString("accessToken");

        user.setPassword(RandomStringUtils.random(8));
        user.setEmail(email);
        user.setName(RandomStringUtils.random(8));


        Response responseChange = userObj.changeData(bearerToken, user);
        responseChange.then().log().all().assertThat().body("success", equalTo(false)).body("message", equalTo("User with such email already exists")).and().statusCode(403);

    }

    @After
    public void tearDown() {
        if (bearerToken != null) {

            Response deletion = userObj.delete(bearerToken);
            deletion.then().log().all().assertThat().statusCode(202).and().body("success", Matchers.is(true)).body("message", Matchers.is("User successfully removed"));

        }

    }
}

