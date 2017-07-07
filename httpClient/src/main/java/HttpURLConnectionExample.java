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
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";


    public static void main(String[] args) throws Exception {

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();

       // new ThreadTest().test();
    }

    //write the data Json in the file result.json
    private void fileWrite(JSONObject jsonObject) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(
                new File("d:/IntelliJ IDEA/httpClient/result.json"), true));
        bw.write(jsonObject.toString() + "\t" + "\n");

        bw.flush();
        bw.close();
    }

    //check if the name of one product exists in the file result.json
    public String search(String name) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(
                "d:/IntelliJ IDEA/httpClient/result.json"));// read the file JSON

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

    // HTTP GET request
    private void sendGet() throws Exception {

        // use thread pool to begin connecting the Internet
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.execute(new Runnable(){
            public void run() {
                try {

                    try {
                        //the site of URL
                        String url = "https://world.openfoodfacts.org/cgi/search.pl?search_terms=boursin&sort_by=unique_scans_n&page_size=100&json=1";

                        // open the connection
                        URL obj = new URL(url);
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
                        JSONObject result = new JSONObject(); // create the JSON object
                        JSONObject jsonObject1 = new JSONObject(jsonString);

                        int pageSize = jsonObject1.getInt("page_size");
                        //System.out.println("pageSize : " + pageSize);

                        //check if there is  product
                        int count = jsonObject1.getInt("count");
                        if (count == 0) {
                            System.out.println("there is no product");
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

                        //look through all the information of one product
                            for (int i = 0; i < productsArray.length(); i++) {
                                JSONObject object2 = (JSONObject) productsArray.get(i);
                                String product_name = object2.getString("product_name");
                                //System.out.println("product_name; " + (product_name));

                                //String packaging = object2.getString("packaging");
                                //System.out.println("packaging : "+(packaging));

                                // JSONObject object3 = new JSONObject(object2);
                                JSONArray EAN = (JSONArray) object2.get("codes_tags"); // get he EAN-13
                                //add the product name ot jsonObject result
                                result.put("product_name", product_name);

                                if (EAN.getString(1) != null) {
                                    //System.out.println("EAN : " + (EAN.getString(1)));
                                    result.put("EAN", EAN.getString(1));
                                } else {
                                    //System.out.println("EAN is null ");
                                    result.put("EAN", 0);
                                }

                                // add the description
                                if (object2.has("packaging")) {
                                    result.put("Description", object2.getString("packaging"));
                                }
                                fileWrite(result);
            /*            if (packaging !=null) {
                            jsonObject.put("packaging", packaging);
                        }
                        else jsonObject.put("packaging","no description");*/

                                //System.out.println("jsonObject is " + jsonObject);

                            }

                        }


                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        executor.shutdown();//shut down the thread pool

    }


}