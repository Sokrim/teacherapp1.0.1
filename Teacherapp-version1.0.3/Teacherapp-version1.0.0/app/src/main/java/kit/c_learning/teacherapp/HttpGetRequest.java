package kit.c_learning.teacherapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by kit on 2/10/18.
 */

public class HttpGetRequest extends AsyncTask<String, Void, String> {
    private static final int READ_TIMEOUT = 3000;
    private static final int CONNECTION_TIMEOUT = 3000;
    private String result;
    private ArrayMap<String, String> headers = new ArrayMap<>();
    private ArrayMap<String, String> data = new ArrayMap<>();
    private JSONObject jsonParam = new JSONObject();

    public HttpGetRequest(){
        this(null);
    }

    HttpGetRequest(ArrayMap<String, String> header_params){
        this(header_params, null);
    }

    HttpGetRequest(ArrayMap<String, String> header_params, ArrayMap<String, String> data_params){


        if (header_params != null && !header_params.isEmpty()){
            for (ArrayMap.Entry<String, String> header : header_params.entrySet()){

                try {
                    this.headers.put(header.getKey(), header.getValue());
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }

        if (data_params != null && !data_params.isEmpty()){
            for (ArrayMap.Entry<String, String> data_attr : data_params.entrySet()){
                try {
                    this.data.put(data_attr.getKey(), data_attr.getValue());
                } catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String method = params.length > 1 ? params[1] : "POST";
        String inputLine;

        try {
            //CreateThread a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //CreateThread a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();

            //Set headers
            if(!this.headers.isEmpty()){
                for (ArrayMap.Entry<String, String> header : this.headers.entrySet()){
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
            }


            switch (method){
                case "GET":
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    break;
                case "POST":
                    Log.e("mer method post ----------------","-----------");

                    connection.setRequestProperty("qb","qbID");
                    connection.setRequestProperty("com","0");
                    connection.setRequestProperty("mode","All");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    break;
                default:
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    break;
            }

            //Set methods and timeouts
            connection.setRequestMethod(method);
//            connection.setReadTimeout(READ_TIMEOUT);
//            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            // Set post data if has
            if(!data.isEmpty()){
                try{
                    for (ArrayMap.Entry<String, String> attr_data : data.entrySet()){
                        jsonParam.put(attr_data.getKey(), attr_data.getValue());
                    }

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(jsonParam));
                    writer.flush();
                    writer.close();
                    os.close();

                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            int responseCode=connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        connection.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line;

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }else if (responseCode == HttpURLConnection.HTTP_NOT_ACCEPTABLE){
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        connection.getErrorStream()));

                StringBuffer sb = new StringBuffer("");
                String line;

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            }
            else {
                Log.e("++++++++++++++++= ", String.valueOf(responseCode));
                return "false : " + responseCode;
            }
        }
        catch(IOException e){
            e.printStackTrace();
            Log.e("errrorr------------",e.toString());
            Log.e(HttpRequestAsync.class.getSimpleName(), "HttpRequestUrl Catch Exception " + e.getMessage());
            this.result = "failed";
        }

        return this.result;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    private String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
