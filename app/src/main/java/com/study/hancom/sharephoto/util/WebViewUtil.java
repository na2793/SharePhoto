package com.study.hancom.sharephoto.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.webkit.WebView;

import com.study.hancom.sharephoto.R;
import com.study.hancom.sharephoto.model.Picture;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class WebViewUtil {
    private static String mDefaultHTMLData;

    private WebViewUtil() {
    }

    public static int getHeightByWidthForHD(int width) {
        return 1024 * width / 768;
    }

    public static int getWidthByWidthForHD(int height) {
        return 768 * height / 1024;
    }

    public static String getDefaultHTMLData(Context context) {
        if (mDefaultHTMLData == null) {
            try {
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open(context.getResources().getString(R.string.epubData_fileName_default_html));
                mDefaultHTMLData = FileUtil.fileToString(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mDefaultHTMLData;
    }

    public static void injectPictureElementByScript(WebView view, List<Picture> pictureList) {
        int pictureNum = pictureList.size();

        injectDivByScript(view, pictureNum);

        // inject data
        for (int i = 0; i < pictureNum; i++) {
            Picture eachPicture = pictureList.get(i);
            if (eachPicture != null) {
                WebViewUtil.injectImageByScript(view, "_" + (i + 1), eachPicture.getPath());
            } else {
                WebViewUtil.injectEmptyImageByScript(view, "_" + (i + 1));
            }
        }
    }

    public static void injectStyleByScript(WebView view, String stylePath) {
        view.loadUrl("javascript:(window.onload = function() {" +
                "var link = document.getElementById('_css_sharePhoto');" +
                    "if (link == null) {" +
                        "link = document.createElement('link');" +
                        "link.id = '_css_sharePhoto';" +
                        "link.rel = 'stylesheet';" +
                        "link.href = '" + stylePath + "';" +

                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "parent.appendChild(link);" +
                    "} else {" +
                        "link.href = '" + stylePath + "';" +
                    "}" +
                "})()");
    }

    private static void injectDivByScript(WebView view, int divNum) {
        view.loadUrl("javascript:(window.onload = function() {" +
                "var container = document.getElementById('container');" +
                "var oldDivNum = container.children.length;" +
                "var increment = oldDivNum - " + divNum + ";" +
                "if (increment > 0) {" +
                "for (var i = 0 ; i < increment ; i++) {" +
                "var eachDiv = document.getElementById('_' + (oldDivNum - i));" +
                "container.removeChild(eachDiv);" +
                "}" +
                "} else if (0 > increment) {" +
                "for (var i = increment ; i < 0 ; i++) {" +
                "var eachDiv = document.createElement('div');" +
                "eachDiv.setAttribute('id', '_' + (" + divNum + " + i + 1));" +
                "container.appendChild(eachDiv);" +
                "}" +
                "}" +
                "})()");
    }

    private static void injectImageByScript(WebView view, String elementId, String picturePath) {
        view.loadUrl("javascript:(window.onload = function() {" +
                "var target = document.getElementById('" + elementId + "');" +
                "target.setAttribute('style', \"background-image:url('" + picturePath + "')\");" +
                "})()");
    }

    private static void injectEmptyImageByScript(WebView view, String elementId) {
        view.loadUrl("javascript:(window.onload = function() {" +
                "var target = document.getElementById('" + elementId + "');" +
                "target.setAttribute('style', \"background-color:#CCCCCC;\");" +
                "})()");
    }
}