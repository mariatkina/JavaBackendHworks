package Lesson3HWBackend;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static com.sun.org.apache.xerces.internal.util.FeatureState.is;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SpoonaularTests extends UrlAndApiKeyAbstrTest{
    String hash = "bfa3570beed03f94e74144d6a78ad4b3d7f176ef";
    @Test
    void getSearchRecipesMexicanTest(){

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("cuisine", "Mexican")
                .queryParam("intolerances", "gluten")
                .log().all()
                .when()
                .get(getBaseUrl()+"/recipes/complexSearch")
                .then()
                .statusCode(200);

    }
    @Test
    void getSearchRecipesType(){
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("type", "appetizer")
                .log().all()
                .when()
                .get(getBaseUrl()+"/recipes/complexSearch")
                .then()
                .statusCode(200);

    }
    @Test
    void getRecipeInfoTest(){
        given()
                .when()
                .get(getBaseUrl()+"/recipes/{id}/information?" +
                        "includeNutrition={Nutrition}&apiKey={apiKey}",664565, true, getApiKey())
                .then()
                .statusCode(200);
    }
    @Test
    void getRandomRecipesTest(){
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", 98)
                .log().all()
                .when()
                .get(getBaseUrl()+"/recipes")
                .then()
                .statusCode(200);
    }
    @Test
    void getAutocompleteRecipeSearchTest(){
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", 4)
                .queryParam("query", "eggs")
                .log().all()
                .when()
                .get(getBaseUrl()+"/recipes/autocomplete")
                .then()
                .statusCode(200);
    }
    @Test
    void getRecipe516325Inf(){
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .log().params()
                .when()
                .get(getBaseUrl()+"/recipes/516325/information")
                .body()
                .jsonPath();

        assertThat(response.get("gaps"), equalTo("no"));
        assertThat(response.get("sourceName"), equalTo("Girl Versus Dough"));
    }
    @Test
    void postAddToMealPlanTest(){
        String id = given()
                .queryParam("hash", hash)
                .queryParam("apiKey", getApiKey())
                .log().all()
                .header("Content-Type","application/json")
                .body("{\n"
                        +"\"date\": 1670592619,\n"
                        +"\"slot\": 1,\n"
                        +"\"position\": 0,\n"
                        +"\"type\": \"RECIPE\",\n"
                        +"\"value\": {\n"
                        +"\"id\": 296213,\n"
                        +"\"servings\": 2,\n"
                        +"\"title\": \"Spinach Salad with Roasted Vegetables and Spiced Chickpea\",\n"
                        +"\"imageType\": \"jpg\",\n"
                            +"}\n"
                        +"}\n"
                )
                .when()
                .post(getBaseUrl()+"/mealplanner/evge0/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        given()
                .queryParam("hash", hash)
                .queryParam("apiKey", getApiKey())
                .log().all()
                .delete(getBaseUrl()+"/mealplanner/evge0/items/" + id)
                .then()
                .statusCode(200);
    }
    @Test
    void postAddToShoppingList(){
        String username = "evge0";

        String id = given()
                .queryParam("hash", hash)
                .queryParam("apiKey", getApiKey())
                .log().all()
                .header("Content-Type","application/json")
                .body("{\n"
                        +"\"item\": \"1 package baking powder\",\n"
                        +"\"aisle\": \"Baking\",\n"
                        +"\"parse\": true\n"
                        +"}"
                )
                .when()
                .post(getBaseUrl()+"/mealplanner/"+username+"/shopping-list/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        given()
                .queryParam("hash", hash)
                .queryParam("apiKey", getApiKey())
                .log().all()
                .delete(getBaseUrl()+"/mealplanner/"+username+"/shopping-list/items/" + id)
                .then()
                .statusCode(200);
    }
    @Test
    void postClassifyHomemadeGuacamoleTest(){
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("language", "en")
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Homemade Guacamole")
                .formParam("ingredientList", "chopped meat")
                .log().all()
                .when()
                .post(getBaseUrl()+"/recipes/cuisine")
                .then()
                .statusCode(200);

    }
    @Test
    void postAnalyzeRecipeSpaghetti(){
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("language", "en")
                .queryParam("includeNutrition", false)
                .queryParam("includeTaste", false)
                .contentType("application/json")
                .body(
                "{\n"
                +"\"title\": \"Spaghetti Carbonara\",\n"
                +"\"servings\": 2,\n"
                +"\"ingredients\": [\n"
                +"\"1 lb spaghetti\",\n"
                +"\"3.5 oz pancetta\",\n"
                +"\"2 Tbsps olive oil\",\n"
                +"\"1  egg\",\n"
                +"\"0.5 cup parmesan cheese\"\n"
                +"],\n"
                +"\"instructions\": "
                        +"\"Bring a large pot of water to a boil and season generously with salt. " +
                        "Add the pasta to the water once boiling and cook until al dente. " +
                        "Reserve 2 cups of cooking water and drain the pasta. \"\n"
                +"}"
                )
                .when()
                .post(getBaseUrl()+"/recipes/analyze")
                .then()
                .statusCode(200);

    }
    @Test
    void postParseIngredients(){
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("language", "en")
                .contentType("application/x-www-form-urlencoded")
                .formParam("ingredientList", "Grilled Chicken Gyros With Tzatziki")
                .formParam("servings", 4)
                .formParam("includeNutrition", true)
                .log().all()
                .when()
                .post(getBaseUrl()+"/recipes/parseIngredients")
                .then()
                .statusCode(200);



    }
}


