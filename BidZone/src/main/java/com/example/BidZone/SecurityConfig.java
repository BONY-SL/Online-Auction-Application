package com.example.BidZone;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
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
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {
        FilterRegistrationBean<OpenEntityManagerInViewFilter> registrationBean = new FilterRegistrationBean<>();
        OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "**", "/styles/**", "/js_haddle/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/auctionappBidZone/registerUser", "/auctionappBidZone/userlogin","/auctionappBidZone/addcategories","/auctionappBidZone/createNewAuctions","/auctionappBidZone/bidForAuctionItem","/auctionappBidZone/validateUserEmailForResetPassword","/auctionappBidZone/sendmailToUser","/auctionappBidZone/verifyGetOtp","/auctionappBidZone/messages/send","/auctionappBidZone/savepayment").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auctionappBidZone/profile", "/auctionappBidZone/profile/{profileId}","/auctionappBidZone/categories","/auctionappBidZone/getAllauctions","/auctionappBidZone/getAuctiondetails","/auctionappBidZone/getUserDetails"
                        ,"/auctionappBidZone/getAuctionsByCategory","/auctionappBidZone/getTheAllBidsUnderTheAuction","/auctionappBidZone/getmyAllAuctions","/auctionappBidZone/getMyAllLisingSpesificOrder","/auctionappBidZone/getAllUsers","/auctionappBidZone/getNotifyMessageAddNewAuction"
                        ,"/auctionappBidZone/messages/response","/auctionappBidZone/myBids","/auctionappBidZone/getpaymentdetails/{username}","/auctionappBidZone/getUsersMessageHistory").permitAll()
                        .requestMatchers(HttpMethod.PATCH,"/auctionappBidZone/profile","/auctionappBidZone/deleteAuction").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/auctionappBidZone/updateAuction","/auctionappBidZone/resetPassword").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
