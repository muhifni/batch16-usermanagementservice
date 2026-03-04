package com.batch16.usermanagementservice.util;

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret-key}") private val jwtSecreteKey: String,
    @Value("\${jwt.expired-in}") private val expiredIn: String,
) {
    fun generateJwt(
            userId: Int,
            roleName: String?
    ): String {
        // Menentukan waktu expired token
        val exp = Date(System.currentTimeMillis() + expiredIn.toLong())
        // Meng-enkripsi secret-key
        val signingKey = Keys.hmacShaKeyFor(jwtSecreteKey.toByteArray())
        val token = Jwts.builder()  // Mulai membentuk token
            // Isi paylaod dari jwt ini secara default optional
            .setIssuer("Gateway")                   // Untuk yg membuat token
            .setSubject(userId.toString())          // Token dibuat untuk siapa
            .claim("authority", roleName ?: "user") // Membuat property dengan nama authority dan value role
            .claim("userId", userId.toString())     // Membuat property dengan userId
            .signWith(signingKey, SignatureAlgorithm.HS256) // Mengisikan algoritma enkripsi
            .setExpiration(exp)                     // Waktu token akan expired
            .compact()                              // Finalisasi token

            return token
        }
}
