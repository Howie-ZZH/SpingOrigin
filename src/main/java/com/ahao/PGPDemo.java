package com.ahao;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPUtil;

public class PGPDemo {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // 加载PGP公钥
        String publicKeyFilePath = "path/to/publickey.asc";
        PGPPublicKey publicKey = loadPublicKey(publicKeyFilePath);

        // 要加密的字符串
        String plainText = "Hello, World!";

        // 加密字符串
        byte[] encryptedData = encryptString(plainText, publicKey);

        // 打印加密后的数据
        System.out.println("Encrypted Data:");
        System.out.println(new String(encryptedData));
    }

    private static PGPPublicKey loadPublicKey(String publicKeyFilePath) throws Exception {
        InputStream keyInputStream = new FileInputStream(publicKeyFilePath);
        PGPPublicKey publicKey = PGPExampleUtil.readPublicKey(keyInputStream);
        keyInputStream.close();
        return publicKey;
    }

    private static byte[] encryptString(String plainText, PGPPublicKey publicKey) throws PGPException, Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArmoredOutputStream armoredOutputStream = new ArmoredOutputStream(outputStream);

        // 创建PGP加密数据生成器
        PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(PGPEncryptedDataGenerator.AES_256, true, new SecureRandom(), "BC");
        encryptedDataGenerator.addMethod(publicKey);

        // 打开输出流，准备写入加密后的数据
        OutputStream encryptedOutputStream = encryptedDataGenerator.open(armoredOutputStream, new byte[PGPUtil.BUFFER_SIZE]);

        // 创建压缩数据输出流
        PGPCompressedData compressedData = new PGPCompressedData(PGPCompressedData.ZIP);
        OutputStream compressedOutputStream = compressedData.open(encryptedOutputStream);

        // 创建字面数据包装流
        PGPLiteralData literalData = new PGPLiteralData(PGPLiteralData.TEXT, PGPUtil.getUniversalTime());
        OutputStream literalOutputStream = literalData.open(compressedOutputStream);

        // 将要加密的字符串写入字面数据流
        literalOutputStream.write(plainText.getBytes());

        // 关闭流
        literalOutputStream.close();
        literalData.close();
        compressedOutputStream.close();
        compressedData.close();
        encryptedOutputStream.close();
        armoredOutputStream.close();

        // 获取加密后的数据
        byte[] encryptedData = outputStream.toByteArray();
        outputStream.close();

        return encryptedData;
    }
}
