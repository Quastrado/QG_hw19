package tests;

import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.user_update.SuccessfulUpdateUserResponseModel;
import models.user_update.*;
import org.junit.jupiter.api.Test;
import tests.testdata.TestDataBookClub;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UpdateUserTest extends TestBase{
    TestDataBookClub testData = new TestDataBookClub();

    @Test
    public void successfulUpdateUserWithPutTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(
                testData.username,
                testData.password
        );
        api.register.register(registrationData);
        LoginBodyModel dataLogin = new LoginBodyModel(testData.username, testData.password);
        String accessToken = api.auth.loginAndGetRefreshToken(dataLogin);
        UpdateUserBodyModel dataUpdateUser = new UpdateUserBodyModel(
                testData.username,
                testData.firstName,
                testData.lastName,
                testData.email
        );
        SuccessfulUpdateUserResponseModel responseUpdateUser = api.update.updatePut(
                dataUpdateUser,
                accessToken
                );
        step("Checks", () -> {
            String actualUsername = responseUpdateUser.username();
            String actualFirstName = responseUpdateUser.firstName();
            String actualLastName = responseUpdateUser.lastName();
            String actualEmail = responseUpdateUser.email();

            assertThat(responseUpdateUser.id()).isPositive();
            assertThat(actualUsername).isEqualTo(testData.username);
            assertThat(actualFirstName).isEqualTo(testData.firstName);
            assertThat(actualLastName).isEqualTo(testData.lastName);
            assertThat(actualEmail).isEqualTo(testData.email);
            assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
        });
    }

    @Test
    public void successfulUpdateUserWithPatchTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(
                testData.username,
                testData.password
        );
        api.register.register(registrationData);
        LoginBodyModel dataLogin = new LoginBodyModel(testData.username, testData.password);
        String accessToken = api.auth.loginAndGetRefreshToken(dataLogin);
        UpdateUserBodyModel dataUpdateUser = new UpdateUserBodyModel(
                testData.username,
                testData.firstName,
                testData.lastName,
                testData.email
        );
        SuccessfulUpdateUserResponseModel responseUpdateUser = api.update.updatePatchFull(
                dataUpdateUser,
                accessToken
        );
        step("Checks", () -> {
            String actualUsername = responseUpdateUser.username();
            String actualFirstName = responseUpdateUser.firstName();
            String actualLastName = responseUpdateUser.lastName();
            String actualEmail = responseUpdateUser.email();

            assertThat(responseUpdateUser.id()).isPositive();
            assertThat(actualUsername).isEqualTo(testData.username);
            assertThat(actualFirstName).isEqualTo(testData.firstName);
            assertThat(actualLastName).isEqualTo(testData.lastName);
            assertThat(actualEmail).isEqualTo(testData.email);
            assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
        });
    }

    @Test
    public void successfulPartialUpdateUserWithPatchTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(
                testData.username,
                testData.password
        );
        api.register.register(registrationData);
        LoginBodyModel dataLogin = new LoginBodyModel(testData.username, testData.password);
        String accessToken = api.auth.loginAndGetRefreshToken(dataLogin);
        PartialUpdateUserBodyModel dataUpdateUser =
                new PartialUpdateUserBodyModel(testData.username, testData.email);
        SuccessfulUpdateUserResponseModel responseUpdateUser = api.update.updatePatchPartial(
                dataUpdateUser,
                accessToken
        );
        step("Checks", () -> {
            String actualUsername = responseUpdateUser.username();
            String actualEmail = responseUpdateUser.email();

            assertThat(responseUpdateUser.id()).isPositive();
            assertThat(actualUsername).isEqualTo(testData.username);
            assertThat(actualEmail).isEqualTo(testData.email);
            assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
        });
    }

    @Test
    public void partialUpdateUserWithPutNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(
                testData.username,
                testData.password
        );
        api.register.register(registrationData);
        LoginBodyModel dataLogin = new LoginBodyModel(testData.username, testData.password);
        String accessToken = api.auth.loginAndGetRefreshToken(dataLogin);
        PartialUpdateUserBodyModel dataUpdateUser =
                new PartialUpdateUserBodyModel(testData.username, testData.email);
        IncorrectPartialUpdateUserResponseModel responseUpdateUser = api.update.updateIncorrectPartial(
                dataUpdateUser,
                accessToken
        );
        step("Checks", () -> {
            String actualFirstName = responseUpdateUser.firstName().get(0);
            String actualLastName = responseUpdateUser.lastName().get(0);
            assertThat(actualFirstName).isEqualTo(testData.requiredFieldError);
            assertThat(actualLastName).isEqualTo(testData.requiredFieldError);
            assertThat(responseUpdateUser.username()).isNull();
            assertThat(responseUpdateUser.email()).isNull();
        });
    }

    @Test
    public void withoutRequiredAuthorizationHeaderUpdateUserNegativeTest() {
        UpdateUserBodyModel dataUpdateUser = new UpdateUserBodyModel(
                testData.username,
                testData.firstName,
                testData.lastName,
                testData.email
        );
        UnauthorizedResponseModel responseUpdateUser = api.update.updateWithoutRequiredHeader(dataUpdateUser);
        step("Checks", () -> {
            String actualDetail = responseUpdateUser.detail();
            assertThat(actualDetail).isEqualTo(testData.unauthorizedError);
        });
    }
}
