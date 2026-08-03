package api;

import io.qameta.allure.Step;
import models.login.*;
import models.logout.WrongRefreshTokenResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.login.LoginSpec.*;

public class AuthApiClient {

    @Step("[API] Authorization request POST /auth/token/")
    public SuccessfulLoginResponseModel login(LoginBodyModel loginBody) {
        return given(baseRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);
    }

    @Step("[API] Authorization with an incorrect username POST /auth/token/")
    public WrongCredentialsLoginResponseModel loginWrongCredentials(LoginBodyModel loginBody) {
        return given(baseRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract().as(WrongCredentialsLoginResponseModel.class);
    }

    @Step("[API] Sending request with empty refresh parameter POST /auth/token/")
    public WithoutRefreshTokenLoginResponseModel loginEmptyRefreshToken(LoginBodyModel loginBody) {
        return given(baseRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(withoutRefreshTokenResponseSpec)
                .extract().as(WithoutRefreshTokenLoginResponseModel.class);
    }

    @Step("[API] Sending request with wrong refresh parameter POST /auth/token/")
    public WrongRefreshTokenResponseModel loginWrongRefreshToken(LoginBodyModel loginBody) {
        return given(baseRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(wrongRefreshTokenResponseSpec)
                .extract().as(WrongRefreshTokenResponseModel.class);
    }

}
