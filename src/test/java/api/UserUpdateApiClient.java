package api;

import io.qameta.allure.Step;
import models.user_update.IncorrectPartialUpdateUserResponseModel;
import models.user_update.SuccessfulUpdateUserResponseModel;
import models.user_update.UnauthorizedResponseModel;
import models.user_update.UpdateUserBodyModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.user_update.UpdateUserSpec.*;

public class UserUpdateApiClient {

    @Step("[API] Full update of user data PUT /users/me/")
    public SuccessfulUpdateUserResponseModel updateFull (
            UpdateUserBodyModel updateBody,
            String accessToken
    ) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateBody)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract()
                .as(SuccessfulUpdateUserResponseModel.class);
    }

    @Step("[API] Full update of user data PATCH /users/me/")
    public IncorrectPartialUpdateUserResponseModel updateIncorrectPartial(
            UpdateUserBodyModel updateBody,
            String accessToken
    ) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateBody)
                .when()
                .put("/users/me/")
                .then()
                .spec(unsuccessfulPartialUpdateUserResponseSpec)
                .extract()
                .as(IncorrectPartialUpdateUserResponseModel.class);
    }

    @Step("[API] Full update of user data PATCH /users/me/")
    public UnauthorizedResponseModel updateWithoutRequiredHeader(
            UpdateUserBodyModel updateBody
    ) {
        return given(baseRequestSpec)
                .body(updateBody)
                .when()
                .put("/users/me/")
                .then()
                .spec(unauthorizedResponseSpec)
                .extract()
                .as(UnauthorizedResponseModel.class);

    }
}
