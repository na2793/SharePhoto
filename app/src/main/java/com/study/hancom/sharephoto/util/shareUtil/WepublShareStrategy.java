package com.study.hancom.sharephoto.util.shareUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public abstract class WepublShareStrategy<T> extends AsyncTaskShareStrategy<T> implements IShareStrategy {
    private HttpURLConnection mHttpURLConnection;

    @Override
    public final void perform(File file, List params) {
        execute(file, params);
    }

    @Override
    protected final T validate(File file, List params) {
        return null;
    }

    @Override
    protected final T sendRequest(File file, List params) throws IOException {
        URL url = new URL("http://s-api.wepubl.com/api/listlibrary"); // s-api.wepubl.com/member/rlogin
        mHttpURLConnection = (HttpURLConnection) url.openConnection();
        mHttpURLConnection.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)

        mHttpURLConnection.setUseCaches(false); //캐시를 사용하지 않게 설정
        mHttpURLConnection.setDoInput(true); //input을 사용하도록 설정 (default : true)
        mHttpURLConnection.setDoOutput(true); //output을 사용하도록 설정 (default : false)

        //mHttpURLConnection.setConnectTimeout(1000); //타임아웃 시간 설정 (default : 무한대기)

        DataOutputStream os = null;
        try {
            os = new DataOutputStream(mHttpURLConnection.getOutputStream());

            os.writeBytes("userKey=dd3724a3-bbe7-423b-9d1c-109119540c50"
            + "&page=1"
            + "&pagesize=20");
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.close();
            }
        }

        return null;
    }

    @Override
    protected final T receiveResponse(StringBuilder result) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(mHttpURLConnection.getInputStream(), "UTF-8")); //캐릭터셋 설정
            String line;
            while ((line = br.readLine()) != null) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
            if (mHttpURLConnection != null) {
                mHttpURLConnection.disconnect();
            }
        }

        return null;
    }
}
