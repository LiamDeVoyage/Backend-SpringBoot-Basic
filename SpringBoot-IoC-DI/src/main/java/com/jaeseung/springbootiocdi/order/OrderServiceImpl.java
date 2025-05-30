package com.jaeseung.springbootiocdi.order;

import com.jaeseung.springbootiocdi.discount.DiscountPolicy;
import com.jaeseung.springbootiocdi.member.Member;
import com.jaeseung.springbootiocdi.member.MemberRepository;

public class OrderServiceImpl implements OrderService {

    /**
     * 아래의 경우는 추상(인터페이스)에만 의존하게 되어 있으므로 DIP, OCP 위반한다.
     * 왜냐하면, DiscountPolicy를 변경해야하는 경우 ServiceImpl의 아래 코드 처럼 변경해야하기 때문
     * private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
     * private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
     */
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;


    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;

    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {

        Member member = memberRepository.findById(memberId);

        // SRP 단일 책임 원칙이 잘 지켜저서, 할인이 변경이 된다면 Service는 변경하지 않아도 된다.
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);

    }
}
