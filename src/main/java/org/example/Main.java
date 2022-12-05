package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        String baseUrl = "https://csgorun.ru/";
        driver.get(baseUrl);

        int crashTime = 6; //120sec
        String text = "";
        ArrayList <Double> crashList = new ArrayList<>();

        for (int i = 0; i < crashTime; i++){
            text = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[2]/div[3]/a[1]")).getText();
            System.out.println(text);
            Double crash = Double.valueOf(text.substring(0, text.length() - 1));
            crashList.add(crash);
            Thread.sleep(10000);
        }

        try{
            FileOutputStream fos = new FileOutputStream("out.txt");
            DataOutputStream dos =  new DataOutputStream(fos);
            for(Double d:crashList)
                dos.writeDouble(d);
            dos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        driver.close();
    }

}