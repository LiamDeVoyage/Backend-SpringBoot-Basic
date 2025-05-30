package com.jaeseung.springbootiocdi;

import com.jaeseung.springbootiocdi.discount.DiscountPolicy;
import com.jaeseung.springbootiocdi.discount.RateDiscountPolicy;
import com.jaeseung.springbootiocdi.member.MemberRepository;
import com.jaeseung.springbootiocdi.member.MemberService;
import com.jaeseung.springbootiocdi.member.MemberServiceImpl;
import com.jaeseung.springbootiocdi.member.MemoryMemberRepository;
import com.jaeseung.springbootiocdi.order.OrderService;
import com.jaeseung.springbootiocdi.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // AppConfig는 구체 클래스를 선택한다.
    // 배역에 맞는 담당 배우를 선택한다. 애플리케이션이 어떻게 동작해야 할 지 전체 구성을 책임진다.

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    @Bean
    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }



}

//public class AppConfig {
//
//    // AppConfig는 구체 클래스를 선택한다.
//    // 배역에 맞는 담당 배우를 선택한다. 애플리케이션이 어떻게 동작해야 할 지 전체 구성을 책임진다.
//
//    public MemberService memberService() {
//    return new MemberServiceImpl(new MemoryMemberRepository());
//    }
//
//    public OrderService orderService() {
//        return new OrderServiceImpl(
//                new MemoryMemberRepository(),
//                new FixDiscountPolicy()
//        );
//    }
//
//}
