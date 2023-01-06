package com.libin.urltopdf.util;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;


/**
 * @author libin
 */
public class UrlToPdfUtil {
    private static Font headfont;// 设置字体大小
    private static Font keyfont;// 设置字体大小
    private static Font textfont;// 设置字体大小

    static {
        BaseFont bfChinese;
        try {
            // bfChinese =BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            headfont = new Font(bfChinese, 10, Font.BOLD);
            // 设置字体大小
            keyfont = new Font(bfChinese, 9, Font.BOLD);
            // 设置字体大小
            textfont = new Font(bfChinese, 8, Font.NORMAL);
            // 设置字体大小
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
        String url = "https://mp.weixin.qq.com/s/3Oj_dOt55Io76BR_mSIOgw";
        //url = "https://mp.weixin.qq.com/s/AeaQdRTfwvR0JdkfdJk04g";
        // url = "https://mp.weixin.qq.com/s/izdCiDpZl0UJ0LPR2rg5hQ";
        url = "https://mp.weixin.qq.com/s/TSyKR22WPaErw7N-CkmNVA";
        byte[] imgBytes = convertHtml2Image(url);
        ByteArrayOutputStream byteArrayOutputStream = createPdf(imgBytes);

        File file = new File("/weixin-pdf.pdf");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream = new FileOutputStream("/weixin-pdf.pdf");
        byteArrayOutputStream.writeTo(fileOutputStream);
    }

    public static ByteArrayOutputStream createPdf(final byte[] imgBytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        document.setPageCount(10);
        document.setPageSize(new Rectangle(PageSize.A4));
        try {
            PdfWriter.getInstance(document, baos);


            document.open();
            //document.add(new Paragraph("hello world"));
            Image png = Image.getInstance(imgBytes);


            Rectangle defaultPageSize = PageSize.A4;

            System.out.println("defaultPageSize:" + defaultPageSize.getWidth() + "  " + defaultPageSize.getHeight());
            float pageWidth = defaultPageSize.getWidth() - document.rightMargin() - document.leftMargin();
            float pageHeight = defaultPageSize.getHeight() - document.topMargin() - document.bottomMargin();
            System.out.println("pageWidth:" + pageWidth + " " + pageHeight);
            System.out.println("png:" + png.getWidth() + " " + png.getHeight());
            System.out.println("pngscale:" + png.getScaledWidth() + " " + png.getScaledHeight());
            if (png.getScaledWidth() >= pageWidth || png.getScaledHeight() >= pageHeight) {
                png.scaleToFit(pageWidth, pageHeight);
            }

            png.scaleToFit(822, pageHeight);
            png.setAlignment(7);//居中
            document.add(png);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return baos;
        }
    }

    public static byte[] convertHtml2Image(String url) throws InterruptedException, IOException, ExecutionException {
        byte[] scrFile = null;
        // 设置 chromedriver 地址
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        // 关闭日志
        System.setProperty("webdriver.chrome.silentOutput", "true");

        //实例化一个Chrome浏览器的实例
        ChromeOptions options = new ChromeOptions();
        //chrome安装路径(linux用)
        //options.setBinary("/usr/bin/google-chrome");
        // 字符编码 utf-8 支持中文字符
        options.addArguments("lang=zh_CN.UTF-8");
        // 开启最大化
        options.addArguments("–start-maximized");
        options.addArguments("–no-sandbox");
        // 开启无头模式
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--silent");
        options.addArguments("--kiosk-printing");
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-dev-shm-usage");

        // 关闭日志
        options.addArguments("--disable-logging");
        WebDriver driver = new ChromeDriver(options);

        driver.get(url);
        // 刷新页面
        driver.navigate().refresh();
        //实现滚轮向下滑动
        //设置5秒,全局寻找元素的等待时间
       /*  driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20000000))
        .implicitlyWait(Duration.ofSeconds(20000000))
                .scriptTimeout(Duration.ofSeconds(20000000));*/


        WebElement htmlElement = driver.findElement(By.tagName("html"));
        int h = htmlElement.getSize().getHeight() / 100 + 2;
        for (int i = 0; i < h; i++) {
            System.out.println("sleep 1s" + h);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(2)).implicitlyWait(Duration.ofSeconds(2)).scriptTimeout(Duration.ofSeconds(2));
            Thread.sleep(10);
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + (i * 90) + ")");
        }




        Thread.sleep(2000);
        int height = Integer.parseInt((String) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight.toString()"));
        int width = Integer.parseInt((String) ((JavascriptExecutor) driver).executeScript("return document.body.scrollWidth.toString()"));

        driver.manage().window().setSize(new Dimension(width, height));
        driver.manage().window().maximize();


        //截屏操作
        //截图到output
        scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        driver.close();
        driver.quit();
        return scrFile;
    }


}
