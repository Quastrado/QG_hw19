package api;

import io.qameta.allure.Step;
import models.logout.LogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutResponseModel;
import models.logout.WrongRefreshTokenResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.logout.LogoutSpec.*;

public class LogoutApiClient {

    @Step("[API] Logout request POST /auth/logout/")
    public void logout(LogoutBodyModel logoutBody) {
        given(baseRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Step("[API] Check error during logout without refresh token POST /auth/logout/")
    public WithoutRefreshTokenLogoutResponseModel logoutWithoutRefreshToken(LogoutBodyModel logoutBody) {
        return given(baseRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(withoutRefreshTokenLogoutResponseSpec)
                .extract().as(WithoutRefreshTokenLogoutResponseModel.class);
    }

    @Step("[API] Logout request with access token instead refresh token POST /auth/logout/")
    public WrongRefreshTokenResponseModel logoutWrongRefreshToken(LogoutBodyModel logoutBody) {
        return given(baseRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(invalidTokenLogoutResponseSpec)
                .extract().as(WrongRefreshTokenResponseModel.class);
    }
}
