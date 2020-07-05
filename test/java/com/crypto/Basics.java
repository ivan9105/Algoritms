package com.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;

import static java.security.SecureRandom.getInstanceStrong;
import static java.security.Security.addProvider;
import static java.security.Security.getProviders;
import static javax.crypto.Cipher.*;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class Basics {
    //TODO можно эту хню потихоньку учить тем более реальных примеров более чем достаточно походить по коммитам поковырять
    //TODO linked hashmap api подучи

    //TODO nio vs io с проверкой на блокировку данных https://www.baeldung.com/java-io-vs-nio
    //TODO https://medium.com/@nilasini/java-nio-non-blocking-io-vs-io-1731caa910a2
    //TODO - Кеши (Ehcache/Redis/Hazelcast/Ignite/GridGain/etc); поизучать епта поковырять что то одно redis maybe?
    ///TODO поковырять простые кеши memorize из проектов что нибудь такое поковырять хорошо
    @Test
    public void showProviders() {
        addProvider(new BouncyCastleProvider());

        for (Provider p : getProviders()) {
            System.out.println(p.getName());
        }
    }

    @Test
    public void simpleHash() throws Exception {
        MessageDigest digester = MessageDigest.getInstance("SHA-512");
        byte[] input = "Secret string".getBytes();
        byte[] salt = new byte[16];
        getInstanceStrong().nextBytes(salt);
        digester.update(salt);
        byte[] digest = digester.digest(input);
        System.out.println(printHexBinary(digest));
    }

    @Test
    public void simpleSymmetricEncryption() throws Exception {
//        AES/ECB/PKCS5Padding
//        AES - алгоритм
//        ECB - режим шифрования
//        PKCS5Padding - отступ (на каждый блок)
        String text = "secret!!secret!!secret!!secret!!";

        //алгоритм
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);
        Key key = keygen.generateKey();

        //настраиваем шифр
        String transformation = "AES/ECB/PKCS5Padding";
        Cipher cipher = getInstance(transformation);
        cipher.init(ENCRYPT_MODE, key);

        //шифруем
        byte[] encrypted = cipher.doFinal(text.getBytes());
        System.out.println(printHexBinary(encrypted));

        //дешифруем
        cipher.init(DECRYPT_MODE, key);
        String result = new String(cipher.doFinal(encrypted));
        System.out.println(result);

        //ECB делает повторы в блоках дешифрованного сообщения
    }

    @Test
    public void simpleSymmetricEncryptionWithoutRepeat() throws Exception {
//        AES/CBC/PKCS5Padding
        //need cbc
        SecureRandom random = new SecureRandom();
        byte[] rnd = new byte[16];
        random.nextBytes(rnd);
        IvParameterSpec ivSpec = new IvParameterSpec(rnd);


        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);
        Key key = keygen.generateKey();
        // CBC
        String text = "secret!!secret!!secret!!secret!!";
        String transformation = "AES/CBC/PKCS5Padding";
        Cipher cipher = getInstance(transformation);
        cipher.init(ENCRYPT_MODE, key, ivSpec);
        byte[] enc = cipher.doFinal(text.getBytes());
        System.out.println(printHexBinary(enc));
        // Decrypt
        cipher.init(DECRYPT_MODE, key, ivSpec);
        String result = new String(cipher.doFinal(enc));
        System.out.println(result);
    }

    @Test
    public void simpleAsymmetricEncryption() throws Exception {
        //шифрует только клиент публичным ключом, при этом у него нет возможности дешифровать
        //читает только получатель - носитель private ключа

        //https://ru.wikipedia.org/wiki/RSA
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String text = "Hello!";

        //encryption
        Cipher cipher = getInstance("RSA");
        cipher.init(ENCRYPT_MODE, publicKey);
        byte[] data = cipher.doFinal(text.getBytes());

        //decryption
        cipher.init(DECRYPT_MODE, keyPair.getPrivate());
        byte[] result = cipher.doFinal(data);
        System.out.println(new String(result));
    }

    @Test
    public void signatureBasic() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        generator.initialize(2048, random);
        KeyPair keyPair = generator.generateKeyPair();

        //цифровая подпись
        Signature dsa = Signature.getInstance("SHA256withRSA");
        dsa.initSign(keyPair.getPrivate());


        String text = "Hello!";

        //подписание
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] data = cipher.doFinal(text.getBytes());
        dsa.update(data);
        //подписанный текс
        byte[] signature = dsa.sign();

        //проверка
        dsa.initVerify(keyPair.getPublic());
        dsa.update(data);
        boolean verifies = dsa.verify(signature);
        System.out.println("Signature is ok: " + verifies);

        //дефишровка
        if (verifies) {
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] result = cipher.doFinal(data);
            System.out.println(new String(result));
        }
    }
}
