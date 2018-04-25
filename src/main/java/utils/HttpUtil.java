package utils;

import net.sf.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Author: youxingyang
 * @date: 2018/4/2 17:06
 */
public final class HttpUtil {

    /**
     * 默认请求重试次数, 3次
     */
    private static int RETRY_COUNT = 3;


    /**
     *
     */
    private List<Header> headers = null;

    /**
     *
     */
    private static final String DEFAULT_CONTENT_ENCODING = "UTF-8";


    /**
     *
     */
    public HttpUtil() {
        this.init();
    }

    /**
     * @param requestHeaders
     */
    public HttpUtil(Collection<? extends Header> requestHeaders) {
        if (requestHeaders instanceof List) {
            this.headers = (List) requestHeaders;
        }
        this.init();
    }


    /**
     *
     */
    private void init() {
        if (this.headers == null) {
            this.headers = new ArrayList<>();
        }
    }

    /**
     * 设置请求重试机制,默认为3次
     */
    private static HttpRequestRetryHandler requestRetryHandler = (IOException exception, int executionCount, HttpContext context) -> {
        if (executionCount >= RETRY_COUNT) {
            exception.printStackTrace();
            return false;
        }
        if (exception instanceof InterruptedIOException) {
            // Timeout
            exception.printStackTrace();
            return false;
        }
        if (exception instanceof UnknownHostException) {
            // Unknown host
            exception.printStackTrace();
            return false;
        }
        if (exception instanceof ConnectTimeoutException) {
            // Connection refused
            exception.printStackTrace();
            return false;
        }
        if (exception instanceof SSLException) {
            // SSL handshake exception
            exception.printStackTrace();
            return false;
        }
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent) {
            // Retry if the request is considered idempotent
            return true;
        }
        return false;
    };

    /**
     * @param headers
     */
    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    /**
     * @param header
     */
    public void setHeaders(Header header) {
        this.headers.add(header);
    }

    /**
     * @param key
     * @param value
     */
    public void setHeaders(String key, String value) {
        this.headers.add(this.getHeader(key, value));
    }

    /**
     * @param key
     * @param value
     * @return
     */
    private Header getHeader(String key, String value) {
        return new Header() {
            @Override
            public String getName() {
                return key;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
    }

    /**
     * 获取支持ssl的httpclient
     *
     * @return
     */
    private CloseableHttpClient getHttpsClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

            return HttpClients.custom()
                    .setRetryHandler(requestRetryHandler)
                    .setSSLSocketFactory(sslsf).build();

        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }

        return HttpClients.createDefault();
    }

    /**
     * 获取httpclient
     *
     * @return
     */
    private CloseableHttpClient getHttpClient() {

        return HttpClients.custom()
                .setRetryHandler(requestRetryHandler)
                .build();
    }

    /**
     * @param url
     * @param data
     * @param method
     * @param ssl
     * @return
     */
    public Optional<CloseableHttpResponse> sendRequest(String url, String data, String method, boolean ssl) throws Exception {

        CloseableHttpClient client;
        if (ssl) {
            client = this.getHttpsClient();
        } else {
            client = this.getHttpClient();
        }

        String requestMethon = method.toUpperCase();
        switch (requestMethon) {
            case "POST":
                return this.postRequest(client, url, data);
            case "GET":
                return this.getRequest(client, url, data);
            case "DELETE":
                return this.deleteRequest(client, url);
            case "PUT":
                return this.putRequest(client, url, data);
            default:
                //TODO 国际化
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                throw new Exception("Unsupported request method: " + method);
        }
    }

    /**
     * @param url
     * @param data
     * @param method
     * @return
     * @throws Exception
     */
    public Optional<CloseableHttpResponse> sendRequest(String url, String data, String method) throws Exception {

        return this.sendRequest(url, data, method, false);
    }

    /**
     * @param client
     * @param url
     * @param data
     * @return
     */
    private Optional<CloseableHttpResponse> postRequest(CloseableHttpClient client, String url, String data) {
        HttpPost postRequest = new HttpPost(url);
        if (!"".equals(data)) {
            StringEntity entity = new StringEntity(data, Charset.forName(DEFAULT_CONTENT_ENCODING));
            entity.setContentEncoding(DEFAULT_CONTENT_ENCODING);
            entity.setContentType("application/json");
            postRequest.setEntity(entity);
        }
        return this.doRequest(client, postRequest);
    }

    /**
     * @param client
     * @param url
     * @param data
     * @return
     */
    private Optional<CloseableHttpResponse> getRequest(CloseableHttpClient client, String url, String data) {
        String requestUrl;
        if (data != null && !"".equals(data)) {
            JSONObject parameters = JSONObject.fromObject(data);
            List<String> paramterList = new ArrayList<>();
            for (Object paramKey : parameters.keySet()) {
                paramterList.add(paramKey + "=" + parameters.get(paramKey));
            }
            requestUrl = url + "?" + "".join("&", paramterList);
        } else {
            requestUrl = url;
        }
        HttpGet get = new HttpGet(requestUrl);
        return this.doRequest(client, get);
    }

    /**
     * @param client
     * @param url
     * @return
     */
    private Optional<CloseableHttpResponse> deleteRequest(CloseableHttpClient client, String url) {
        HttpDelete deleteRequest = new HttpDelete(url);
        return this.doRequest(client, deleteRequest);
    }

    /**
     * @param client
     * @param url
     * @param data
     * @return
     */
    private Optional<CloseableHttpResponse> putRequest(CloseableHttpClient client, String url, String data) {
        HttpPut putRequest = new HttpPut(url);
        if (data != null && !"".equals(data)) {
            StringEntity entity = new StringEntity(data, Charset.forName(DEFAULT_CONTENT_ENCODING));
            entity.setContentEncoding(DEFAULT_CONTENT_ENCODING);
            entity.setContentType("application/json");
            putRequest.setEntity(entity);
        }
        return this.doRequest(client, putRequest);
    }

    /**
     * @param client
     * @param request
     * @return
     */
    private Optional<CloseableHttpResponse> doRequest(CloseableHttpClient client, HttpRequestBase request) {
        Optional<CloseableHttpResponse> responseResult;
        CloseableHttpResponse response = null;

        if (this.headers != null && !this.headers.isEmpty()) {
            request.setHeaders(this.headers.toArray(new Header[this.headers.size()]));
        }

        try {
            response = client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseResult = Optional.ofNullable(response);
        return responseResult;
    }
}
