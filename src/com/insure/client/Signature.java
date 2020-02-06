package com.insure.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Signature {
	public Signature () {
	}

	private String generateHash (String docContent) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		return Base64.getEncoder().encodeToString(digest.digest(docContent.getBytes("UTF-8")));
	}

	private String encryptHash (String fileName, String hash) throws java.lang.Exception {
		AsymEncryptPriv cipher = new AsymEncryptPriv();
		PrivateKey privKey = cipher.getPrivate(fileName);
		return cipher.encryptText(hash, privKey);
	}

	private String decryptHash (String fileName, String encryptedHash) throws java.lang.Exception {
		AsymDecryptPub cipher = new AsymDecryptPub();
		PublicKey pubKey = cipher.getPublic(fileName);
		return cipher.decryptText(encryptedHash, pubKey);
	}

	public String createSignature (String fileName, String docContent) throws java.lang.Exception {
		//create hash

		// create hash(message) and encrypt hash
		String hash = generateHash(docContent);

		// encrypt hash
		String encryptedHash = encryptHash(fileName, hash);

		return encryptedHash;

	}

	//decrypt hash and compare hash obtained from the message
	// validate integrity and authenticity
	public void validateSignature (String fileName, String encryptedHash, String docContent) throws TamperedDocumentException_Exception, java.lang.Exception {
		// Decrypt encrypted hash
		String decryptedHash = decryptHash(fileName, encryptedHash);

		// Create hash of document
		String docHash = generateHash(docContent);

		// Validate
		if (!docHash.equals(decryptedHash))
			throw new TamperedDocumentException_Exception("This document was tampered!", new TamperedDocumentException());
	}
}


