package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutResponseModel;
import models.logout.WrongRefreshTokenResponseModel;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.Test;
import tests.testdata.TestDataBookClub;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.*;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;

public class LogoutTest extends TestBase{
    TestDataBookClub testData = new TestDataBookClub();

    @Test
    public void successfulLogoutTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.username, testData.password);

        step("User registration request", () -> {
                    given(baseRequestSpec)
                            .body(registrationData)
                            .when()
                            .post("/users/register/")
                            .then()
                            .spec(successfulRegistrationResponseSpec);
        });

        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);
        SuccessfulLoginResponseModel responseLogin = step("Authorization request", () ->
                given(baseRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class)
        );

        String refreshToken = responseLogin.refresh();

        step("Logout request", () -> {
            LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
            given(baseRequestSpec)
                    .body(logoutData)
                    .when()
                    .post("/auth/logout/")
                    .then()
                    .spec(successfulLogoutResponseSpec);
        });
    }

    @Test
    public void logoutWithoutRefreshTokenNegativeTest() {

        step("Check error during logout without refresh token", () -> {
            WithoutRefreshTokenLogoutBodyModel logoutData = new WithoutRefreshTokenLogoutBodyModel();
            WithoutRefreshTokenLogoutResponseModel logoutResponse =
                    given(baseRequestSpec)
                            .body(logoutData)
                            .when()
                            .post("/auth/logout/")
                            .then()
                            .spec(withoutRefreshTokenLogoutResponseSpec)
                            .extract().as(WithoutRefreshTokenLogoutResponseModel.class);


            String actualErrorWithoutRefreshToken = logoutResponse.refresh().get(0);
            assertThat(actualErrorWithoutRefreshToken).isEqualTo(testData.requiredFieldError);
        });
    }

    @Test
    public void accessTokenInsteadOfRefreshTokenNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.username, testData.password);
        step("User registration request", () -> {
            given(baseRequestSpec)
                    .body(registrationData)
                    .when()
                    .post("/users/register/")
                    .then()
                    .spec(successfulRegistrationResponseSpec);
        });
        String accessToken = step("Log in request. Get an access token", () -> {
                    LoginBodyModel data = new LoginBodyModel(testData.username, testData.password);
                    return given(baseRequestSpec)
                            .body(data)
                            .when()
                            .post("/auth/token/")
                            .then()
                            .spec(successfulLoginResponseSpec)
                            .extract().path("access");
                });
        LogoutBodyModel logoutData = new LogoutBodyModel(accessToken);
        WrongRefreshTokenResponseModel logoutResponse = step(
                "Logout requst with access token instead refresh token", () ->
                given(baseRequestSpec)
                        .body(logoutData)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(invalidTokenLogoutResponseSpec)
                        .extract().as(WrongRefreshTokenResponseModel.class)
                );
        step("Checks", () -> {
            String token = logoutResponse.detail();
            String tokenCode = logoutResponse.code();
            assertThat(token).isEqualTo(testData.wrongTokenTypeError);
            assertThat(tokenCode).isEqualTo(testData.notValidTokenCodeError);
        });

    }

}
