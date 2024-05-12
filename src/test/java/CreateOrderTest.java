import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;
import praktikum.burger.pojo.Order;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest extends BaseTest{

    OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Успешный запрос должен возвращать статус код - 200 и success: true")
    public void createOrderWithAuthorization() {

        Response response = userClient.createUser(user);
        bearerToken = response.jsonPath().getString("accessToken");
        userClient.login(user);


        List<String> ingredients = orderClient.getIngredientsData().jsonPath().getList("data._id");
        System.out.println(ingredients);

        Order order = new Order(ingredients);

        Response responseOrder = orderClient.createOrder(order, bearerToken);
        responseOrder.then().log().all().assertThat().body("success", equalTo(true)).and().statusCode(200);


    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Успешный запрос должен возвращать статус код - 200 и success: true")
    public void createOrderWithoutAuthorization() {

        bearerToken = "";

        List<String> ingredients = orderClient.getIngredientsData().then().extract().path("data._id");

        Order order = new Order(ingredients);

        Response responseOrder = orderClient.createOrder(order, bearerToken);
        responseOrder.then().log().all().assertThat().body("success", equalTo(true)).and().statusCode(200);


    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Если не передать ни один ингредиент, вернётся код ответа 400 Bad Request, success: true и message: Ingredient ids must be provided")
    public void createOrderWithoutIngredients() {



        Response response = userClient.createUser(user);
        bearerToken = response.jsonPath().getString("accessToken");
        userClient.login(user);

        List<String> ingredients = new ArrayList<>();
        Order order = new Order(ingredients);

        Response responseOrder = orderClient.createOrder(order, bearerToken);
        responseOrder.then().log().all().assertThat().statusCode(400).and().body("success", Matchers.is(false)).body("message", Matchers.is("Ingredient ids must be provided"));


    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Если в запросе передан невалидный хеш ингредиента, вернётся код ответа 500 Internal Server Error, success: false и message: One or more ids provided are incorrect")
    public void createOrderWithWrongIngredient() {

        Response response = userClient.createUser(user);
        bearerToken = response.jsonPath().getString("accessToken");
        userClient.login(user);


        List<String> ingredients = new ArrayList<>();
        ingredients.add("60d3b41abda9cab0026a733c6");

        Order order = new Order(ingredients);

        Response responseOrder = orderClient.createOrder(order, bearerToken);
        responseOrder.then().log().all().assertThat().statusCode(500);



    }




}

