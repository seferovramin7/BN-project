package com.autoauction.buynow.service;

import com.autoauction.buynow.model.BnArchive;
import com.autoauction.buynow.model.CopartTransportation;
import com.autoauction.buynow.model.IaaiTransportation;
import com.autoauction.buynow.repository.CopartRepository;
import com.autoauction.buynow.repository.IaaiRepository;
import com.autoauction.buynow.repository.LotRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeleniumService {
    @Autowired
    LotRepository lotRepository;
    @Autowired
    CopartRepository copartRepository;

    @Autowired
    IaaiRepository iaaiRepository;

    Boolean color = true;

    public WebDriver driver;


    public void goToCopart(String url, String lotType, String auctionType) throws Exception {
        System.setProperty("webdriver.chrome.driver", "phantomjs.exe");
        driver = new PhantomJSDriver();
        Dimension windowMinSize = new Dimension(500,500);
        driver.manage().window().setSize(windowMinSize);
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"joyride-step-WELCOME\"]/div/div[2]/tour-header/div/span\n")).click();
        collectCopartData(lotType, auctionType);
        WebElement isNextButtonEnabled = driver.findElement(By.xpath("//*[@id=\"mainBody\"]/div[2]/div[3]/div/app-root/lot-search-results/search-results/div/div[2]/div[2]/search-table-component/copart-table/div/p-table/div/p-paginator/div/button[3]\n"));
        if (isNextButtonEnabled.isEnabled()) {
            driver.findElement(By.xpath("//*[@id=\"mainBody\"]/div[2]/div[3]/div/app-root/lot-search-results/search-results/div/div[2]/div[2]/search-table-component/copart-table/div/p-table/div/p-paginator/div/button[3]\n")).click();
            collectCopartData(lotType, auctionType);
        }
        driver.quit();
    }
