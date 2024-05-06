import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import praktikum.burger.pojo.Order;
public class OrderObj {
    private static final String CREATE_ORDER = "/api/orders/";
    private static final String GET_USER_ORDERS = "/api/orders/";
    private static final String GET_INGREDIENTS_DATA = "/api/ingredients/";


    @Step("Создание заказа")
    public Response createOrder(Order order, String bearerToken) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .headers("Authorization", bearerToken)
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }
    @Step("Получение заказов конкретного пользователя")
    public Response getUserOrders(String bearerToken) {
        return RestAssured.given()
                .headers("Authorization", bearerToken)
                .get(GET_USER_ORDERS);
    }
    @Step("Получение данных об ингредиентах")
    public Response getIngredientsData() {
        return RestAssured.given()
                .get(GET_INGREDIENTS_DATA);
    }
}

