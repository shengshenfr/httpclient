/**
 * Created by Administrator on 2017/6/23.
 */
import static org.junit.Assert.*;



import org.json.JSONObject;
import org.junit.Test;

import java.net.URL;


public class test {
    @Test
    public void test() throws Exception {
        URL urlDb = new URL("https://world.openfoodfacts.org/cgi/search.pl");
        String param = "&search_simple=1&action=process&json=1&page=1&page_size=100";

        String inputFilePath = "d:/IntelliJ IDEA/httpClient/inputfile_example.txt";
        String resultPath = "d:/IntelliJ IDEA/httpClient/result.json";
        System.out.println("Testing 1 - Send Http GET request");
        HttpURLConnectionExample http = new HttpURLConnectionExample(inputFilePath,resultPath,urlDb, "UTF-8", param, "search_terms");
        //choose one product name from site
        JSONObject example1 = new JSONObject("{\"product_name\":\"Ail & Fines Herbes (40 % MG)\"}");
        //check if it exists in the file result
        assertEquals(example1.toString(), http.search("Ail & Fines Herbes (40 % MG)"));
        assertEquals(example1.toString(), http.search("write you want"));
    }
}