/////
    public void goToiaai(String motor, String url, String lotType, String auctionType) throws Exception {
        System.setProperty("webdriver.chrome.driver", "phantomjs.exe");
        driver = new PhantomJSDriver();
        Dimension windowMinSize = new Dimension(500,500);
        driver.manage().window().setSize(windowMinSize);
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        collectIaaiData(motor, lotType, auctionType);
        driver.quit();
    }

    public String collectCopartData(String carType, String auctionType) throws URISyntaxException {
        List<WebElement> numberRows = (driver.findElements(By.xpath("//*[@id=\"mainBody\"]/div[2]/div[3]/div/app-root/lot-search-results/search-results/div/div[2]/div[2]/search-table-component/copart-table/div/p-table/div/div[1]/table/tbody/tr\n")));
        int size = numberRows.size();
        String result = "";
        for (int i = 1; i <= size; i++) {
            String lot = driver.findElement(By.xpath("//*[@id=\"mainBody\"]/div[2]/div[3]/div/app-root/lot-search-results/search-results/div/div[2]/div[2]/search-table-component/copart-table/div/p-table/div/div[1]/table/tbody/tr[" + i + "]/td[2]/span[2]/div/span/div/span[2]/a\n")).getText();


            String odometer = driver.findElement(By.xpath("//*[@id=\"mainBody\"]/div[2]/div[3]/div/app-root/lot-search-results/search-results/div/div[2]/div[2]/search-table-component/copart-table/div/p-table/div/div[1]/table/tbody/tr[" + i + "]/td[3]/span[2]/div/div[1]/div")).getText();
            odometer = odometer.split(" miles")[0];
            Integer odo = Integer.parseInt(odometer);

            String bnPrice = driver.findElement(By.xpath("//*[@id=\"mainBody\"]/div[2]/div[3]/div/app-root/lot-search-results/search-results/div/div[2]/div[2]/search-table-component/copart-table/div/p-table/div/div[1]/table/tbody/tr[" + i + "]/td[6]/span[2]/div/div/div/div[3]/span/span/span\n")).getText();
            String buyNowString = bnPrice.substring(1, bnPrice.length()).replaceAll("\\,", "").replaceAll("\\.", "");
            buyNowString = buyNowString.substring(0, buyNowString.length() - 4);
            int priceInt = Integer.parseInt(buyNowString) / 100;


            String location = driver.findElement(By.xpath("//*[@id=\"mainBody\"]/div[2]/div[3]/div/app-root/lot-search-results/search-results/div/div[2]/div[2]/search-table-component/copart-table/div/p-table/div/div[1]/table/tbody/tr[" + i + "]/td[5]/span[2]/div/span[2]\n")).getText();
            CopartTransportation globalTransport = copartRepository.findByOriginAndLocationIgnoreCase("Global", location);
            int transportPrice = 0;
            int totalPrice = 0;
            if (globalTransport != null) {
                int price = Integer.parseInt(globalTransport.getPrice());
                transportPrice = price;
                totalPrice = priceInt + price;
            }

            BnArchive bnArchive = BnArchive.builder().buyNow(priceInt).lot(lot).auctionType(auctionType).build();
            String telegramMessage = createTelegramMessage(bnArchive, location, carType, transportPrice, totalPrice);

            if (!retrieveIfExists(lot, priceInt) && odo < 220000) {
                lotRepository.save(bnArchive);
                sendMessage(telegramMessage, auctionType);
                System.out.println("Telegram Message : \n" + telegramMessage);
            }
        }
        return result;
    }

    public String collectIaaiData(String motor, String carType, String auctionType) throws URISyntaxException {
        WebElement numberCars = driver.findElement(By.xpath("//*[@id=\"headerTotalAmount\"]"));
        String size = numberCars.getText().substring(0, 2).replaceAll("\\s+","");
        String result = "";
        int intSize = Integer.parseInt(size);
        for (int i = 1; i <= intSize; i++) {
            String lot = driver.findElement(By.xpath("/html/body/section/main/main/section[3]/div/div/div[2]/div[1]/div[2]/div[3]/div[2]/div[" + i + "]/div/div[3]/div[1]/h4/a")).getAttribute("href");
            lot = lot.substring(8, lot.length()).split("\\?")[0].split("\\/")[2];

            String location = driver.findElement(By.xpath("/html/body/section/main/main/section[3]/div/div/div[2]/div[1]/div[2]/div[3]/div[2]/div[" + i + "]/div/div[3]/div[2]/div[4]/ul/li[1]/span/a")).getText();
            location = location.split("\\(")[0].replaceAll("\\s+$", "").replaceAll("-", " ").toUpperCase();

            String bnPrice = driver.findElement(By.xpath("/html/body/section/main/main/section[3]/div/div/div[2]/div[1]/div[2]/div[3]/div[2]/div[" + i + "]/div/div[3]/div[2]/div[5]/ul/li[3]/span")).getText();
            bnPrice = bnPrice.substring(9, bnPrice.length()).replaceAll("\\,", "").replaceAll("\\.", "");
            int priceInt = Integer.parseInt(bnPrice);

            String motorFromIaai = driver.findElement(By.xpath("//*[@id=\"ListingGrid\"]/div[3]/div[2]/div["+i+"]/div/div[3]/div[2]/div[3]/ul/li[1]/span")).getText();
            Boolean ifMotorMeets = iaaiMotorChecker(motorFromIaai, motor);

            IaaiTransportation globalTransport = iaaiRepository.findByOriginAndLocationIgnoreCase("Global", location);
            int transportPrice = 0;
            int totalPrice = 0;
            if (globalTransport != null) {
                int price = Integer.parseInt(globalTransport.getPrice());
                transportPrice = price;
                totalPrice = priceInt + price;
            }

            BnArchive bnArchive = BnArchive.builder().buyNow(priceInt).lot(lot).auctionType(auctionType).build();
            String telegramMessage = createTelegramMessage(bnArchive, location, carType, transportPrice, totalPrice);

            if (!retrieveIfExists(lot, priceInt) && ifMotorMeets) {
                lotRepository.save(bnArchive);
                sendMessage(telegramMessage, auctionType);
                System.out.println("Telegram Message : \n" + telegramMessage);
            }
        }
        return result;
    }

    public String createTelegramMessage(BnArchive bnArchive, String location, String type, int transportPrice, int totalPrice) {

        int additionalPrice = 150 + 240;
        transportPrice += additionalPrice;
        totalPrice += additionalPrice;
        String link = "";
        if (bnArchive.getAuctionType().equals("copart")) {
            link = "copart.com/lot/" + bnArchive.getLot();
        } else {
            link = "iaai.com/VehicleDetails/" + bnArchive.getLot();
        }
        return " Type : " + type + " %0A "
                + "Location : " + location + " %0A "
                + "Buy Now Price : " + bnArchive.getBuyNow() + "$" + " %0A "
                + "Transport Price : " + transportPrice + "$" + " %0A "
                + "Total Price : " + totalPrice + "$" + " %0A " +
                link + " %0A ";
    }

    public void sendMessage(String text, String auctionType) throws URISyntaxException {
        if (color) {
            color = false;
        } else if (!color) {
            color = true;
        }
        String emoji = "";
        if (color) {
            emoji = "\uD83C\uDF4F";
        } else {
            emoji = "\uD83C\uDF4E";
        }

//        System.setProperty("https.proxyHost", "10.0.3.18");
//        System.setProperty("https.proxyPort", "3128");
        RestTemplate restTemplate = new RestTemplate();

        String baseUrl = "";

        if (auctionType.equals("copart")) {
            baseUrl =
                    "https://api.telegram.org/bot5121356280:AAFVerglwLuI9shViZFU5O-fH_e04t0wY18/sendMessage?chat_id=-1001596490509&text="
                            + emoji + " " + text;
        } else {
            baseUrl =
                    "https://api.telegram.org/bot5297651309:AAGpziE4Q27UrK77k51FwDkDZ_h7kdbheg0/sendMessage?chat_id=-1001733208631&text="
                            + emoji + " " + text;
        }
        String newUrlString = baseUrl.replaceAll(" ", "%20");
        URI uri = new URI(newUrlString);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, null, String.class);
    }

    public Boolean retrieveIfExists(String lot, Integer price) {
        Boolean exists = false;
        BnArchive byLot = lotRepository.findByLotAndAndBuyNow(lot, price);
        if (byLot != null) {
            exists = true;
        }
        return exists;
    }

    public Boolean iaaiMotorChecker(String iaaiMotor, String requiredMotor){
        Boolean requiredMotorBoolean = false;
        if (requiredMotor == null){
            requiredMotorBoolean = true;
        }else{
            if (iaaiMotor.contains(requiredMotor)){
                requiredMotorBoolean = true;
            }
        }
        return requiredMotorBoolean;
    }
}
