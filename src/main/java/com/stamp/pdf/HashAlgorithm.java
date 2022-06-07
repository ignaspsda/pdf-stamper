package com.stamp.pdf;

import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cms.CMSSignedDataGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * Operates with hash
 * 
 */
public enum HashAlgorithm 
{
	SHA1("SHA-1"),
	SHA256("SHA-256"),
	SHA384("SHA-384"),
	SHA512("SHA-512"),
	MD2("MD2"),
	MD5("MD5");
	
	private final String code;
	private static final Map<String, String>     encryptionAlgs = new HashMap<String, String>();
	private static final Map<String, String>     digestAlgs = new HashMap<String, String>(); 
	
	static
	{
	    encryptionAlgs.put(X9ObjectIdentifiers.id_dsa_with_sha1.getId(), "DSA");
	    encryptionAlgs.put(X9ObjectIdentifiers.id_dsa.getId(), "DSA");
	    encryptionAlgs.put(OIWObjectIdentifiers.dsaWithSHA1.getId(), "DSA");
	    encryptionAlgs.put(PKCSObjectIdentifiers.rsaEncryption.getId(), "RSA");
	    encryptionAlgs.put(PKCSObjectIdentifiers.sha1WithRSAEncryption.getId(), "RSA");
	    encryptionAlgs.put(TeleTrusTObjectIdentifiers.teleTrusTRSAsignatureAlgorithm.getId(), "RSA");
	    encryptionAlgs.put(X509ObjectIdentifiers.id_ea_rsa.getId(), "RSA");
	    encryptionAlgs.put(CMSSignedDataGenerator.ENCRYPTION_ECDSA, "ECDSA");
	    encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA2.getId(), "ECDSA");
	    encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA224.getId(), "ECDSA");
	    encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA256.getId(), "ECDSA");
	    encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA384.getId(), "ECDSA");
	    encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA512.getId(), "ECDSA");
	    encryptionAlgs.put(CMSSignedDataGenerator.ENCRYPTION_RSA_PSS, "RSAandMGF1");
	    encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3410_94.getId(), "GOST3410");
	    encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3410_2001.getId(), "ECGOST3410");
	    encryptionAlgs.put("1.3.6.1.4.1.5849.1.6.2", "ECGOST3410");
	    encryptionAlgs.put("1.3.6.1.4.1.5849.1.1.5", "GOST3410");

	    digestAlgs.put(PKCSObjectIdentifiers.md5.getId(), "MD5");
	    digestAlgs.put(PKCSObjectIdentifiers.md2.getId(), "MD2");
	    digestAlgs.put(OIWObjectIdentifiers.idSHA1.getId(), "SHA-1");
	    digestAlgs.put(NISTObjectIdentifiers.id_sha256.getId(), "SHA-256");
	    digestAlgs.put(NISTObjectIdentifiers.id_sha384.getId(), "SHA-384");
	    digestAlgs.put(NISTObjectIdentifiers.id_sha512.getId(), "SHA-512");
	    digestAlgs.put(PKCSObjectIdentifiers.sha1WithRSAEncryption.getId(), "SHA-1");
	    digestAlgs.put(PKCSObjectIdentifiers.sha256WithRSAEncryption.getId(), "SHA-256");
	    digestAlgs.put(PKCSObjectIdentifiers.sha384WithRSAEncryption.getId(), "SHA-384");
	    digestAlgs.put(PKCSObjectIdentifiers.sha512WithRSAEncryption.getId(), "SHA-512");
	}
	
	private HashAlgorithm(String code)
	{
		this.code = code;
	}
	
	/**
	 * Name of hash algorithm
	 * @return string representation of hash algorithm
	 */
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Calculates hash
	 * @param stream - input stream of which hash is calculated  
	 * @param hashAlg - hash algorithm 
	 * @return hashed digests
	 * @throws Exception
	 */
	public static byte[] createHash(InputStream stream, HashAlgorithm hashAlg) throws NoSuchAlgorithmException, IOException 
	{		
		MessageDigest messageDigest = MessageDigest.getInstance(hashAlg.getCode());
		
	    int n;
		byte buf[] = new byte[8192];
		while ((n = stream.read(buf)) > 0) 
		{
			messageDigest.update(buf, 0, n);
		}
	    return messageDigest.digest();
	}
		
	/**
	 * Gets HashAlgorythm by its name
	 * @param name - name of hash algorithm
	 * @return hash algorithm
	 * @throws Exception if name is not recognized
	 */
	public static HashAlgorithm getEnum(String name) throws Exception
	{
		name = getDigestAlgName(name);
		
		switch(name.toUpperCase())
		{
			case "SHA1":
			case "SHA-1":
				return HashAlgorithm.SHA1;
			case "SHA256":
			case "SHA-256":
				return HashAlgorithm.SHA256;
			case "SHA384":
			case "SHA-384":
				return HashAlgorithm.SHA384;
			case "SHA512":
			case "SHA-512":
				return HashAlgorithm.SHA512;
			case "MD2":
				return HashAlgorithm.MD2;
			case "MD5":
				return HashAlgorithm.MD5;
			default:
				throw new Exception(name + " not recognized or supported");
		}

	}
	
	private static String getDigestAlgName(String digestAlgOID) {
	    String algName = (String)digestAlgs.get(digestAlgOID);

	    if (algName != null)
	    {
	        return algName;
	    }

	    return digestAlgOID;
	}

	/**
	 * @deprecated not yet needed
	 * @param encryptionAlgOID
	 * @return
	 */
	private static String getEncryptionAlgName(String encryptionAlgOID) {
	    String algName = (String)encryptionAlgs.get(encryptionAlgOID);

	    if (algName != null)
	    {
	        return algName;
	    }

	    return encryptionAlgOID;
	}

}
