package tds.user.api.tests;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.jayway.restassured.http.ContentType;
import org.testng.annotations.Test;
import tds.base.BaseUri;
import tds.user.api.model.UserInfo;
import tds.user.api.model.RoleAssociation;

/*
 * This class tests User API endpoints that creates, updates and deletes a user
 */
public class UserApiTests extends BaseUri {

    private String uriLocation = "/rest/external/user";

    /*
     * Test of Create User, HTTP POST of /rest/external/user, 201 success item created
     * Test of Get User, HTTP GET of /rest/external/user/{email}/details, 200 success item found
     */
    private UserInfo createUserOneRoleAssoc(String userEmail) {
        String entityId = "44886";
        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", entityId));

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

        // Execute a GET by email to validate that the user was created
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + "/" + userEmail + "/details")
        .then()
            .statusCode(200)
            .body("firstName", is(userInfo.getFirstName()))
            .body("lastName", is(userInfo.getLastName()))
            .body("phoneNumber", is(userInfo.getPhoneNumber()))
            .body("roleAssociations[0].entityId", equalTo(entityId));

        return userInfo;
    }

    /*
     *  Test of Delete User, HTTP DELETE of /rest/external/user, 204 success item found and deleted
     *  Test of Get User, HTTP GET of /rest/external/user/{email}/details, 404 item not found
     */
    private void deleteExistingUser(String userEmail) {
        // Execute a Delete to remove the user that was just created
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userEmail)
        .when()
            .delete(uriLocation)
        .then()
            .statusCode(204);

        // Execute a GET by email to validate user is deleted
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + "/" + userEmail + "/details")
        .then()
            .statusCode(404);
    }

    /*
     * Test of Create User, HTTP POST of /rest/external/user, 201 success item created
     * Test of Delete User, HTTP DELETE of /rest/external/user, 204 success item found and deleted
     */
    @Test
    public void shouldCreateDeleteUserWithOneRole() {
        String randomUserEmail = createRandomUserEmail();

        // Create a user with one role association
        UserInfo userInfo = createUserOneRoleAssoc(randomUserEmail);

        // Execute a DELETE to delete the user
        deleteExistingUser(randomUserEmail);
    }

    /*
     * Test of Create User, HTTP POST of /rest/external/user, 201 success item created
     * Test of Update User, HTTP POST of /rest/external/user, 204 success item updated
     * Test of Delete User, HTTP DELETE of /rest/external/user, 204 success item found and deleted
     */
    @Test
    public void shouldCreateUpdateDeleteUserWithOneRole() {
        String entityId = "10040";

        String randomUserEmail = createRandomUserEmail();

        UserInfo userInfo = createUserOneRoleAssoc(randomUserEmail);

        // Prepare Role with new data to update user
        List<RoleAssociation> roleAssociations = userInfo.getRoleAssociations();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", entityId));

        userInfo.setFirstName("Kelly");
        userInfo.setLastName("Yates");
        userInfo.setPhoneNumber("619-224-7865");
        userInfo.setRoleAssociations(roleAssociations);

        // Execute a POST to update the user with new information
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(204)
            .header("location", endsWith(uriLocation + "/" + randomUserEmail + "/details"));

        // Execute a GET by email to verify that the user information has changed
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + "/" + randomUserEmail + "/details")
        .then()
            .statusCode(200)
            .body("firstName", is(userInfo.getFirstName()))
            .body("lastName", is(userInfo.getLastName()))
            .body("phoneNumber", is(userInfo.getPhoneNumber()));

        // Execute a DELETE to delete the user
        deleteExistingUser(randomUserEmail);
    }

    /*
     * Test of Create User, HTTP POST of /rest/external/user, 201 success item created
     * Test of Update User with multiple roles, HTTP POST of /rest/external/user, 204 success item updated
     * Test of Delete User, HTTP DELETE of /rest/external/user, 204 success item found and deleted
     */
    @Test
    public void shouldCreateUpdateUserToMultipleRoles() {
        String randomUserEmail = createRandomUserEmail();

        UserInfo userInfo = createUserOneRoleAssoc(randomUserEmail);

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "32467"));
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "11276"));

        userInfo.setFirstName("Miranda");
        userInfo.setLastName("Bailey");
        userInfo.setPhoneNumber("415-332-9090");
        userInfo.setRoleAssociations(roleAssociations);

        // Execute a POST to update the user with new information
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(204)
            .header("location", endsWith(uriLocation + "/" + randomUserEmail + "/details"));

        // Execute a DELETE to delete the user
        deleteExistingUser(randomUserEmail);
    }

    /*
     * Test of Create User with invalid email, HTTP POST of /rest/external/user, 400 bad request
     */
    @Test
    public void shouldNotCreateUserWithInvalidEmail() {
        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "19037"));

        UserInfo userInfo = new UserInfo("bademail", "Judy", "Bloom", "808-443-1199", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(400)
            .body("messages.email[0]", equalTo("User Email Address must be a valid email address"));
    }

    /*
     * Test of Create User with invalid first name, HTTP POST of /rest/external/user, 400 bad request
     */
    @Test
    public void shouldNotCreateUserWithInvalidFirstName() {
        String randomUserEmail = createRandomUserEmail();

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "55342"));

        UserInfo userInfo = new UserInfo(randomUserEmail, "", "Dodge", "714-228-4848", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(400)
            .body("messages.firstName[0]", equalTo("User First Name is required"));
    }

    /*
     * Test of Create User with invalid last name, HTTP POST of /rest/external/user, 400 bad request
     */
    @Test
    public void shouldNotCreateUserWithInvalidLastName() {
        String randomUserEmail = createRandomUserEmail();

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "55342"));

        UserInfo userInfo = new UserInfo(randomUserEmail, "Wiliam", "", "714-228-4848", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(400)
        .body("messages.lastName[0]", equalTo("User Last Name is required"));
    }

    /*
     * Test of Create User with invalid phone, HTTP POST of /rest/external/user, 500 server error
     */
    @Test
    public void shouldNotCreateUserWithInvalidPhone() {
        String randomUserEmail = createRandomUserEmail();

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "11008"));

        UserInfo userInfo = new UserInfo(randomUserEmail, "Mark", "Beel", "1-808-883-7783", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(400)
            .body("messages.phone[0]", startsWith("User Telephone Number must be in the format"));
    }

    /*
     * Test of Create User with invalid role, HTTP POST of /rest/external/user, 500 server error
     */
    @Test
    public void shouldNotCreateUserWithInvalidRole() {
        String randomUserEmail = createRandomUserEmail();

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation(null, "CLIENT", "11008"));

        UserInfo userInfo = new UserInfo(randomUserEmail, "Mark", "Beel", "808-883-7783", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(500);
    }

    /*
     * Test of Create User with invalid level, HTTP POST of /rest/external/user, 400 bad request
     */
    @Test
    public void shouldNotCreateUserWithInvalidLevel() {
        String randomUserEmail = createRandomUserEmail();

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "Customer", "33010"));

        UserInfo userInfo = new UserInfo(randomUserEmail, "Meredith", "Grey", "510-335-1212", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(400);
    }

    /*
     * Test of Create User with invalid level, HTTP POST of /rest/external/user, 500 server error
     */
    @Test
    public void shouldNotCreateUserWithInvalidEntityId() {
        String randomUserEmail = createRandomUserEmail();

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", null));

        UserInfo userInfo = new UserInfo(randomUserEmail, "Miranda", "Bailey", "213-145-2000", roleAssociations);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
        .when()
            .post(uriLocation)
        .then()
            .statusCode(500);
    }

    /*
     *  Test of Get User that does not exist, HTTP GET of /rest/external/user/{email}/details, 404 not found
     */
    @Test
    public void shouldNotFindUserWithNonExistingEmail() {
        String randomUserEmail = createRandomUserEmail();

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + randomUserEmail + "/details")
        .then()
            .statusCode(404);
    }


    /*
     *  Test of Delete User with bad email, HTTP DELETE of /rest/external/user, 404 item not found
     */
    @Test
    public void shouldNotDeleteUserWithBadEmail() {
        String randomUserEmail = createRandomUserEmail();

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(randomUserEmail + "@example2")
        .when()
            .delete(uriLocation)
        .then()
            .statusCode(404);
    }
}
