package test.temporary;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.springframework.jdbc.core.JdbcTemplate;

public class testKey {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KeyPairGenerator userKey = null;
		try {
			userKey = KeyPairGenerator.getInstance("RSA");

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userKey.initialize(512);
		KeyPair usrKey = userKey.genKeyPair();
		java.security.Key publicKey = usrKey.getPublic();
		java.security.Key privateKey = usrKey.getPrivate();

		String usersPrivateKey= ""; 
		String usersPublicKey = "";
		try {
			KeyFactory users = KeyFactory.getInstance("RSA");
			try {
				RSAPublicKeySpec pubKey = (RSAPublicKeySpec) users
						.getKeySpec(publicKey, RSAPublicKeySpec.class);
				RSAPrivateKeySpec privKey = (RSAPrivateKeySpec) users
						.getKeySpec(privateKey, RSAPrivateKeySpec.class);

				
				usersPrivateKey = privKey.getModulus()+""+privKey.getPrivateExponent();
				usersPublicKey  = pubKey.getModulus()+""+pubKey.getPublicExponent();
				
				System.out.println(usersPrivateKey);
				System.out.println(usersPublicKey);
				System.out.println(usersPrivateKey.length());
				System.out.println(usersPublicKey.length());
				
				//commented by shivam, I dont uderstand why we need to store txt file. Need to verify with sunit.
//				try {
//					PrintWriter pubOut = new PrintWriter(new FileWriter(
//							"public.key"));
//					pubOut.println(pubKey.getModulus());
//					pubOut.println(pubKey.getPublicExponent());
//					pubOut.close();
//				} catch (FileNotFoundException e6) {
//					// TODO Auto-generated catch block
//					e6.printStackTrace();
//				} catch (IOException e6) {
//					// TODO Auto-generated catch block
//					e6.printStackTrace();
//				}
//
//				try {
//					PrintWriter priOut = new PrintWriter(new FileWriter(
//							"private.txt"));
//					priOut.println(privKey.getModulus());
//					priOut.println(privKey.getPrivateExponent());
//					priOut.close();
//				} catch (FileNotFoundException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				} catch (IOException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}

			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
