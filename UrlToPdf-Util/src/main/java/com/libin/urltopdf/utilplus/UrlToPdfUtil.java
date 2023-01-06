package com.libin.urltopdf.utilplus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


/**
 * @author libin9ioak
 * @date 2022-12-08
 */
public class UrlToPdfUtil {
    protected static final Logger log = LoggerFactory.getLogger(UrlToPdfUtil.class);

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();

        log.info("currentTimeMillis = " + currentTimeMillis);
         // String toPdf = urlToPdf( "https://mp.weixin.qq.com/s/TSyKR22WPaErw7N-CkmNVA", 666546, String.valueOf(99));
        int toPdf = urlToPdf("https://mp.weixin.qq.com/s/SMIqUruxrL10WV9k5W4e7Q", 666546, String.valueOf(99),"E:/test");

        String pdfurl = null;
     /*   try {

            pdfurl = OSSUtil.uploadUrlToPdf(new ByteArrayInputStream(InputStream2ByteArray("E:\\PDF\\my_pdf_file.pdf")), "abcddwrehi-hh.pdf", "23232");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


        long currentTimeMilli2 = System.currentTimeMillis();

        log.info("(currentTimeMilli2-currentTimeMillis) = " + (currentTimeMilli2 - currentTimeMillis));

        log.info("toPd-furl: = " + toPdf);

    }

    /**
     * @param url      网络url
     * @param userId   用户
     * @param fileName 文件名
     * @return
     */
    public static int urlToPdf(String url, int userId, String fileName,String saveUri) {
        RemoteWebDriver driver = null;
        try {
            System.setProperty("java.awt.headless", "false");
            System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            // url = "https://mp.weixin.qq.com/s/3Oj_dOt55Io76BR_mSIOgw";
            //url = "https://mp.weixin.qq.com/s/AeaQdRTfwvR0JdkfdJk04g";
            // url = "https://mp.weixin.qq.com/s/izdCiDpZl0UJ0LPR2rg5hQ";
            // url = "https://mp.weixin.qq.com/s/TSyKR22WPaErw7N-CkmNVA";

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
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--disable-dev-shm-usage");

            // 关闭日志
            options.addArguments("--disable-logging");

            //添加UA
            options.addArguments("user-agent='MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1'");


            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("download.prompt_for_download", false);
            // 保存 目录
            chromePrefs.put("download.default_directory", saveUri);
            chromePrefs.put("savefile.default_directory", saveUri);
            options.addArguments("--disable-infobars");
            options.addArguments("--print-to-pdf");
            options.addArguments("–incognito");
            // 重要 配置 页面 加载 速度
            options.setPageLoadStrategy(PageLoadStrategy.EAGER);

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
                    	"marginsType": 2,
                    	"scaling": 100,
                    	
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
            ChromeDriverService driverService = ChromeDriverService.createDefaultService();
            driver = new ChromeDriver(driverService, options);


            Map<String, Object> commandParams = new HashMap<>();
            commandParams.put("cmd", "Page.setDownloadBehavior");
            Map<String, String> params = new HashMap<>();
            params.put("behavior", "allow");
            params.put("downloadPath",saveUri);
            //log.info(params.get("downloadPath"));
            commandParams.put("params", params);
            ObjectMapper objectMapper = new ObjectMapper();
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            String command = objectMapper.writeValueAsString(commandParams);

            String u = driverService.getUrl().toString() + "/session/" + driver.getSessionId() + "/chromium/send_command";
            HttpPost request = new HttpPost(u);
            request.addHeader("content-type", "application/json");
            request.setEntity(new StringEntity(command));
            httpClient.execute(request);


            driver.get(url);
            // 刷新页面
            //driver.navigate().refresh();

            //实现滚轮向下滑动
            //TODO 核心代码,下方数字配置改一个都将导致转换失败

            WebElement htmlElement = driver.findElement(By.tagName("html"));
            int h = htmlElement.getSize().getHeight() / 100 + 2;
            if (url.contains("weixin")) {
                for (int i = 0; i < h; i++) {
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(6)).implicitlyWait(Duration.ofSeconds(6)).scriptTimeout(Duration.ofSeconds(6));
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + (i * 90) + ")");
                    Thread.sleep(30);
                }
            } else {

                for (int i = 0; i < h; i++) {
                    Thread.sleep(10);
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + (i * 60) + ")");
                }
            }


            Point point = new Point(0, 0);
            driver.manage().window().setPosition(point);
            // 注意：设定了浏览器固定大小后，浏览器打开后浏览器的位置可能会变到其他位置，因此可以使用设置刘浏览器的位置方法和设置浏览器的大小方法一起使用；

            //document.title 文件名称 可传递参数 配置
            ((JavascriptExecutor) driver).executeScript("document.title=\"my_pdf_file.pdf\";window.print();document.close();window.close();return document.readyState;");

            if (!url.contains("weixin")) {
                Thread.sleep(20);

            }


        } catch (InterruptedException | IOException e) {
            log.error(e.getMessage());
            return 1;


        } finally {
            //driverService.close();
            driver.quit();
        }
        return 0;

    }


    private static byte[] InputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    private static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

}
