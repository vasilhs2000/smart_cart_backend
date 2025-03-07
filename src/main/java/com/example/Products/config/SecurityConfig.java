package com.example.Products.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.regex.Pattern;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final List<String> ALLOWED_IPS = List.of("192.168.1.4","109.242.204.160", "192.168.1.15", "192.168.1.16");  // Επιτρεπόμενες IP διευθύνσεις
    private static final List<String> ALLOWED_MACS = List.of("28:cd:c1:00:85:6d");  // Επιτρεπόμενες MAC διευθύνσεις
    private static final List<Pattern> ALLOWED_URI_PATTERNS = List.of(
        Pattern.compile("^/api/basket/[0-9]+$"),      // Μορφή URI όπως /api/basket/123
        Pattern.compile("^/api/basket/[0-9]+/[0-9]+/[0-9]+$"),
        Pattern.compile("^/api/productIds/[0-9]+$"),
        Pattern.compile("^/api/productsIDs"),
        Pattern.compile("^/api/basket/setTotalCost/[0-9]+/[0-9]+/(0|1)$"),
        Pattern.compile("^/api/basket/[0-9]+/[0-9]+$"),
        Pattern.compile("^/api/products+$"),
        Pattern.compile("^/api/offers+$"),
        Pattern.compile("^/api/basket/offers+$"),
        Pattern.compile("^/api/totalCost/[0-9]+$"),
        Pattern.compile("^/api/basket/confirm/[0-9]+/[0-2]+$"),
        Pattern.compile("^/api/basketConfirmation/[0-9]+$"),
        Pattern.compile("^/api/offers.*$")//edw prepei na to elejw giati prepei na brw 
                                                //to sugkekrimeno link kai na mhn ta epitrepew ola
        //Pattern.compile("^/api/offers/products\\?offerIds=([0-9]+(,[0-9]+)*)$")
        

    );

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .requiresChannel(channel -> channel.anyRequest().requiresSecure())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new RequestMatcher() {
                    @Override
                    public boolean matches(HttpServletRequest request) {
                        // Έλεγχος IP


                        String ip = request.getHeader("X-Forwarded-For");
                        if (ip == null || ip.isEmpty()) {
                            ip = request.getRemoteAddr(); // Επιστροφή στη διεύθυνση απόμακρου πελάτη αν δεν υπάρχει X-Forwarded-For
                        }
                        System.out.println("Request IP: " + ip);

                        // Έλεγχος μορφής URI
                        String uri = request.getRequestURI();
                        System.out.println("Request URI: " + uri);

                        // Επιτρέπει πρόσβαση μόνο αν η IP είναι επιτρεπόμενη και το URI ταιριάζει με κάποιο από τα πρότυπα
                        boolean isUriAllowed = ALLOWED_URI_PATTERNS.stream().anyMatch(pattern -> pattern.matcher(uri).matches());
                        String macAddress = request.getHeader("X-Device-MAC");
                        System.out.println("Request MAC: " + macAddress);
                        boolean isMacAllowed = macAddress != null && ALLOWED_MACS.contains(macAddress);
                        return ALLOWED_IPS.contains(ip) && isUriAllowed && isMacAllowed;
                    }
                }).permitAll()
                .anyRequest().denyAll())  // Απορρίπτει τα υπόλοιπα αιτήματα
            .httpBasic()
            .and()
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/basket/**", "/api/special/link/**"))
            .build();
    }



    // Password encoder to store passwords securely
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("Bill")
            .password(passwordEncoder.encode("210123456789bill!"))
            .roles("USER");
    }
}
