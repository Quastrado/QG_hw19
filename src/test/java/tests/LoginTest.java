package tests;

import models.login.*;
import models.logout.WrongRefreshTokenResponseModel;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.Test;
import tests.testdata.TestDataBookClub;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends TestBase {
    TestDataBookClub testData = new TestDataBookClub();

    @Test
    public void successfulLogin() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(
                testData.username,
                testData.password
        );
        api.register.register(registrationData);
        LoginBodyModel loginData = new LoginBodyModel(
                testData.username,
                testData.password
        );
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);
        step("Checks", () -> {
            String access = loginResponse.access();
            String refresh = loginResponse.refresh();
            assertThat(access).startsWith(testData.expectedTokenPart);
            assertThat(refresh).startsWith(testData.expectedTokenPart);
            assertThat(access).isNotEqualTo(refresh);
        });
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel registrationData = new LoginBodyModel(
                testData.username,
                testData.wrongPassword
        );
        WrongCredentialsLoginResponseModel loginResponse = api.auth.loginWrongCredentials(registrationData);
        step("Checks", () -> {
            String error = loginResponse.detail();
            assertThat(error).isEqualTo(testData.wrongCredentialsError);
        });
    }

    @Test
    public void emptyRefreshTokenLoginTest() {
        WithoutRefreshTokenLoginBodyModel emptyRefreshToken = new WithoutRefreshTokenLoginBodyModel();
        WithoutRefreshTokenLoginResponseModel emptyRefreshResponseModel = api.auth.loginEmptyRefreshToken(
                emptyRefreshToken
        );
        step("Checks", () -> {
            String actualRefresh = emptyRefreshResponseModel.refresh().get(0);
            assertThat(actualRefresh).isEqualTo(testData.requiredFieldError);
        });
    }

    @Test
    public void wrongRefreshTokenLoginTest() {
        InvalidRefreshTokenBodyModel invalidTokenBodyModel = new InvalidRefreshTokenBodyModel(
                testData.wrongRefreshToken
        );
        WrongRefreshTokenResponseModel loginResponse = api.auth.loginWrongRefreshToken(invalidTokenBodyModel);
        step("Checks", () -> {
        String detailInvalidRefreshToken = loginResponse.detail();
        String codeInvalidRefreshToken = loginResponse.code();

        assertThat(detailInvalidRefreshToken).isEqualTo(testData.invalidTokenError);
        assertThat(codeInvalidRefreshToken).isEqualTo(testData.notValidTokenCodeError);
        });
    }
}
