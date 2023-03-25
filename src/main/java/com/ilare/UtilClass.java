package com.ilare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class CsgorunApiCon {

    public static String Read(Reader re) throws IOException {
        StringBuilder str = new StringBuilder();
        int temp;

        do {
            temp = re.read();
            str.append((char) temp);
        } while (temp != -1);

        return str.toString();
    }

    public static JSONObject readJsonFromUrl(String link) throws IOException, JSONException {
        InputStream input = new URL(link).openStream();

        try {
            BufferedReader re = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
            String Text = Read(re);
            JSONObject json = new JSONObject(Text);

            return json;
        }
        catch (Exception e) {
            return null;
        }
        finally {
            input.close();
        }
    }

    public static boolean doesURLExist(URL url) throws IOException {
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("HEAD");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        int responseCode = httpURLConnection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_OK;
    }
}