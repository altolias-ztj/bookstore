package com.bookstore.common.util;

import com.bookstore.admin.service.UserService;
import com.bookstore.common.entity.User;
import com.bookstore.common.entity.wx.*;
import okhttp3.HttpUrl;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletInputStream;
import java.io.InputStream;
import java.security.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class WxUtil {

    @Value("${woniu.wx.appid}")
    private String appid;

    @Value("${woniu.wx.secret}")
    private String secret;

    @Value("${woniu.wxurls.tokenUrl}")
    private String tokenUrl;

    @Value("${woniu.wxurls.telUrl}")
    private String telUrl;

    @Value("${woniu.wxurls.createOrderUrl}")
    private String createOrderUrl;

    @Value("${woniu.wx.mchid}")
    private String mchid;

    @Value("${woniu.wxurls.openidurl}")
    private String openidUrl;

    @Value("${woniu.wx.serialno}")
    private String serialNo;

    @Value("${woniu.wx.key}")
    private String mykey;

    @Value("${woniu.mp.tokenUrl}")
    private String mpTokenUrl;

    @Value("${woniu.mp.appId}")
    private String mpAppId;

    @Value("${woniu.mp.appSecret}")
    private String mpAppSecret;

    @Value("${woniu.mp.ticket_expire}")
    private int ticketExpire;

    @Value("${woniu.mp.ticketUrl}")
    private String ticketUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    public String getTelByCode(String code) {
        String token = getToken();
        String url = telUrl.replace("ACCESS_TOKEN", token);
        String data = "{\"code\":\"" + code + "\"}";
        TelNumber telNumber = restTemplate.postForObject(url, data, TelNumber.class);
        return telNumber.getPhone_info().getPhoneNumber();
    }

    public String getToken() {
        Object wxToken = redisTemplate.opsForValue().get("wxToken");
        if (wxToken == null) {
            return getTokenFromWxServer();
        }
        return wxToken.toString();
    }

    public String getTokenFromWxServer() {
        String url = tokenUrl.replace("APPID", appid).replace("SECRET", secret);
        Token token = restTemplate.getForObject(url, Token.class);
        redisTemplate.opsForValue().set("wxToken", token.getAccess_token(), 7200, TimeUnit.SECONDS);
        return token.getAccess_token();
    }

    public Map<String, Object> createOrder(Long orderId, String name, int amount, String payer) {
        //创建订单时的data
        String data = "{\n" +
                "\t\"mchid\": \"" + mchid + "\",\n" +
                "\t\"out_trade_no\": \"" + orderId + "\",\n" +
                "\t\"appid\": \"" + appid + "\",\n" +
                "\t\"description\": \"" + name + "\",\n" +
                "\t\"notify_url\": \"https://4582w6687r.yicp.fun/user/notify\",\n" +
                "\t\"amount\": {\n" +
                "\t\t\"total\": " + amount + ",\n" +
                "\t\t\"currency\": \"CNY\"\n" +
                "\t},\n" +
                "\t\"payer\": {\n" +
                "\t\t\"openid\": \"" + payer + "\"\n" +
                "\t}\n" +
                "}";
        System.out.println(data);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        //获取签名
        Map<String, Object> info = getSignature("POST", HttpUrl.parse(createOrderUrl), data);
        String signature = (String) info.get("signature");
        System.out.println("signature:" + signature);
        //在请求头中添加签名信息
        requestHeaders.add("Authorization", "WECHATPAY2-SHA256-RSA2048 " + signature);
        HttpEntity requestEntity = new HttpEntity(data, requestHeaders);

        PreOrderResult orderResult = restTemplate.postForObject(createOrderUrl, requestEntity, PreOrderResult.class);
        info.put("prepay_id", orderResult.getPrepay_id());
        return info;
    }


    public String getOpenIdByCode(String openidCode) {
        String url = openidUrl.replace("APPID", appid).replace("SECRET", secret).replace("CODE", openidCode);
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);
        return null;
    }

    /**
     * 生成签名
     *
     * @param method 请求方法  get/post
     * @param url    请求的地址
     * @param body   请求体
     * @return
     */
    Map<String, Object> getSignature(String method, HttpUrl url, String body) {
        Map<String, Object> info = new HashMap<>();
        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        info.put("nonceStr", nonceStr);
        long timestamp = System.currentTimeMillis() / 1000;
        info.put("timeStamp", timestamp + "");
        String message = buildMessage(method, url, timestamp, nonceStr, body);
        String signature = null;
        try {
            signature = sign(message.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String signInfo = "mchid=\"" + mchid + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + serialNo + "\","
                + "signature=\"" + signature + "\"";
        info.put("signature", signInfo);
        info.put("sign", signature);
        return info;
    }

    String sign(byte[] message) {
        Signature sign = null;
        try {
            sign = Signature.getInstance("SHA256withRSA");
            PrivateKey privateKey = getPrivateKey("C:\\1629812166_20230610_cert\\apiclient_key.pem");
            sign.initSign(privateKey);
            sign.update(message);
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }

    /**
     * 获取私钥。
     *
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)), "utf-8");
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (Exception e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    public String getPaySign(Map<String, Object> map) {
        String nonceStr = (String) map.get("nonceStr");
        String prepay_id = (String) map.get("prepay_id");
        String timeStamp = (String) map.get("timeStamp");
        String str = appid + "\n" +
                timeStamp + "\n" +
                nonceStr + "\n" +
                "prepay_id=" + prepay_id + "\n";
        return sign(str.getBytes());
    }

    static final int KEY_LENGTH_BYTE = 32;
    static final int TAG_LENGTH_BIT = 128;

    public String decryptToString(byte[] associatedData, byte[] nonce, String ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec key = new SecretKeySpec(mykey.getBytes(), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);
            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), "utf-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> parseXml(InputStream xml) {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            //文档对象
            Document document = reader.read(xml);
            //根节点  xml
            Element rootElement = document.getRootElement();
            //取出所有的子节点
            List<Element> elements = rootElement.elements();
            for (Element element : elements) {
                String name = element.getName();
                String value = element.getStringValue();
                map.put(name, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public Object getResponse(ServletInputStream is) {
        Map<String, String> map = parseXml(is);
        String msgType = map.get("msgType");
        switch (msgType) {
            case "text":
                return dealTextMsg(map);
            case "voice":
                return dealVoiceMsg(map);
            case "video":
                return dealVideoMsg(map);
            case "event":
                return dealScanMsg(map);
        }
        return null;
    }

    private Object dealScanMsg(Map<String, String> map) {
        String eventKey = map.get("EventKey");
        switch (eventKey) {
            case "login":
                return dealLogin(map);
        }
        return null;
    }

    private Object dealLogin(Map<String, String> map) {
        String userName = map.get("FormUserName");
        User user = userService.getByWxId(userName);
        if (user != null) {
            redisUtil.set(map.get("Ticket"), user, 300);
            TextMessage textMessage = new TextMessage(map);
            textMessage.setContent("登录成功");
            return textMessage;
        } else {
            TextMessage textMessage = new TextMessage(map);
            textMessage.setContent("你还没有绑定账号！");
            return textMessage;
        }
    }

    private Object dealVideoMsg(Map<String, String> map) {
        return null;
    }

    private Object dealVoiceMsg(Map<String, String> map) {
        return null;
    }

    private Object dealTextMsg(Map<String, String> map) {
        TextMessage textMessage = new TextMessage(map);
        textMessage.setContent(map.get("Content"));
        return textMessage;
    }

    public String getTicket() {
        TicketData ticketData = new TicketData(ticketExpire * 60, "login");
        String url = ticketUrl.replace("TOKEN", getMpToken());
        Map<String, Object> map = restTemplate.postForObject(url, ticketData, Map.class);
        String ticket = (String) map.get("ticket");
        return ticket;
    }

    private String getMpToken() {
        Object mpToken = redisTemplate.opsForValue().get("mpToken");
        if (mpToken == null) {
            return getMpTokenFromWxServer();
        }
        return mpToken.toString();
    }

    public String getMpTokenFromWxServer() {
        String url = tokenUrl.replace("APPID", mpAppId).replace("SECRET", mpAppSecret);
        Token token = restTemplate.getForObject(url, Token.class);
        redisTemplate.opsForValue().set("mpToken", token.getAccess_token(), 7200, TimeUnit.SECONDS);
        return token.getAccess_token();
    }
}
