package com.jaeseung.springbootiocdi.service;

import com.jaeseung.springbootiocdi.AppConfig;
import com.jaeseung.springbootiocdi.member.Grade;
import com.jaeseung.springbootiocdi.member.Member;
import com.jaeseung.springbootiocdi.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    //MemberService memberService = new MemberServiceImpl();
    MemberService memberService;


    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }

    @Test
    void join() {

        // given
        Member member = new Member(1L, "memberA", Grade.VIP);

        // when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        // then
        Assertions.assertThat(member).isEqualTo(findMember);
        Assertions.assertThat(member.getGrade()).isEqualTo(Grade.VIP);

    }

}
