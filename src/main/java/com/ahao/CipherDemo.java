package com.ahao;

import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;


@Component
class CipherDemo {

    public static final String RULE = "zhangzh11...";


    void encodeAndeDecode() throws Exception {
        String encrypt = encrypt("zhangzh");
        String decrypt = decrypt(encrypt);
        System.out.println("zhangzh:" + decrypt);
    }

    // 加密
    public static String encrypt(String content) throws Exception {
        // 1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        // 2.根据ecnodeRULEs规则初始化密钥生成器
        // 生成一个128位的随机源,根据传入的字节数组
        // 防止linux下 随机生成key
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(RULE.getBytes());
        System.out.println(Arrays.toString(RULE.getBytes(StandardCharsets.UTF_8)));
        // 根据密钥初始化密钥生成器
        keygen.init(256, secureRandom);
        // 3.产生原始对称密钥
        SecretKey original_key = keygen.generateKey();
        // 4.获得原始对称密钥的字节数组
        byte[] raw = original_key.getEncoded();
        System.out.println("rawbyte" + Arrays.toString(raw));
        // 5.根据字节数组生成AES密钥
        SecretKey key = new SecretKeySpec(raw, "AES");
        // 6.根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
        byte[] byte_encode = content.getBytes("utf-8");
        // 9.根据密码器的初始化方式--加密：将数据加密
        byte[] byte_AES = cipher.doFinal(byte_encode);
        // 10.将加密后的数据转换为字符串
        String AES_encode = new String(new BASE64Encoder().encode(byte_AES));
        // 11.将字符串返回
        return AES_encode;
    }

    // 解密
    public static String decrypt(String content) throws Exception {
        content = content.replaceAll(" ", "+");
        // 1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        // 2.根据ecnodeRULEs规则初始化密钥生成器
        // 生成一个128位的随机源,根据传入的字节数组
        // 防止linux下 随机生成key
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.generateSeed(16);
        secureRandom.setSeed(RULE.getBytes());
        // 根据密钥初始化密钥生成器
        keygen.init(256, secureRandom);
        // 3.产生原始对称密钥
        SecretKey original_key = keygen.generateKey();
        // 4.获得原始对称密钥的字节数组
        byte[] raw = original_key.getEncoded();
        System.out.println("rawbyte" + Arrays.toString(raw));
        // 5.根据字节数组生成AES密钥
        SecretKey key = new SecretKeySpec(raw, "AES");
        // 6.根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 8.将加密并编码后的内容解码成字节数组
        byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
        //解密
        byte[] byte_decode = cipher.doFinal(byte_content);
        String AES_decode = new String(byte_decode, "utf-8");
        return AES_decode;
    }

    public static String encryptKeyByString(String content) throws Exception {
        // 1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        // 2.根据ecnodeRULEs规则初始化密钥生成器
        // 生成一个128位的随机源,根据传入的字节数组
        // 防止linux下 随机生成key
        // 根据密钥初始化密钥生成器

        // 5.根据字节数组生成AES密钥
        SecretKey key = new SecretKeySpec(RULE.getBytes(), "AES");
        // 6.根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
        byte[] byte_encode = content.getBytes("utf-8");
        // 9.根据密码器的初始化方式--加密：将数据加密
        byte[] byte_AES = cipher.doFinal(byte_encode);
        // 10.将加密后的数据转换为字符串
        String AES_encode = new String(new BASE64Encoder().encode(byte_AES));
        // 11.将字符串返回
        return AES_encode;
    }

    // 解密
    public static String decryptKeyByString(String content) throws Exception {


        // 5.根据字节数组生成AES密钥
        SecretKey key = new SecretKeySpec(RULE.getBytes(), "AES");
        // 6.根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 8.将加密并编码后的内容解码成字节数组
        byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
        //解密
        byte[] byte_decode = cipher.doFinal(byte_content);
        String AES_decode = new String(byte_decode, "utf-8");
        return AES_decode;
    }

}
