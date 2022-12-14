package Lesson4HWBackEnd;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SpoonacularLesson4Tests extends AbstrTest{
    String hash = "bfa3570beed03f94e74144d6a78ad4b3d7f176ef";
    String username = "evge0";
    @Test
    void getSearchRecipesMexicanTest(){

        given()
                .spec(getRequestSpecification())
                .queryParam("cuisine", "Mexican")
                .queryParam("intolerances", "gluten")
                .when()
                .get(getBaseUrl()+"/recipes/complexSearch")
                .then()
                .spec(getResponseSpecification());
    }
    @Test
    void getSearchRecipesType(){
        given()
                .spec(getRequestSpecification())
                .queryParam("type", "appetizer")
                .when()
                .get(getBaseUrl()+"/recipes/complexSearch")
                .then()
                .spec(getResponseSpecification());

    }
    @Test
    void getAutocompleteRecipeSearchTest(){
        given()
                .spec(getRequestSpecification())
                .queryParam("number", 4)
                .queryParam("query", "eggs")
                .when()
                .get(getBaseUrl()+"/recipes/autocomplete")
                .then()
                .spec(getResponseSpecification());
    }
    @Test
    void getRecipe516325Inf(){
        JsonPath response = given()
                .spec(getRequestSpecification())
                .queryParam("includeNutrition", "false")
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
                .spec(getRequestSpecification())
                .queryParam("hash", hash)
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
                .post(getBaseUrl()+"/mealplanner/"+username+"/items")
                .then()
                .spec(getResponseSpecification())
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        given()
                .spec(getRequestSpecification())
                .queryParam("hash", hash)
                .delete(getBaseUrl()+"/mealplanner/"+username+"/items/" + id)
                .then()
                .spec(getResponseSpecification());
    }
    @Test
    void postAddToShoppingList(){
        String id = given()
                .spec(getRequestSpecification())
                .queryParam("hash", hash)
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
                .spec(getResponseSpecification())
                .extract()
                .jsonPath()
                .get("id")
                .toString();
        given()
                .spec(getRequestSpecification())
                .queryParam("hash", hash)
                .get(getBaseUrl()+"/mealplanner/"+username+"/shopping-list")
                .then()
                .spec(getResponseSpecification());

        given()
                .spec(getRequestSpecification())
                .queryParam("hash", hash)
                .delete(getBaseUrl()+"/mealplanner/"+username+"/shopping-list/items/" + id)
                .then()
                .spec(getResponseSpecification());
    }
    @Test
    void postClassifyHomemadeGuacamoleTest(){
        given()
                .spec(getRequestSpecification())
                .queryParam("language", "en")
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Homemade Guacamole")
                .formParam("ingredientList", "chopped meat")
                .when()
                .post(getBaseUrl()+"/recipes/cuisine")
                .then()
                .spec(getResponseSpecification());
    }
    @Test
    void postAnalyzeRecipeSpaghetti(){
        given()
                .spec(getRequestSpecification())
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
                .spec(getResponseSpecification());
    }
    @Test
    void postParseIngredients(){
        given()
                .spec(getRequestSpecification())
                .queryParam("language", "en")
                .contentType("application/x-www-form-urlencoded")
                .formParam("ingredientList", "Grilled Chicken Gyros With Tzatziki")
                .formParam("servings", 4)
                .formParam("includeNutrition", true)
                .when()
                .post(getBaseUrl()+"/recipes/parseIngredients")
                .then()
                .spec(getResponseSpecification());
    }
}
