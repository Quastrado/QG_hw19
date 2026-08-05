package tests;

import models.registration.*;
import org.junit.jupiter.api.Test;
import tests.testdata.TestDataBookClub;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class RegistrationTests extends TestBase {
    TestDataBookClub testData = new TestDataBookClub();

    @Test
    public void successfulRegistrationTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(
                testData.username,
                testData.password
        );
        SuccessfulRegistrationResponseModel registrationResponse = api.register.register(registrationData);
        String userName = registrationResponse.username();
        assertThat(userName).isEqualTo(testData.username);
    }

    @Test
    public void existingUserRegistrationTest() {

        RegistrationBodyModel registrationBodyModel = new RegistrationBodyModel(
                testData.username,
                testData.password
        );
        api.register.register(registrationBodyModel);
        ExistingUserResponseModel secondRegistrationResponse = api.register.registerExistingUser(
                registrationBodyModel
        );
        step("Checks", () -> {
            String error = secondRegistrationResponse.username().get(0);
            assertThat(error).isEqualTo(testData.existingUserRegistrationError);
        });
    }

    @Test
    public void emptyUsernameRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", testData.password);

        EmptyFieldUsernameResponseModel emptyFieldUsernameResponseModel = api.register.registerEmptyFieldUsername(
                registrationData
        );
        step("Check", () -> {
            String error = emptyFieldUsernameResponseModel.username().get(0);
            assertThat(error).isEqualTo(testData.notBeBlankError);
        });
    }

    @Test
    public void emptyUsernamePasswordNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.username, "");

        EmptyFieldPasswordResponseModel emptyFieldUsernameResponseModel = api.register.registerEmptyFieldPassword(
          registrationData
        );
        step("Check", () -> {
            String error = emptyFieldUsernameResponseModel.password().get(0);
            assertThat(error).isEqualTo(testData.notBeBlankError);
        });
    }
}
