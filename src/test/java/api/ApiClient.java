package api;

/**
 * Общий API-клиент — единая точка доступа к клиентам эндпоинтов.
 */
public class ApiClient {

    public final AuthApiClient auth = new AuthApiClient();
    public final LogoutApiClient logout = new LogoutApiClient();
    public final RegistrationApiClient register = new RegistrationApiClient();
    public final UserUpdateApiClient update = new UserUpdateApiClient();

}