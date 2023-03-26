package com.ilare;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class CsgorunAutoBetApplication {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.setProperty("webdriver.gecko.driver","selenium\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";
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
        for (int i = 0; i <= gameAmount; i++) {
            JsonUrl = "https://api.csgorun.ru/games/" + (gameNum + i);
            URL url = new URL(JsonUrl);
            if (UtilClass.doesURLExist(url)) {
                JSONObject json = UtilClass.readJsonFromUrl(JsonUrl);
                JSONObject dataJson = json.getJSONObject("data");
                crash = Double.valueOf(dataJson.optString("crash"));
                crashList.add(crash);
                System.out.println(crashList.get(i));
            } else {
                i--;
                Thread.sleep(2000);
            }
            if (i > 1 && crashList.get(i) < 1.20 && crashList.get(i-1) < 1.20 && crashList.get(i-2) < 1.20) {
                System.out.println("Triple crash, staking...");
                Thread.sleep(5000);
                driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[1]/div[2]/div[3]/div[2]/div/button[1]")).click();
                Thread.sleep(1000);
                driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div[1]/div[2]/div/div[4]/div/button")).click();
                Thread.sleep(120000);
            }
            Thread.sleep(1000);
        }
        driver.quit();
    }
}
