import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeDataUserTest extends BaseTest{

    @Test
    @DisplayName("Изменение данных пользователя c авторизацией")
    @Description("Успешный запрос должен возвращать статус код - 200 и success: true")
    public void changeDataUserWithAuthorization() {

        Response responseCreate = userClient.createUser(user);
        bearerToken = responseCreate.jsonPath().getString("accessToken");

        user.setPassword(RandomStringUtils.random(8));
        user.setEmail(RandomStringUtils.random(8) + "@yandex.ru");
        user.setName(RandomStringUtils.random(8));


        Response responseChange = userClient.changeData(bearerToken, user);
        responseChange.then().assertThat().body("success", equalTo(true)).and().statusCode(200);

    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Если выполнить запрос без авторизации, вернётся код ответа 401 Unauthorized, success: false  и message: You should be authorised")
    public void changeDataUserWithoutAuthorization() {

        Response responseCreate = userClient.createUser(user);
        bearerToken = responseCreate.jsonPath().getString("accessToken");

        user.setPassword(RandomStringUtils.random(8));
        user.setEmail(RandomStringUtils.random(8) + "@yandex.ru");
        user.setName(RandomStringUtils.random(8));


        Response responseChange = userClient.changeData(null, user);
        responseChange.then().assertThat().body("success", equalTo(false)).body("message", equalTo("You should be authorised")).and().statusCode(401);

    }

    @Test
    @DisplayName("Изменение поля email на тоже самое значение")
    @Description("Если передать почту, которая уже используется, вернётся код ответа 403 Forbidden, success: false  и message: User with such email already exists")
    public void changeUserWhenEmailStayTheSame() {


        Response responseCreate = userClient.createUser(user);
        bearerToken = responseCreate.jsonPath().getString("accessToken");

        user.setPassword(RandomStringUtils.random(8));
        user.setName(RandomStringUtils.random(8));


        Response responseChange = userClient.changeData(bearerToken, user);
        responseChange.then().log().all().assertThat().body("success", equalTo(false)).body("message", equalTo("User with such email already exists")).and().statusCode(403);

    }

}

