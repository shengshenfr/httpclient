import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";
    private String inputFilePath ;
    private String resultPath ;
    private URL urlDb; //search url of database
    private String param, key; //user defined search parameters and key represents product name (in our case "search_terms")
    private String charset; //ofen UTF-8

    //construct
    public HttpURLConnectionExample (String inputFilePath,String resultPath, URL urlDb, String charset, String param, String key) {
        this.inputFilePath = inputFilePath;
        this.resultPath = resultPath;
        this.urlDb = urlDb;
        this.charset = charset;
        this.param = param;
        this.key = key;
    }


    public static void main(String[] args) throws Exception {

        //String inputFilePath = "d:/IntelliJ IDEA/httpClient/inputfile_example.txt";
        //String resultPath = "d:/IntelliJ IDEA/httpClient/result.json";
        //create the URL
        System.out.println(args[0]);
        System.out.println(args[1]);

        URL urlDb = new URL("https://world.openfoodfacts.org/cgi/search.pl");
        String param = "&search_simple=1&action=process&json=1&page=1&page_size=100";

        System.out.println("Testing 1 - Send Http GET request");
        HttpURLConnectionExample http = new HttpURLConnectionExample(args[0],args[1],urlDb, "UTF-8", param, "search_terms");
        //http.sendGet();
        http.read();
       // new ThreadTest().test();
    }

    //write the data Json in the file result.json
    private void fileWrite(JSONObject jsonObject) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(
                new File(resultPath), true));
        bw.write(jsonObject.toString() + "\t" + "\n");

        bw.flush();
        bw.close();
    }

    //check if the name of one product exists in the file result.json
    public String search(String name) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(
                resultPath));// read the file JSON

        String product_name = null;//define the product_name is null
        String s = null;
        //read the data by line
        while ((s = br.readLine()) != null) {
            System.out.println(s);

            JSONObject dataJson = new JSONObject(s);// create an objet JSON
            product_name = dataJson.getString("product_name");
            //if the product_name is the same of input product name, give this product_name to name
            if (name == product_name){
                name = product_name;
            }

        }
        return product_name;
    }

    //read the inputfile
    public void read() throws Exception {

        final String product_name = null;
        final BufferedReader br = new BufferedReader(new FileReader(
                inputFilePath));// read the file txt
        //use the threadpool to read the product
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.execute(new Runnable() {
            public void run() {
                try {
                    //define the product_name is null
                    String s = null;

                    while ((s = br.readLine()) != null) {
                        sendGet(s);
                        //System.out.println(s);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        executor.shutdown();
    }

    // HTTP GET request
    private JSONObject sendGet(String product) throws Exception {
        String productName;
        String description;
        JSONObject result = new JSONObject(); // create the JSON object

        //get product name and description from inputfile_example
        if (product.contains("/")) {
            productName = product.split("/",2)[0];
            description = product.split("/",2)[1];
        }
        else {
            productName = product;
            description = " ";
        }

        try {

            //create object url
            String productFormat = String.format("?" + key + "=%s"
                    , URLEncoder.encode(productName, charset));
            URL obj = new URL(urlDb + productFormat + param);

            // open the connection
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            //System.out.println("\nSending 'GET' request to URL : " + url);
            //System.out.println("Response Code : " + responseCode);

            //get response stream
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            //add inputLine in response
            while ((inputLine = in.readLine()) != null) {

                response.append(inputLine);

            }
            // close the connection
            in.close();

            //print result
            //System.out.println(response.toString());
            //change the type of response to String
            String jsonString = response.toString();


            // System.out.println("jsonString : "+jsonString.page_size);

            JSONObject jsonObject1 = new JSONObject(jsonString);

            //int pageSize = jsonObject1.getInt("page_size");
            //System.out.println("pageSize : " + pageSize);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
            //check if there is  product
            int count = jsonObject1.getInt("count");
            System.out.println("count : "+count );
            if (count == 0) {
                result.put("product_name", productName);
                result.put("description", description);
                System.out.println("there is no product found");
            } else {
                //JSONArray page_size = (JSONArray) jsonObject1.get("page_size");
                // jsonObject1.page_size = 1000;
                // int page_size1 = jsonObject1.getInt("page_size");
                //  System.out.println("page_size1 : "+page_size1);

                JSONArray productsArray = jsonObject1.getJSONArray("products");// create the JSON Array on the base of JSON object

            /*        List<String> list = new ArrayList<String>();
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("EAN", "product_name");
                    ArrayList<String> m = new ArrayList<String>();*/
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                for (int i = 0; i < productsArray.length(); i++) {
                    JSONObject object2 = (JSONObject) productsArray.get(i);
                    //String product_name = object2.getString("product_name");
                    //System.out.println("product_name; " + (product_name));

                    //String packaging = object2.getString("packaging");
                    //System.out.println("packaging : "+(packaging));

                    // JSONObject object3 = new JSONObject(object2);
                    //JSONArray EAN = (JSONArray) object2.get("codes_tags"); // get he EAN-13
                    //add the product name ot jsonObject result
                    result.put("product_name", productName);
                    result.put("EAN code", object2.getString("code"));
/*                    if (EAN.getString(1) != null) {
                        //System.out.println("EAN : " + (EAN.getString(1)));
                        result.put("EAN", EAN.getString(1));
                    } else {
                        //System.out.println("EAN is null ");
                        result.put("EAN", 0);
                    }*/

                    // add the description
                    if (object2.has("packaging")) {
                        result.put("description", object2.getString("packaging"));
                    }
                    else{
                        result.put("description", description);
                    }
/*            if (packaging !=null) {
                jsonObject.put("packaging", packaging);
            }
            else jsonObject.put("packaging","no description");*/

                    //System.out.println("jsonObject is " + jsonObject);

                }
            }
            System.out.println("result " +result);
            fileWrite(result);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}


