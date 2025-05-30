package com.jaeseung.springbootiocdi.discount;

import com.jaeseung.springbootiocdi.member.Grade;
import com.jaeseung.springbootiocdi.member.Member;

public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1000; // 1000원 할인

    @Override
    public int discount(Member member, int price) {

        if(member.getGrade() == Grade.VIP){
            return discountFixAmount;
        } else {
            return 0;
        }

    }
}

