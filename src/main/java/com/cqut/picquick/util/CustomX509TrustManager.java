package com.cqut.picquick.util;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author : HK意境
 * @ClassName : CustomX509TrustManager
 * @date : 2021/9/15 15:59
 * @description : 信任管理器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class CustomX509TrustManager implements X509TrustManager {

    // 检查客户端证书
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    // 检查服务器端证书
    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    // 返回受信任的X509证书数组
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}
