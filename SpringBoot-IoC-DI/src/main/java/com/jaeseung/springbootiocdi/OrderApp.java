package com.jaeseung.springbootiocdi;

import com.jaeseung.springbootiocdi.member.Grade;
import com.jaeseung.springbootiocdi.member.Member;
import com.jaeseung.springbootiocdi.member.MemberService;
import com.jaeseung.springbootiocdi.order.Order;
import com.jaeseung.springbootiocdi.order.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {

    public static void main(String[] args) {

        // 1. 직접 객체를 생성한다.
        // MemberService memberService = new MemberServiceImpl();
        // OrderService orderService = new OrderServiceImpl();

        // 2. AppConfig를 통해 객체를 생성하고 주입한다.
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();

        // 3. Spring 컨테이너를 통해 객체를 주입한다. -> 즉 DI, IoC 컨테이너 역할을 Spring이 해준다.
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean(MemberService.class);
        OrderService orderService = applicationContext.getBean(OrderService.class);


        long memberId = 1L;

        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);
        System.out.println("order = " + order);
    }

}
