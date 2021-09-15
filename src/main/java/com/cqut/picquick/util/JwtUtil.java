package com.cqut.picquick.util;

import com.cqut.picquick.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : JwtUtil
 * @date : 2021/9/14 15:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class JwtUtil {

    @Value("${jwt.signing-key}")
    private String SIGNING_KEY;
    /**
     * 过期时间
     **/
    private static final long EXPIRATION = 3600L;//单位为秒




    public String createJWT(User user){

        //指定签名的时候使用的签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //过期时间
        Date exp = new Date(nowMillis + EXPIRATION * 1000);

        //创建一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //保存在Payload（有效载荷）中的内容
                .claim("id",user.getId())
                .claim("username",user.getUsername())
                .claim("account",user.getAccount())
                .claim("password",user.getPassword())
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //设置过期时间
                .setExpiration(exp)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, SIGNING_KEY);
        String token = builder.compact();
        return token ;

    }

    public String createJWT(Map<String, Object> claims){

        String jwt = this.createJWT(new Date(System.currentTimeMillis() + EXPIRATION * 1000), claims);
        return jwt ;
    }


    /**
     * 用户登录成功后生成Jwt
     * 使用Hs256算法
     *
     * @param exp    jwt过期时间
     * @param claims 保存在Payload（有效载荷）中的内容
     * @return token字符串
     */
    public String createJWT(Date exp, Map<String, Object> claims) {
        //指定签名的时候使用的签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //创建一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //保存在Payload（有效载荷）中的内容
                .setClaims(claims)
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //设置过期时间
                .setExpiration(exp)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, SIGNING_KEY);
        return builder.compact();
    }

    /**
     * 解析token，获取到Payload（有效载荷）中的内容，包括验证签名，判断是否过期
     *
     * @param token
     * @return
     */
    public Claims parseJWT(String token) {
        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(SIGNING_KEY)
                //设置需要解析的token
                .parseClaimsJws(token).getBody();

        return claims;
    }





}