package com.libin.urltopdf.util;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.ruiyun.jvppeteer.core.Puppeteer;
import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.core.browser.BrowserFetcher;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.options.LaunchOptions;
import com.ruiyun.jvppeteer.options.LaunchOptionsBuilder;
import com.ruiyun.jvppeteer.options.PDFOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * @author libin
 */
public class UrlToPdfUtil_jvppeteer {
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
        //自动下载，第一次下载后不会再下载
        BrowserFetcher.downloadIfNotExist(null);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("--no-sandbox");
        arrayList.add("--disable-setuid-sandbox");

        //生成pdf必须在无厘头模式下才能生效
        LaunchOptions options = new LaunchOptionsBuilder().withArgs(arrayList).withHeadless(true).build();

        Browser browser = Puppeteer.launch(options);
        Page page = browser.newPage();
        page.goTo(url);

        Thread.sleep(10);


        PDFOptions pdfOptions = new PDFOptions();

        pdfOptions.setPath("/test1.pdf");
        page.pdf(pdfOptions);
        page.close();
        browser.close();


    }


}
