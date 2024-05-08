package com.example.BidZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auctionappBidZone/registerUser", "/auctionappBidZone/userlogin","/auctionappBidZone/addcategories","/auctionappBidZone/createNewAuctions","/auctionappBidZone/bidForAuctionItem","/auctionappBidZone/validateUserEmailForResetPassword","/auctionappBidZone/sendmailToUser","/auctionappBidZone/verifyGetOtp").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auctionappBidZone/profile", "/auctionappBidZone/profile/{profileId}","/auctionappBidZone/categories","/auctionappBidZone/getAllauctions","/auctionappBidZone/getAuctiondetails","/auctionappBidZone/getUserDetails"
                        ,"/auctionappBidZone/getAuctionsByCategory","/auctionappBidZone/getTheAllBidsUnderTheAuction","/auctionappBidZone/getmyAllAuctions","/auctionappBidZone/getMyAllLisingSpesificOrder","/auctionappBidZone/myBids").permitAll()
                        .requestMatchers(HttpMethod.PATCH,"/auctionappBidZone/profile").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/auctionappBidZone/updateAuction","/auctionappBidZone/resetPassword").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
