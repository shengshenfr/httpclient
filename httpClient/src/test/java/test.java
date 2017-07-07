/**
 * Created by Administrator on 2017/6/23.
 */
import static org.junit.Assert.*;


import org.json.JSONObject;
import org.junit.Test;


public class test {
    @Test
    public void test() throws Exception {
        HttpURLConnectionExample http = new HttpURLConnectionExample();
        //choose one product name from site
        JSONObject example1 = new JSONObject("{\"product_name\":\"Ail & Fines Herbes (40 % MG)\"}");
        //check if it exists in the file result
        assertEquals(example1.toString(), http.search("Ail & Fines Herbes (40 % MG)"));
        assertEquals(example1.toString(), http.search("write any name"));
    }
}
