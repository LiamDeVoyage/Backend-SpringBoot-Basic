package com.jaeseung.springbootiocdi.member;

public class MemberServiceImpl implements MemberService {

    /**
     * 아래의 경우는 추상(인터페이스)에만 의존하게 되어 있으므로 DIP, OCP 위반한다.
     * 왜냐하면, MemberRepository를 변경해야하는 경우 ServiceImpl의 아래 코드 처럼 변경해야하기 때문
     * private final MemberRepository memberRepository  = new MemoryMemberRepository();
     * private final MemberRepository memberRepository  = new DbMemberRepository();
     *
     * 이를 해결하기 위해 AppConfig를 사용하여 생성자를 주입해주면, memberRespository 인터페이스에만 의존하게 된다.
     */
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
