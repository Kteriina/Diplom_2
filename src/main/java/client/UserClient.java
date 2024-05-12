package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import praktikum.burger.pojo.User;

public class UserClient {
    private static final String CREATE_USER = "/api/auth/register/";
    private static final String LOGIN_USER = "/api/auth/login/";
    private static final String CHANGE_USER = "/api/auth/user";
    private static final String DELETE_USER = "/api/auth/user/";

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return RestAssured.given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    @Step("Логин пользователя")
    public Response login(User user) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Изменение данных пользователя")
    public Response changeData(String bearerToken, User user) {
        if (bearerToken != null) {
            return RestAssured.given()
                    .header("Content-type", "application/json")
                    .headers("Authorization", bearerToken)
                    .body(user)
                    .when()
                    .patch(CHANGE_USER);
        }
        else {
            return RestAssured.given()
                    .header("Content-type", "application/json")
                    .body(user)
                    .when()
                    .patch(CHANGE_USER);
        }
    }

    @Step("Удаление пользователя")
    public Response delete(String bearerToken) {
        return RestAssured.given()
                .headers("Authorization", bearerToken)
                .delete(DELETE_USER);
    }

}
