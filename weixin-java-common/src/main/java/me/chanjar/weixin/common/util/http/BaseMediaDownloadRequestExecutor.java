package me.chanjar.weixin.common.util.http;

import jodd.http.HttpConnectionProvider;
import jodd.http.ProxyInfo;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.apache.ApacheMediaDownloadRequestExecutor;
import me.chanjar.weixin.common.util.http.hc.HttpComponentsMediaDownloadRequestExecutor;
import me.chanjar.weixin.common.util.http.jodd.JoddHttpMediaDownloadRequestExecutor;
import me.chanjar.weixin.common.util.http.okhttp.OkHttpMediaDownloadRequestExecutor;
import me.chanjar.weixin.common.util.http.okhttp.OkHttpProxyInfo;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;

/**
 * 下载媒体文件请求执行器.
 * 请求的参数是String, 返回的结果是File
 * 视频文件不支持下载
 *
 * @author Daniel Qian
 */
public abstract class BaseMediaDownloadRequestExecutor<H, P> implements RequestExecutor<File, String> {
  protected RequestHttp<H, P> requestHttp;
  protected File tmpDirFile;

  public BaseMediaDownloadRequestExecutor(RequestHttp<H, P> requestHttp, File tmpDirFile) {
    this.requestHttp = requestHttp;
    this.tmpDirFile = tmpDirFile;
  }

  @Override
  public void execute(String uri, String data, ResponseHandler<File> handler, WxType wxType) throws WxErrorException, IOException {
    handler.handle(this.execute(uri, data, wxType));
  }

  @SuppressWarnings("unchecked")
  public static RequestExecutor<File, String> create(RequestHttp<?, ?> requestHttp, File tmpDirFile) {
    switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new ApacheMediaDownloadRequestExecutor(
          (RequestHttp<org.apache.http.impl.client.CloseableHttpClient, org.apache.http.HttpHost>) requestHttp, tmpDirFile);
      case JODD_HTTP:
        return new JoddHttpMediaDownloadRequestExecutor((RequestHttp<HttpConnectionProvider, ProxyInfo>) requestHttp, tmpDirFile);
      case OK_HTTP:
        return new OkHttpMediaDownloadRequestExecutor((RequestHttp<OkHttpClient, OkHttpProxyInfo>) requestHttp, tmpDirFile);
      case HTTP_COMPONENTS:
        return new HttpComponentsMediaDownloadRequestExecutor(
          (RequestHttp<org.apache.hc.client5.http.impl.classic.CloseableHttpClient, org.apache.hc.core5.http.HttpHost>) requestHttp, tmpDirFile);
      default:
        throw new IllegalArgumentException("不支持的http执行器类型：" + requestHttp.getRequestType());
    }
  }

}
