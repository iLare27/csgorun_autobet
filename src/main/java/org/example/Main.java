package org.example;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static boolean doesURLExist(URL url) throws IOException
    {
        // We want to check the current URL
        HttpURLConnection.setFollowRedirects(false);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        // We don't need to get data
        httpURLConnection.setRequestMethod("HEAD");

        // Some websites don't like programmatic access so pretend to be a browser
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        int responseCode = httpURLConnection.getResponseCode();

        // We only accept response code 200
        return responseCode == HttpURLConnection.HTTP_OK;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.setProperty("webdriver.chrome.driver","selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        System.out.println("Enter a game number: ");
        int gameNum = sc.nextInt();
        System.out.println("Enter amount: ");
        int gameAmount = sc.nextInt();
        sc.close();

        ArrayList<Double> crashList = new ArrayList<>();
        crashList.add(99999.0);
        crashList.add(99999.0);
        Double crash;
        String JsonUrl = "https://api.csgorun.ru/games/" + gameNum;

        for (int i = 0; i < gameAmount; i++){
            JsonUrl = "https://api.csgorun.ru/games/" + (gameNum + i);
            URL url = new URL(JsonUrl);

            if (doesURLExist(url)) {
                ReadJSON reader = new ReadJSON();       // To ReadJson in order to read from url.
                JSONObject json = reader.readJsonFromUrl(JsonUrl);  // calling method in order to read.
                JSONObject dataJson = json.getJSONObject("data");

                crash = Double.valueOf(dataJson.optString("crash"));
                crashList.add(crash);

                System.out.println(crash);

                if (crashList.get(i+2) < 1.26 && crashList.get(i+1) < 1.26 && crashList.get(i) < 1.26) {
                    System.out.println("Launching auto-bet...");
                    driver.get("https://csgorun.ru/");
                }

            } else {
                i--;
                Thread.sleep(5000);
            }
            Thread.sleep(1000);
        }

    }
}

