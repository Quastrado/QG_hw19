package tests;

import models.registration.*;
import org.junit.jupiter.api.Test;
import tests.testdata.TestDataBookClub;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.registration.RegistrationSpec.*;


public class RegistrationTests extends TestBase {
    TestDataBookClub testData = new TestDataBookClub();

    @Test
    public void successfulRegistrationTest() {

        RegistrationBodyModel registrationBodyModel = new RegistrationBodyModel(
                testData.username,
                testData.password
        );
        SuccessfulRegistrationResponseModel successfulRegistrationResponseModel = given(baseRequestSpec)
                .body(registrationBodyModel)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        String userName = successfulRegistrationResponseModel.username();

        assertThat(userName).isEqualTo(testData.username);
    }

    @Test
    public void existingUserRegistrationTest() {

        RegistrationBodyModel registrationBodyModel = new RegistrationBodyModel(
                testData.username,
                testData.password
        );
        step("User registration request", () -> {
                    given(baseRequestSpec)
                            .body(registrationBodyModel)
                            .when()
                            .post("/users/register/")
                            .then()
                            .spec(successfulRegistrationResponseSpec)
                            .extract()
                            .as(SuccessfulRegistrationResponseModel.class);
        });

        ExistingUserResponseModel secondRegistrationResponse = step("Registration of existing user", () ->
                    given(baseRequestSpec)
                            .body(registrationBodyModel)
                            .when()
                            .post("/users/register/")
                            .then()
                            .spec(existingUserRegistrationResponseSpec)
                            .extract()
                            .as(ExistingUserResponseModel.class)
        );
        step("Checks", () -> {
            String error = secondRegistrationResponse.username().get(0);
            assertThat(error).isEqualTo(testData.existingUserRegistrationError);
        });
    }

    @Test
    public void emptyUsernameRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", testData.password);

        EmptyFieldUsernameResponseModel emptyFieldUsernameResponseModel = step(
                "User registration request with empty username field", () ->
                given(baseRequestSpec)
                    .body(registrationData)
                    .when()
                    .post("/users/register/")
                    .then()
                    .spec(emptyUsernameResponseSpecification)
                    .extract()
                    .as(EmptyFieldUsernameResponseModel.class)
        );
        step("Check", () -> {
            String error = emptyFieldUsernameResponseModel.username().get(0);
            assertThat(error).isEqualTo(testData.notBeBlankError);
        });
    }

    @Test
    public void emptyUsernamePasswordNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel( testData.username, "");

        EmptyFieldPasswordResponseModel emptyFieldUsernameResponseModel = step(
                "User registration request with empty password field", () ->
                given(baseRequestSpec)
                    .body(registrationData)
                    .when()
                    .post("/users/register/")
                    .then()
                    .spec(emptyPasswordResponseSpecification)
                    .extract()
                    .as(EmptyFieldPasswordResponseModel.class)
        );
        step("Check", () -> {
            String error = emptyFieldUsernameResponseModel.password().get(0);
            assertThat(error).isEqualTo(testData.notBeBlankError);
        });
    }
}
