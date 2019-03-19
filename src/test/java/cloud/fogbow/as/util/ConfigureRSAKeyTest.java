package cloud.fogbow.as.util;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.HomeDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public abstract class ConfigureRSAKeyTest {

    protected String privateKeyPath;
    protected String publicKeyPath;


    protected void init() throws FogbowException, GeneralSecurityException, IOException {
        String keysPath = HomeDir.getPath();
        publicKeyPath = keysPath + "public.key";
        privateKeyPath = keysPath + "private.key";

        KeyPair keyPair = CryptoUtil.generateKeyPair();
        PublicKey publicKey= keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        saveKeyToFile(publicKeyPath, CryptoUtil.savePublicKey(publicKey));
        saveKeyToFile(privateKeyPath, CryptoUtil.savePrivateKey(privateKey));
    }

    private static void saveKeyToFile(String path, String key) throws IOException {File file = new File(path);
        file.getParentFile().mkdirs();
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(key.getBytes());
        stream.close();
    }

    private static void deleteKey(String path){
        File file = new File(path);
        file.delete();
    }

    protected void tearDown() {
        deleteKey(publicKeyPath);
        deleteKey(privateKeyPath);
    }
}
