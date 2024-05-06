import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import praktikum.burger.pojo.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;

public class GetUserOrdersTest extends BaseTest{
    UserObj userObj = new UserObj();
    String bearerToken;
    OrderObj orderObj = new OrderObj();

    @Test
    @DisplayName("Получение заказов конкретного авторизованного пользователя")
    @Description("Успешный запрос должен возвращать статус код - 200, success: true и orders: [...]")
    public void getOrdersOfAuthorizedUser() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response response = userObj.createUser(user);
        bearerToken = response.jsonPath().getString("accessToken");
        userObj.login(user);

        Response responseOrder = orderObj.getUserOrders(bearerToken);
        responseOrder.then().log().all().assertThat().body("success", equalTo(true)).and().statusCode(200);



    }

    @Test
    @DisplayName("Получение заказов конкретного неавторизованного пользователя")
    @Description("УЕсли выполнить запрос без авторизации, вернётся код ответа 401 Unauthorized, success: false и message: You should be authorised")
    public void getOrdersOfNonAuthorizedUser() {

        bearerToken = "";

        Response responseOrder = orderObj.getUserOrders(bearerToken);
        responseOrder.then().log().all().assertThat().body("success", equalTo(false)).body("message", equalTo("You should be authorised")).and().statusCode(401);


    }

}
