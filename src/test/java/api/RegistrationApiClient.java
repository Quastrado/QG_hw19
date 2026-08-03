package api;

import io.qameta.allure.Step;
import models.registration.*;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.registration.RegistrationSpec.*;

public class RegistrationApiClient {

    @Step("[API] Successful registration request POST /users/register/")
    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel registerBody) {
        return given(baseRequestSpec)
                .body(registerBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    @Step("[API] Registration of existing user POST /users/register/")
    public ExistingUserResponseModel registerExistingUser(RegistrationBodyModel registerBody) {
        return given(baseRequestSpec)
                .body(registerBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);
    }

    @Step("[API] User registration request with empty username field POST /users/register/")
    public EmptyFieldUsernameResponseModel registerEmptyFieldUsername(RegistrationBodyModel registerBody) {
        return given(baseRequestSpec)
                .body(registerBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(emptyUsernameResponseSpecification)
                .extract()
                .as(EmptyFieldUsernameResponseModel.class);
    }

    @Step("[API] User registration request with empty password field POST /users/register/")
    public EmptyFieldPasswordResponseModel registerEmptyFieldPassword(RegistrationBodyModel registerBody) {
        return given(baseRequestSpec)
                .body(registerBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(emptyPasswordResponseSpecification)
                .extract()
                .as(EmptyFieldPasswordResponseModel.class);
    }

}
