import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import praktikum.burger.pojo.Order;
import praktikum.burger.pojo.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest extends BaseTest{

    OrderObj orderObj = new OrderObj();
    UserObj userObj = new UserObj();
    private String bearerToken;
    User user;

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Успешный запрос должен возвращать статус код - 200 и success: true")
    public void createOrderWithAuthorization() {
        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response response = userObj.createUser(user);
        bearerToken = response.jsonPath().getString("accessToken");
        userObj.login(user);


        //Response responseIngredients = orderObj.getIngredientsData();
        List<String> ingredients = orderObj.getIngredientsData().jsonPath().getList("data._id");
        System.out.println(ingredients);
        //System.out.println(Optional.ofNullable(orderObj.getIngredientsData().then().extract().path("data.id")));
        Order order = new Order(ingredients);

        Response responseOrder = orderObj.createOrder(order, bearerToken);
        responseOrder.then().log().all().assertThat().body("success", equalTo(true)).and().statusCode(200);


    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Успешный запрос должен возвращать статус код - 200 и success: true")
    public void createOrderWithoutAuthorization() {

        bearerToken = "";

        //Response responseIngredients = orderObj.getIngredientsData();
        List<String> ingredients = orderObj.getIngredientsData().then().extract().path("data._id");
        //System.out.println(Optional.ofNullable(orderObj.getIngredientsData().then().extract().path("data.id")));
        Order order = new Order(ingredients);

        Response responseOrder = orderObj.createOrder(order, bearerToken);
        responseOrder.then().log().all().assertThat().body("success", equalTo(true)).and().statusCode(200);


    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Если не передать ни один ингредиент, вернётся код ответа 400 Bad Request, success: true и message: Ingredient ids must be provided")
    public void createOrderWithoutIngredients() {

        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response response = userObj.createUser(user);
        bearerToken = response.jsonPath().getString("accessToken");
        userObj.login(user);

        List<String> ingredients = new ArrayList<>();
        Order order = new Order(ingredients);
        //Order order = new Order(java.util.Collections.emptyList());

        Response responseOrder = orderObj.createOrder(order, bearerToken);
        responseOrder.then().log().all().assertThat().statusCode(400).and().body("success", Matchers.is(false)).body("message", Matchers.is("Ingredient ids must be provided"));
        //responseOrder.then().assertThat().body("success", equalTo(false)).body("message", equalTo("Ingredient ids must be provided")).and().statusCode(400);

    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Если в запросе передан невалидный хеш ингредиента, вернётся код ответа 500 Internal Server Error, success: false и message: One or more ids provided are incorrect")
    public void createOrderWithWrongIngredient() {

        String email = RandomStringUtils.random(8) + "@yandex.ru";
        String password = RandomStringUtils.random(8);
        String name = RandomStringUtils.random(8);
        User user = new User(email, password, name);

        Response response = userObj.createUser(user);
        bearerToken = response.jsonPath().getString("accessToken");
        userObj.login(user);


        List<String> ingredients = new ArrayList<>();
        ingredients.add("60d3b41abda9cab0026a733c6");

        Order order = new Order(ingredients);

        Response responseOrder = orderObj.createOrder(order, bearerToken);
        responseOrder.then().log().all().assertThat().statusCode(500);
        //responseOrder.then().assertThat().body("success", equalTo(false)).body("messages", equalTo("One or more ids provided are incorrect")).and().statusCode(500);


    }


}

