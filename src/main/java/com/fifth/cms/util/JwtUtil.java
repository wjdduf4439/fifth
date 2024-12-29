package com.fifth.cms.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys; // 추가된 부분

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils; // 추가된 부분

public class JwtUtil {

	//비밀키는 많이 길어야 한다, 256bit 이상
	private static final String PROJECT_NAME = "ljy_springboot_fourth_project";
    private static final String SECRET_KEY = PROJECT_NAME + "_jwttoken_secretkey";

	/*
	토큰 생성을 하게 되면 다음 형식의 문자열이 생성된다. 각 부분은 Base64 인코딩된 상태입니다. 
	-헤더 : eyJhbGciOiJIUzM4NCJ9.
		 JWT의 타입과 사용된 서명 알고리즘을 나타냅니다. 주어진 토큰의 헤더는 다음과 같습니다:
		 {
			"alg": "HS384"
		 }
	-페이로드 : eyJzdWIiOiJ3amRkdWY0NDM5IiwiaWF0IjoxNzI2NjA2MzQxLCJleHAiOjE3MjY2NDIzNDF9.
		 JWT의 페이로드는 클레임(claims)을 포함하는 부분입니다. 주어진 토큰의 페이로드는 다음과 같습니다:
		 {
			"sub": "wjdduf4439",
			"iat": 1726606341,
			"exp": 1726642341
		 }
	-시그니처 : 5E1RysD5kt_8bQM5fdwMfNq7rKHDUvd4_WzoLXBbhm0vq-qE0RyP2KkFcJDuMFOP
		 JWT의 서명은 헤더와 페이로드를 결합하여 생성된 토큰의 유효성을 검증하는 데 사용됩니다. 주어진 토큰의 서명은 다음과 같습니다:
		 {
			"alg": "HS384"
		 }

	 */

    public static String generateAccessToken(String username, String authorities, String ip, String userAgent) {

        Map<String, Object> claims = new HashMap<>();
		claims.put("authorities", authorities);
		claims.put("ip", DigestUtils.sha384Hex(ip)); 
		claims.put("userAgent", userAgent);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 유효
				//.setExpiration(new Date(System.currentTimeMillis() + 1000 * 30 * 1)) // 30초 유효
                .setHeaderParam("PROJECT_NAME", PROJECT_NAME) // 새로운 키 추가
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS384)
                .compact();
    }

	public static String generateRefreshToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", "refresh");
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 180)) // 180일 유효
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS384)
				.compact();
	}

    public static Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes()) 
					// 서명 검증 부분, 서명은 JWT의 마지막 부분으로, 헤더와 페이로드를 결합한 후 비밀 키를 사용하여 생성됩니다. 서명은 토큰이 변조되지 않았음을 보장합니다.
					// JWT 토큰은 양방향 암호화를 사용하지 않습니다. JWT 토큰은 서명 기반으로, 토큰의 무결성을 검증하기 위해 서명을 사용합니다.
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) { // 수정된 부분
            throw new RuntimeException("Invalid token");
        }
    }

	public static boolean isJwtTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder() 
				.setSigningKey(SECRET_KEY.getBytes()) // 비밀 키를 설정하세요
				.build() // 빌드
				.parseClaimsJws(token)
				.getBody(); // 수정된 부분

            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            // 기타 오류 처리
            return false;
        }
    }
}