package com.geekster.Blogging.API.service.hashUtility;

import jakarta.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypter {
    public static String passwordEncrypt(String password) throws NoSuchAlgorithmException
    {
        MessageDigest md5=MessageDigest.getInstance("MD5");
        md5.update(password.getBytes());
        byte[] digested=md5.digest();
        return DatatypeConverter.printHexBinary(digested);
    }

}
