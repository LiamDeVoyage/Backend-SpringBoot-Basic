package com.jaeseung.springbootiocdi;

import com.jaeseung.springbootiocdi.member.Grade;
import com.jaeseung.springbootiocdi.member.Member;
import com.jaeseung.springbootiocdi.member.MemberService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {


    public static void main(String[] args) {
        // 1. 직접 객체를 생성해서 주입한다.
        // MemberService memberService = new MemberServiceImpl();

        // 2. AppConfig를 통해 객체를 주입한다.
        // AppConfig appConfig = new AppConfig();
        // MemberService memberService = appConfig.memberService();

        // 3. Spring 컨테이너를 이용하여 객체를 주입한다.
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean(MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);

        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());
    }

}
