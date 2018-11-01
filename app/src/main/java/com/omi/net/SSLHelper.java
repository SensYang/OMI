package com.omi.net;

import com.omi.OmiApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by SensYang on 2017/03/11 16:54
 */

public class SSLHelper {
    private final static String CLIENT_PRI_KEY = "client.bks";
    private final static String TRUSTSTORE_PUB_KEY = "truststore.bks";
    private final static String CLIENT_BKS_PASSWORD = "omi2501";
    private final static String TRUSTSTORE_BKS_PASSWORD = "omi2501";
    private final static String KEYSTORE_TYPE = "BKS";
    private final static String PROTOCOL_TYPE = "TLS";
    private final static String CERTIFICATE_FORMAT = "X509";
    private static KeyStore keyStore;
    private static KeyStore trustStore;

    public static HostnameVerifier verifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public synchronized static SSLSocketFactory getDoubleSSLCertifcation() {
        try {
            if (keyStore == null || trustStore == null) {
                // 服务器端需要验证的客户端证书，其实就是客户端的keystore
                keyStore = KeyStore.getInstance(KEYSTORE_TYPE);// 客户端信任的服务器端证书
                trustStore = KeyStore.getInstance(KEYSTORE_TYPE);//读取证书
                InputStream ksIn = OmiApplication.getInstance().getAssets().open(CLIENT_PRI_KEY);
                InputStream tsIn = OmiApplication.getInstance().getAssets().open(TRUSTSTORE_PUB_KEY);//加载证书
                keyStore.load(ksIn, CLIENT_BKS_PASSWORD.toCharArray());
                trustStore.load(tsIn, TRUSTSTORE_BKS_PASSWORD.toCharArray());
                ksIn.close();
                tsIn.close();
            }
            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(CERTIFICATE_FORMAT);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(CERTIFICATE_FORMAT);
            trustManagerFactory.init(trustStore);
            keyManagerFactory.init(keyStore, CLIENT_BKS_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
