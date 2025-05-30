package com.jaeseung.springbootiocdi.service;

import com.jaeseung.springbootiocdi.AppConfig;
import com.jaeseung.springbootiocdi.member.Grade;
import com.jaeseung.springbootiocdi.member.Member;
import com.jaeseung.springbootiocdi.member.MemberService;
import com.jaeseung.springbootiocdi.order.Order;
import com.jaeseung.springbootiocdi.order.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

//    MemberService memberService = new MemberServiceImpl();
//    OrderService orderService = new OrderServiceImpl();

    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }

    @Test
    void createOrder() {

        //given

        Member member = new Member(1L,"memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        Assertions.assertThat(member).isEqualTo(findMember);

        //when
        Order order = orderService.createOrder(1L,"itemA",10000);

        //then
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

}
