import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class DataDrivenTest_AddNewEmployees {


    @Test(dataProvider ="empdataprovider")
    void postNewEmployees(String parName, String parSalary, String parAge) {

        RestAssured.baseURI = "http://dummy.restapiexample.com/api/v1";

        RequestSpecification httpRequest = RestAssured.given();

        JSONObject requestParams = new JSONObject();

        requestParams.put("name", parName);
        requestParams.put("salary", parSalary);
        requestParams.put("age", parAge);

        httpRequest.header("Content-type", "application/json");

        httpRequest.body(requestParams.toJSONString());

        Response response = httpRequest.request(Method.POST,"/create");

        String responseBody = response.getBody().asString();

        System.out.println("Response Body is "+responseBody);

        Assert.assertEquals(responseBody.contains(parName), true);
        Assert.assertEquals(responseBody.contains(parSalary), true);
        Assert.assertEquals(responseBody.contains(parAge), true);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);

    }

    @DataProvider(name="empdataprovider")
    Object [][] getEmpData() throws IOException {

        //Read data from Excel
        String path = System.getProperty("user.dir")+"/src/test/java/empdata.xlsx";

        int rowNum = XLUtils.getRowCount(path,"Sheet1");
        int colCount = XLUtils.getCellCount(path,"Sheet1",1);

        String empdata[][] = new String[rowNum][colCount];

        for (int i=1; i<=rowNum; i++) {
            for (int j=0; j<colCount; j++) {
                empdata[i - 1][j] = XLUtils.getCellData(path,"Sheet1",i,j);
            }
        }

        return empdata;
    }

}
