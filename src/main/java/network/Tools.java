package network;

import net.sf.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.Map;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Random;
public class Tools {
    public static String getMessagePropertyValue(String fullStr, String itemName)
    {
        return getMidString(fullStr, "<" + itemName + ">", "</>", 0);
    }

    public static String getMidString(String fullStr, String former, String behind, int beginFrom)
    {
        int positionA = fullStr.indexOf(former, beginFrom);
        int positionB = fullStr.indexOf(behind, positionA + 1);
        if (positionA < 0 || positionB < 0) return "";
        int len = positionB - positionA - former.length();
        int s = positionA + former.length();
        return fullStr.substring(s, s + len);
    }
    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static   String httpRequest(String url, Map<String, String> params, boolean isHttps) throws Exception {
        // 构建请求参数
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;

        String sendString = "";
        JSONObject json = JSONObject.fromObject(params);
        System.out.println("发送报文:" + json.toString());
        sendString = json.toString();

        System.out.println("ERP连接:" + url);
        System.out.println("发送给ERP信息:" + sendString);

        try {
            if(isHttps){
                trustAllHosts();
            }

            URL url2 = new URL(url);

            URLConnection urlCon;
            if(isHttps){
                urlCon = (HttpsURLConnection) url2.openConnection();
                ((HttpsURLConnection)urlCon).setHostnameVerifier(DO_NOT_VERIFY);
                urlCon.setDoOutput(true);
                urlCon.setDoInput(true);
                ((HttpsURLConnection)urlCon).setRequestMethod("POST");
                urlCon.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            }else{
                urlCon = (HttpURLConnection) url2.openConnection();
                urlCon.setDoOutput(true);
                urlCon.setDoInput(true);
                ((HttpURLConnection)urlCon).setRequestMethod("POST");
                urlCon.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            }


            // 发送POST请求必须设置如下两行
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            OutputStream os = urlCon.getOutputStream();
            //参数是键值队  , 不以"?"开始
            os.write(sendString.getBytes());
            //os.write("googleTokenKey=&username=admin&password=5df5c29ae86331e1b5b526ad90d767e4".getBytes());
            os.flush();
            // 发送请求参数
            //out.print(a);
            // flush输出流的缓冲
            //out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(urlCon.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }


            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 加密
     * @param datasource byte[]
     * @param password String
     * @return byte[]
     */
    public static  byte[] encrypt(byte[] datasource, String password) {
        try{
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        }catch(Throwable e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解密
     * @param src byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }


}
