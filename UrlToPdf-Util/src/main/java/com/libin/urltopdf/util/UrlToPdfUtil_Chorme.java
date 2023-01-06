package com.libin.urltopdf.util;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.HashMap;


/**
 * @author libin
 */
public class UrlToPdfUtil_Chorme {

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

    public static void main(String[] args) throws Exception {
        // System.setProperty("java.awt.headless", "false");
        String url = "https://mp.weixin.qq.com/s/3Oj_dOt55Io76BR_mSIOgw";
        //url = "https://mp.weixin.qq.com/s/AeaQdRTfwvR0JdkfdJk04g";
        // url = "https://mp.weixin.qq.com/s/izdCiDpZl0UJ0LPR2rg5hQ";
        url = "https://mp.weixin.qq.com/s/TSyKR22WPaErw7N-CkmNVA";

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
        // 最高权限
        options.addArguments("–no-sandbox");
        // 开启无头模式 不能开启 会导致 打印失效
        // options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--silent");

        // 静默打印
        options.addArguments("--kiosk-printing");
        //打印 预览 不能开启 会导致 浏览器不自动关闭
        //options.addArguments("--enable-print-browser");
        options.setExperimentalOption("useAutomationExtension", true);
        options.addArguments("--disable-dev-shm-usage");

        // 关闭日志
        options.addArguments("--disable-logging");

            //添加UA
        options.addArguments("user-agent='MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1'");


        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("download.prompt_for_download", false);
        // 保存 目录
        chromePrefs.put("download.default_directory", "E:\\PDF");
        chromePrefs.put("savefile.default_directory", "E:\\PDF");
        //"marginsType": 2,#边距（2是最小值、0是默认）

        //"isLandscapeEnabled": True,  # 若不设置该参数，默认值为纵向
        chromePrefs.put("printing.print_preview_sticky_settings.appState", """
                {
                	"recentDestinations": [{
                		"id": "Save as PDF",
                		"origin": "local",
                		"account": ""
                	}],
                	"selectedDestinationId": "Save as PDF",
                	"version": 2,
                	"isHeaderFooterEnabled": false,
                                
                	"customMargins": {},
                	"marginsType": 0,
                	"scaling": 100,
                	"scalingType": 3,
                	"scalingTypePdf": 3,
                	"isLandscapeEnabled": false,
                	"isCssBackgroundEnabled": true,
                	"mediaSize": {
                		"height_microns": 297000,
                		"name": "ISO_A4",
                		"width_microns": 210000,
                		"custom_display_name": "A4"
                	}
                }
                """);
        chromePrefs.put("plugins.always_open_pdf_externally", true);
        options.setExperimentalOption("prefs", chromePrefs);



         options.addArguments("disable-infobars");
         options.addArguments("--print-to-pdf");

         // 重要 配置 页面 加载 速度
    options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        WebDriver driver = new ChromeDriver(options);




        driver.get(url);
        // 刷新页面
        driver.navigate().refresh();
        //实现滚轮向下滑动

        WebElement htmlElement = driver.findElement(By.tagName("html"));
        int h = htmlElement.getSize().getHeight() / 100 + 2;
        for (int i = 0; i < h; i++) {
            System.out.println("sleep 1s" + h);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(2)).implicitlyWait(Duration.ofSeconds(2)).scriptTimeout(Duration.ofSeconds(2));
            Thread.sleep(60);
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + (i * 90) + ")");
        }

        // Thread.sleep(1000);


        int height = Integer.parseInt((String) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight.toString()"));
        int width = Integer.parseInt((String) ((JavascriptExecutor) driver).executeScript("return document.body.scrollWidth.toString()"));

        driver.manage().window().setSize(new Dimension(width, height));
        Point point = new Point(0,0);
        driver.manage().window().setPosition(point);
// 注意：设定了浏览器固定大小后，浏览器打开后浏览器的位置可能会变到其他位置，因此可以使用设置刘浏览器的位置方法和设置浏览器的大小方法一起使用；

        driver.manage().window().maximize();


        ((JavascriptExecutor) driver).executeScript("document.title=\"my_test_file99923.pdf\";window.print();");


        driver.quit();
        driver.close();
        driver.close();
        driver.quit();
        driver.close();


    }


}
