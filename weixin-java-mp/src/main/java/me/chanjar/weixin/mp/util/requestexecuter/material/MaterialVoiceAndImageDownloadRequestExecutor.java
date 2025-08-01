package me.chanjar.weixin.mp.util.requestexecuter.material;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jodd.http.HttpConnectionProvider;
import jodd.http.ProxyInfo;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.RequestExecutor;
import me.chanjar.weixin.common.util.http.RequestHttp;
import me.chanjar.weixin.common.util.http.ResponseHandler;
import me.chanjar.weixin.common.util.http.okhttp.OkHttpProxyInfo;
import okhttp3.OkHttpClient;

public abstract class MaterialVoiceAndImageDownloadRequestExecutor<H, P> implements RequestExecutor<InputStream, String> {
  protected RequestHttp<H, P> requestHttp;
  protected File tmpDirFile;

  public MaterialVoiceAndImageDownloadRequestExecutor(RequestHttp<H, P> requestHttp, File tmpDirFile) {
    this.requestHttp = requestHttp;
    this.tmpDirFile = tmpDirFile;
  }

  @Override
  public void execute(String uri, String data, ResponseHandler<InputStream> handler, WxType wxType) throws WxErrorException, IOException {
    handler.handle(this.execute(uri, data, wxType));
  }

  @SuppressWarnings("unchecked")
  public static RequestExecutor<InputStream, String> create(RequestHttp<?, ?> requestHttp, File tmpDirFile) {
    switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new MaterialVoiceAndImageDownloadApacheHttpRequestExecutor(
          (RequestHttp<org.apache.http.impl.client.CloseableHttpClient, org.apache.http.HttpHost>) requestHttp, tmpDirFile);
      case JODD_HTTP:
        return new MaterialVoiceAndImageDownloadJoddHttpRequestExecutor((RequestHttp<HttpConnectionProvider, ProxyInfo>) requestHttp, tmpDirFile);
      case OK_HTTP:
        return new MaterialVoiceAndImageDownloadOkhttpRequestExecutor((RequestHttp<OkHttpClient, OkHttpProxyInfo>) requestHttp, tmpDirFile);
      case HTTP_COMPONENTS:
        return new MaterialVoiceAndImageDownloadHttpComponentsRequestExecutor(
          (RequestHttp<org.apache.hc.client5.http.impl.classic.CloseableHttpClient, org.apache.hc.core5.http.HttpHost>) requestHttp, tmpDirFile);
      default:
        throw new IllegalArgumentException("不支持的http执行器类型：" + requestHttp.getRequestType());
    }
  }


}
