package test;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Test {
	
	//Sample method to validate and read the JWT
	private static void parseJWT(String jwt) {
		String a = "string";
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()         
	       .setSigningKey(DatatypeConverter.parseBase64Binary(a))
	       .parseClaimsJws(jwt).getBody();
	    System.out.println("ID: " + claims.getId());
	    System.out.println("Subject: " + claims.getSubject());
	    System.out.println("Issuer: " + claims.getIssuer());
	    System.out.println("Expiration: " + claims.getExpiration());
	}

	//Sample method to construct a JWT
		private static String createJWT(String id, String issuer, String subject, long ttlMillis) {
		 
		    //The JWT signature algorithm we will be using to sign the token
		    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		 
		    long nowMillis = System.currentTimeMillis();
		    Date now = new Date(nowMillis);
		 
		    String a = "string";
		    
		    //We will sign our JWT with our ApiKey secret
		    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(a);
		    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		 
		    //Let's set the JWT Claims
		    JwtBuilder builder = Jwts.builder().setId(id)
		                                .setIssuedAt(now)
		                                .setSubject(subject)
		                                .setIssuer(issuer)
		                                .signWith(signatureAlgorithm, signingKey);
		 
		    //if it has been specified, let's add the expiration
		    if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		        Date exp = new Date(expMillis);
		        builder.setExpiration(exp);
		    }
		 
		    //Builds the JWT and serializes it to a compact, URL-safe string
		    return builder.compact();
		}
	
	public static void main(String[] args) {
		String id = "1";
		String issuer = "sxjd";
		String subject = "steven";
		long ttlMillis = 10000l;
		String jwt = createJWT(id, issuer, subject, ttlMillis);
		System.out.println(jwt);
		parseJWT(jwt);
		
	}
		// We need a signing key, so we'll create one just for this example. Usually
		// the key would be read from your application configuration instead.
//		Key key = MacProvider.generateKey();
//		System.out.println(key.getEncoded());
//		
//		String compactJws = Jwts.builder()
//		  .setSubject("Joe")
//		  .signWith(SignatureAlgorithm.HS512, key)
//		  .compact();
//		System.out.println(compactJws);
//		
//		System.out.println(Jwts.parser().setSigningKey(key).
//				parseClaimsJws(compactJws).getBody().getSubject().equals("Joe"));
//		
//		try {
//
//		    Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws);
//
//		    //OK, we can trust this JWT
//
//		} catch (SignatureException e) {
//
//		    //don't trust the JWT!
//		}
//	}

}
