package org.example;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

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
        System.setProperty("webdriver.gecko.driver","selenium\\geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36 OPR/60.0.3255.170";
        options.addPreference("general.useragent.override", userAgent);

        WebDriver driver = new FirefoxDriver(options);
        driver.get("https://csgorun.ru/");

        System.out.println("Enter a game number: ");
        int gameNum = sc.nextInt();
        System.out.println("Enter amount: ");
        int gameAmount = sc.nextInt();
        sc.close();


        ArrayList<Double> crashList = new ArrayList<>();
        Double crash;
        String JsonUrl = "";

//        for (int i = 1; i < 3; i++) {
//            JsonUrl = "https://api.csgorun.ru/games/" + (gameNum - i);
//            URL url_ar = new URL(JsonUrl);
//            ReadJSON reader_ar = new ReadJSON();
//            JSONObject json_ar = reader_ar.readJsonFromUrl(JsonUrl);  // Get 2 previous crashes
//            JSONObject dataJson_ar = json_ar.getJSONObject("data");
//            crashList.add(Double.valueOf(dataJson_ar.optString("crash")));
//            Thread.sleep(1000);
//        }

        for (int i = 0; i <= gameAmount; i++) {
            JsonUrl = "https://api.csgorun.ru/games/" + (gameNum + i);
            URL url = new URL(JsonUrl);

            if (doesURLExist(url)) {
                ReadJSON reader = new ReadJSON();       // To ReadJson in order to read from url.
                JSONObject json = reader.readJsonFromUrl(JsonUrl);  // calling method in order to read.
                JSONObject dataJson = json.getJSONObject("data");

                crash = Double.valueOf(dataJson.optString("crash"));
                crashList.add(crash);

                System.out.println(crashList.get(i));

            } else {
                i--;
                Thread.sleep(2000);
            }

            if (i > 1 && crashList.get(i) < 1.20 && crashList.get(i-1) < 1.20 && crashList.get(i-2) < 1.20) { // добавить условие i == (минимальное значение для проверки)
                System.out.println("Triple crash, staking...");
                driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[1]/div[2]/div[3]/div[2]/div/button[1]")).click(); // select item
                Thread.sleep(1000);
                driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div[1]/div[2]/div/div[4]/div/button")).click(); // stake
                Thread.sleep(120000);
            }

            Thread.sleep(1000);
        }

        driver.quit();
    }
}

