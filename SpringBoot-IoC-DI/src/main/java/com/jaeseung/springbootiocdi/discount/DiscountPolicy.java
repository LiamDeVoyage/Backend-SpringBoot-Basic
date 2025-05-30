package com.jaeseung.springbootiocdi.discount;

import com.jaeseung.springbootiocdi.member.Member;

public interface DiscountPolicy {

    /**
     * @return 할인 대상 금액
     */

    int discount(Member member, int price);

}
