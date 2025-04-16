import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class skelbiult {
    @Test
    public void selenium(){

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Nustatome laukimo laiką
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Atidarome pirmą puslapį
        String url = "https://www.skelbiu.lt/skelbimai/1?autocompleted=1&keywords=verpimo+ratelis&cities=0&distance=0&mainCity=0&search=1&category_id=0&user_type=0&ad_since_min=0&ad_since_max=0&visited_page=1&orderBy=-1&detailsSearch=0";
        driver.get(url);

        // Laukiame, kol puslapis užsikraus ir skelbimų konteineriai taps matomi
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'standard')]")));

        // Randame visus skelbimų konteinerius
        List<WebElement> adElements = driver.findElements(By.className("content-block"));

        // Sąrašas kainoms saugoti
        List<Double> prices = new ArrayList<>();

        // Surenkame kainas iš pirmo puslapio
        for (WebElement adElement : adElements) {
            try {
                // Ištraukiame kainos tekstą (pvz., "15,8 e")
                String priceElement = adElement.findElement(By.className("price")).getText();
                System.out.println(priceElement);
                // Išvalome tekstą: pašaliname visus simbolius, išskyrus skaitmenis ir kablelį, tada kablelį keičiame į tašką
                String cleanedPrice = priceElement.replaceAll("[^0-9,]", "").replace(",", ".");
                // Konvertuojame tekstą į double
                double price = Double.parseDouble(cleanedPrice);
                prices.add(price);
            } catch (Exception e) {
                System.out.println("Klaida apdorojant kainą: " + e.getMessage());
            }
        }

        // Spausdiname kainas stulpeliu nuo viršaus į apačią
        System.out.println("Kainos iš pirmo puslapio:");
        for (Double price : prices) {
            System.out.println(price);
        }

        // Uždaryti naršyklę
        driver.quit();
    }
}




