package tds.user.api.tests;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.endsWith;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;
import tds.user.api.model.UserInfo;
import tds.user.api.model.RoleAssociation;

/*
 * Authenticate user
 * Testing HTTP POST of https://sso-deployment.sbtds.org/auth/oauth2/access_token?realm=/sbac
 * and success status 200
 */
<<<<<<< HEAD:api_end_end_testing/src/test/java/tds/user/api/tests/CreateUserTest.java
public class CreateUserTest extends BaseUri{
    private String uriLocation = "/rest/external/user";
=======
public class UserApiTests extends BaseUri{
 //   String accessToken = null;
    String email = null;

    public void openAuthentication() {
        System.out.println("SET Restassured.baseURI: "+ authenticateURI);

        RestAssured.baseURI = authenticateURI;
        System.out.println("GET the access token");

        // Execute POST to open authentication and get access token
        accessToken = given()
            .contentType("application/x-www-form-urlencoded")
            .queryParam("realm", "/sbac")
            .formParam("client_id", "pm")
            .formParam("client_secret", "sbac12345")
            .formParam("grant_type", "password")
            .formParam("password", "password")
            .formParam("username", "prime.user@example.com")
            .when()
            .post("/auth/oauth2/access_token")
            .then()
            .statusCode(200)
            .extract()
            .path("access_token");
>>>>>>> feature/ART_API_testing:api_end_end_testing/src/test/java/tds/user/api/tests/UserApiTests.java

        System.out.println("accessToken: " + accessToken);
    }
    /*
<<<<<<< HEAD:api_end_end_testing/src/test/java/tds/user/api/tests/CreateUserTest.java
     * Test of Create User, HTTP POST of /rest/external/user, 201 success item created
     * Test Delete User by Email, HTTP DELETE of /rest/external/user, 204 success item
     * found and deleted
     */
    private void createUserOneRoleAssoc() {
=======
        Create user
        Testing HTTP POST of /rest/external/user, 201 success item created
    */
    @Test
    public void createUser() {
        super.init();
        openAuthentication();

        email = "betsy.ross@example.com";
System.out.println("Inside createUser: userUri is " + userUri);

        RestAssured.baseURI = userUri;
>>>>>>> feature/ART_API_testing:api_end_end_testing/src/test/java/tds/user/api/tests/UserApiTests.java

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "44886"));

        UserInfo userInfo = new UserInfo(userEmail, "amy", "watson", "800-332-4747", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(201)
            .header("location", endsWith(uriLocation + "/" + userEmail + "/details"));

        // Execute a GET to validate
    }


    private void deleteUser() {
        // Now delete the user that was just created
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userEmail)
        .when()
            .delete(uriLocation)
        .then()
            .statusCode(204);

        // Execute a GET to validate user is deleted

    }

    /*
     * Test of Create User, HTTP POST of /rest/external/user, 201 success item created
     * Test of Update User, HTTP POST of /rest/external/user, 204 success item updated
     */
 //   @Test
    public void updateUserOneRoleAssoc() {
        createUserOneRoleAssoc();

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "98765"));

        // Update the user with new information
        UserInfo userInfo = new UserInfo(userEmail, "Bob", "Miller", "858-748-1122", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(204);

     //   deleteUser();
    }

    @Test
    public void updateUserMultiRoleAssoc() {

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "66283"));
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "77392"));

        // Update the user with new information
        UserInfo userInfo = new UserInfo(userEmail, "Bob", "Miller", "619-222-1122", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(204);

     //   deleteUser();
    }

    /*
        Create invalid user
        Testing HTTP POST of /rest/external/user, 400 bad request
    */
   // @Test
    public void invalidUser() {

        RestAssured.baseURI = userURI;

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "98765"));
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "98765"));

        UserInfo userInfo = new UserInfo(userEmail, "Betsy", "", null, roleAssociations);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;

        try {
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfo);
            System.out.println("jsonInString: " + jsonInString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
            .when()
            .post("/rest/external/user")
            .then()
            .statusCode(400);
    }

    /*
        Get user by email
        Testing HTTP GET of /rest/external/user/{email}/details, 200 success return student info
    */
  //  @Test
    public void getUserByEmail() {
        RestAssured.baseURI = userURI;

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
            .when()
            .get("/rest/external/user/" + userEmail + "/details")
            .then()
            .statusCode(200);
    }

    /*
        Get user by Email that is non-existent
        Testing HTTP GET of /rest/external/user/{email}/details, 404 not found
    */
  //  @Test
    public void getUserByInvalidEmail() {
        RestAssured.baseURI = userURI;

        String invalidEmail = "invalidEmail@example.com";

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
            .when()
            .get("/rest/external/user/" + invalidEmail + "/details")
            .then()
            .statusCode(404);
    }


    /*
        Delete user by Email that is non-existent
        Testing HTTP DELETE of /rest/external/user, 404 not found
    */
    @Test
    public void deleteUserByInvalidEmail() {
        String invalidEmail = "abc123@example.com";

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(invalidEmail)
            .when()
            .delete(uriLocation)
            .then()
            .statusCode(404);
    }

}