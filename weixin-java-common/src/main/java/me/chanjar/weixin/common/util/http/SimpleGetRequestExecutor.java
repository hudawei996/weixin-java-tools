package me.chanjar.weixin.common.util.http;

import jodd.http.HttpConnectionProvider;
import jodd.http.ProxyInfo;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.apache.ApacheSimpleGetRequestExecutor;
import me.chanjar.weixin.common.util.http.hc.HttpComponentsSimpleGetRequestExecutor;
import me.chanjar.weixin.common.util.http.jodd.JoddHttpSimpleGetRequestExecutor;
import me.chanjar.weixin.common.util.http.okhttp.OkHttpProxyInfo;
import me.chanjar.weixin.common.util.http.okhttp.OkHttpSimpleGetRequestExecutor;
import okhttp3.OkHttpClient;

import java.io.IOException;

/**
 * 简单的GET请求执行器.
 * 请求的参数是String, 返回的结果也是String
 *
 * @author Daniel Qian
 */
public abstract class SimpleGetRequestExecutor<H, P> implements RequestExecutor<String, String> {
  protected RequestHttp<H, P> requestHttp;

  public SimpleGetRequestExecutor(RequestHttp<H, P> requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, String data, ResponseHandler<String> handler, WxType wxType) throws WxErrorException, IOException {
    handler.handle(this.execute(uri, data, wxType));
  }

  @SuppressWarnings("unchecked")
  public static RequestExecutor<String, String> create(RequestHttp<?, ?> requestHttp) {
    switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new ApacheSimpleGetRequestExecutor(
          (RequestHttp<org.apache.http.impl.client.CloseableHttpClient, org.apache.http.HttpHost>) requestHttp);
      case JODD_HTTP:
        return new JoddHttpSimpleGetRequestExecutor((RequestHttp<HttpConnectionProvider, ProxyInfo>) requestHttp);
      case OK_HTTP:
        return new OkHttpSimpleGetRequestExecutor((RequestHttp<OkHttpClient, OkHttpProxyInfo>) requestHttp);
      case HTTP_COMPONENTS:
        return new HttpComponentsSimpleGetRequestExecutor(
          (RequestHttp<org.apache.hc.client5.http.impl.classic.CloseableHttpClient, org.apache.hc.core5.http.HttpHost>) requestHttp);
      default:
        throw new IllegalArgumentException("不支持的http执行器类型：" + requestHttp.getRequestType());
    }
  }

  protected String handleResponse(WxType wxType, String responseContent) throws WxErrorException {
    WxError error = WxError.fromJson(responseContent, wxType);
    if (error.getErrorCode() != 0) {
      throw new WxErrorException(error);
    }

    return responseContent;
  }
}
