package io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets.utils;

import io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets.HazelcastMonitorServerBuilder;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Utility class for initializing an {@link SSLContext}.
 * <p>
 * Can be used to create a context to use with {@link HazelcastMonitorServerBuilder#setSslContext(SSLContext)}.
 * @see HazelcastMonitorServerBuilder
 */
public abstract class SSLFactoryUtils {
    /**
     * Creates a {@link SSLContext} by loading a JKS file.
     * <p>
     * A self-signed certificate for localhost can be generated with:
     * <pre>
     * keytool -genkey -keyalg RSA -validity 3650 -keystore "keystore.jks" -storepass "storepassword" -keypass "keypassword" -alias "default" -dname "CN=127.0.0.1, OU=MyOrgUnit, O=MyOrg, L=MyCity, S=MyRegion, C=MyCountry"
     * </pre>
     *
     * @param keyStorePath path to the jks file
     * @param storePassword password for the store
     * @param keyPassword password for the key
     * @return the initialized SSL context ({@code TLS} instance)
     * @throws IOException if an error occurs
     * @throws GeneralSecurityException if an error occurs
     */
    public static SSLContext loadJKS(final String keyStorePath, final String storePassword, final String keyPassword) throws IOException, GeneralSecurityException {
        final KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(new File(keyStorePath)), storePassword.toCharArray());

        final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keyPassword.toCharArray());
        final TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return sslContext;
    }

    /**
     * Creates a {@link SSLContext} by loading the PEM files. The private key must not be encrypted.
     * <p>
     * A self-signed certificate for localhost can be generated with:
     * <pre>
     * openssl req -x509 -nodes -subj '/CN=localhost' -days 365000 -newkey rsa:4096 -keyout key.pem -out cert.pem
     * </pre>
     *
     * @param publicKeyPath to public key PEM file (typically {@code cert.pem}).
     * @param privateKeyPath to private key PEM file (typically {@code key.pem}), must not be encrypted.
     * @param password password to use for the generated KeyStore, cannot be null
     * @return the initialized SSL context ({@code TLS} instance)
     * @throws IOException if an error occurs
     * @throws GeneralSecurityException if an error occurs
     */
    public static SSLContext loadPEM(final String publicKeyPath, final String privateKeyPath, final String password) throws IOException, GeneralSecurityException {
        final byte[] certBytes = parseDERFromPEM(getBytes(new File(publicKeyPath)), "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----" );
        final byte[] keyBytes = parseDERFromPEM(getBytes(new File(privateKeyPath)), "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----" );

        final X509Certificate cert = generateCertificateFromDER(certBytes);
        final RSAPrivateKey key = generatePrivateKeyFromDER(keyBytes);

        final KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(null);
        keystore.setCertificateEntry( "cert-alias", cert);
        keystore.setKeyEntry( "key-alias", key, password.toCharArray(), new Certificate[]{ cert });

        final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keystore, password.toCharArray());

        final KeyManager[] km = kmf.getKeyManagers();
        final SSLContext context = SSLContext.getInstance("TLS");
        context.init(km, null, null);
        return context;
    }

    private static byte[] parseDERFromPEM(final byte []pem, final String beginDelimiter, final String endDelimiter) {
        final String data = new String(pem);
        String []tokens = data.split(beginDelimiter);
        tokens = tokens[1].split(endDelimiter);
        return DatatypeConverter.parseBase64Binary(tokens[0]);
    }

    /**
     * Creates an instance of {@link RSAPrivateKey} from the raw key bytes.
     * <p>The key must not be encrypted.
     *
     * @param keyBytes the bytes of the private key.
     * @return the RSAPrivateKey instance
     * @throws InvalidKeySpecException if an error occurs
     * @throws NoSuchAlgorithmException if an error occurs
     */
    private static RSAPrivateKey generatePrivateKeyFromDER(final byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        final KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey)factory.generatePrivate(spec);
    }

    /**
     * Generates a X509 certificate from the raw bytes.
     *
     * @param certBytes the raw bytes of the certificate
     * @return the X509 certificate
     * @throws CertificateException if an error occurs
     */
    private static X509Certificate generateCertificateFromDER(final byte[] certBytes) throws CertificateException {
        final CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    /**
     * Reads the file contents.
     *
     * @param file the file to read
     * @return the file contents, as a byte array.
     * @throws IOException if an error occurs
     */
    private static byte[] getBytes(final File file) throws IOException {
        try (final FileInputStream fis = new FileInputStream(file)) {
            final byte[] bytesArray = new byte[(int)file.length()];
            fis.read(bytesArray);
            return bytesArray;
        }
    }
}
