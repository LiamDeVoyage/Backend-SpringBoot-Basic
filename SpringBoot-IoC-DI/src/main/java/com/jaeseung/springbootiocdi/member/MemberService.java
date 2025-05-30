package com.jaeseung.springbootiocdi.member;

public interface MemberService {

    void join(Member member);

    Member findMember(Long memberId);

}
