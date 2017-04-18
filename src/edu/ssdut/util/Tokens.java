package edu.ssdut.util;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;

public final class Tokens {
	
    public static final String JWT_SECRET = "SSDUT12sxjd345";
    public static final long JWT_TTL = 60*60*1000;  //millisecond
	public static final String JWT_ISSUER = "SSDUT_SXJD";
    
    public static String createJWT(String type, String subject) {
    	return createJWT(type, JWT_ISSUER, subject, JWT_TTL, JWT_SECRET);
    }
    
    public static Claims parseJWT(String jwt){
    	return parseJWT(jwt, JWT_SECRET);
    }
    
    public static boolean verityJWT(String jwt, String type,  String subject) {
    	return verifyJWT(jwt, type, JWT_ISSUER, subject, JWT_SECRET);
    }
    
    public static String verityJWT(String jwt, String type) {
    	return verifyJWT(jwt, type, JWT_ISSUER, JWT_SECRET);
    }
    
    public static String verifyJWT(String jwt, String type, String issuer,  String secret) {
    	try{
	    	Claims claims = Jwts.parser()      
	       .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
	       .parseClaimsJws(jwt).getBody();
	    	
	    	if ( !(claims.getId().equals(type) 
	    			&& claims.getIssuer().equals(issuer)) )
	    	return null;
	    	
	    	long nowMillis = System.currentTimeMillis();
		    Date now = new Date(nowMillis); 
			Date exp = claims.getExpiration();
			if (now.before(exp))
			    return claims.getSubject();
		    return null;
		    
		}catch (SignatureException e){
		    return null;
		}catch (IncorrectClaimException e) {
			return null;
		}catch(MalformedJwtException e) {
			return null;
		}catch(ExpiredJwtException e){
			return null;
		}
    }
    
    public static boolean verifyJWT(String jwt, String type, String issuer, String subject, String secret) {
    	try{
	    	Claims claims = Jwts.parser()      
	       .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
	       .parseClaimsJws(jwt).getBody();
	    	
	    	if ( !(claims.getId().equals(type) 
	    			&& claims.getIssuer().equals(issuer)
	    				&& claims.getSubject().equals(subject)) )
	    	return false;
	    	
	    	long nowMillis = System.currentTimeMillis();
		    Date now = new Date(nowMillis); 
			Date exp = claims.getExpiration();
			if (now.before(exp))
			    return true;
		    return false;
		    
		}catch (SignatureException e){
		    return false;
		}catch (IncorrectClaimException e) {
			return false;
		}catch(MalformedJwtException e) {
			return false;
		}
    }
    
	//Sample method to construct a JWT
	public static String createJWT(String type, String issuer, String subject, long ttlMillis, String secret) {
	 
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	    
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(type)
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
	
	//Sample method to validate and read the JWT
	public static Claims parseJWT(String jwt, String secret) {
		Claims claims = null;
		
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    try{
	    	claims = Jwts.parser()      
	       .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
	       .parseClaimsJws(jwt).getBody();
		}catch (SignatureException e){
		    return null;
		}catch (IncorrectClaimException e) {
			return null;
		}
	    return claims;
	}
	
	
}
