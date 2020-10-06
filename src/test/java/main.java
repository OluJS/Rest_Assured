import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(DataProviderRunner.class)
public class main {

    private static ResponseSpecification responseSpecification;

    @BeforeClass
    public static void CreateRequestSpecification(){
        requestSpecification = new RequestSpecBuilder().
                setBaseUri("http://api.zippopotam.us").
                build();
    }

    @Test
    public void RequestExample(){
        given().
            spec(requestSpecification).
        when().
            get("gb/AB1").
        then().
            assertThat().
            statusCode(200);
    }

    @DataProvider
    public static Object[][] zipCodesAndPlaces(){
        return new Object[][]{
                {"gb", "AB1", "Sunnyside"}
        };
    }


    @Test
    @UseDataProvider("zipCodesAndPlaces")
    public void TTD(String countryCode, String zipCode, String placeName){
        given().
                pathParam("countryCode", countryCode).pathParam("zipCode", zipCode).
        when().
                get("http://api.zippopotam.us/{countryCode}/{zipCode}").
        then().
                assertThat().
                body("places[0].'place name'", equalTo(placeName));

    }

    @Test
    public void CheckLocationName(){
        given().
        when().
            get("http://api.zippopotam.us/GB/AB1").
        then().
                assertThat().
            body("places[0].'place name'", equalTo("Sunnyside"));
    }

    @Test
    public void CheckStatusCode(){
        given().
        when().
            get("http://api.zippopotam.us/GB/AB1").
        then().
            assertThat().
                statusCode(200);
    }

    @Test
    public void CheckContentType(){
        given().
        when().
            get("http://api.zippopotam.us/GB/AB1").
        then().
            assertThat().
                contentType(ContentType.JSON);
    }

    @Test
    public void LogRequestAndResponseDetails(){
        given().
            log().all().
        when().
            get("http://api.zippopotam.us/GB/AB1").
        then().
            log().body();
    }

    @Test
    public void HasItemMethod(){
        given().
        when().
            get("http://api.zippopotam.us/GB/AB1").
        then().
            assertThat().
                body("places.'place name'", hasItem("Sunnyside"));
    }

}
