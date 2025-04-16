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
    public void selenium() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Nustatome laukimo laiką
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Naudojame List<Double> kainoms rinkti
        List<Double> allPricesList = new ArrayList<>();

        // Puslapių URL šablonas
        String baseUrl = "https://www.skelbiu.lt/skelbimai/1?autocompleted=1&keywords=barzdaskute&cities=0&distance=0&mainCity=0&search=1&category_id=5&user_type=0&ad_since_min=0&ad_since_max=0&visited_page=2&orderBy=-1&detailsSearch=0";

        // Naudojame while ciklą, kad pereitume per trylika puslapiu
        int page = 1;
        while (page <= 13) {
            String url = String.format(baseUrl, page, page);
            driver.get(url);

            // Laukiame, kol puslapis užsikraus
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'standard')]")));

            // Priimame slapukus, jei yra toks mygtukas (tik pirmame puslapyje)
            if (page == 1) {
                try {
                    WebElement acceptBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
                    acceptBtn.click();
                    Thread.sleep(500);
                } catch (Exception e) {
                    System.out.println("Slapukų mygtukas nerastas, tęsiame...");
                }
            }

            // Randame visus skelbimų konteinerius
            List<WebElement> adElements = driver.findElements(By.className("content-block"));

            // Surenkame kainas iš dabartinio puslapio
            for (WebElement adElement : adElements) {
                try {
                    String priceElement = adElement.findElement(By.className("price")).getText(); // Pvz., "15,8 e"
                    // Išvalome tekstą: pašaliname visus simbolius, išskyrus skaitmenis ir kablelį, tada pakeičiame kablelį į tašką
                    String cleanedPrice = priceElement.replaceAll("[^0-9,]", "").replace(",", ".");
                    Double price = Double.parseDouble(cleanedPrice);
                    allPricesList.add(price); // Pridedame kainą į sąrašą
                } catch (Exception e) {
                    System.out.println("Klaida apdorojant kainą: " + e.getMessage());
                }
            }

            // Didiname puslapio numerį
            page++;
        }

        // Konvertuojame List<Double> į Double[] masyvo tipą
        Double[] allPrices = allPricesList.toArray(new Double[0]);

        // Spausdiname visas kainas stulpeliu
        System.out.println("Visos kainos iš trylikos puslapių:");
        for (Double price : allPrices) {
            System.out.println(price);
        }

        // Apskaičiuojame ir spausdiname vidurkį
        if (allPrices.length > 0) {
            double sum = 0;
            for (Double price : allPrices) {
                sum += price;
            }
            double average = sum / allPrices.length;
            System.out.println("Vidutinė kaina iš visų trylikos puslapių: " + average);
        } else {
            System.out.println("Kainų nerasta.");
        }

        // Uždaryti naršyklę
        driver.quit();
    }
}

