package com.jaeseung.springbootiocdi.member;

import java.util.HashMap;
import java.util.Map;

public class MemoryMemberRepository implements MemberRepository {

    // static 필드는 클래스의 모든 인스턴스가 공유하는 필드이기 때문에, 서로 다른 인스턴스가 같은 데이터를 접근할 수 있게 됩니다.
    // 실제 데이터베이스 없이 데이터를 공유하는 방법을 보여줄 때 유용합니다.
    // 하지만, 실무에서는 static 필드를 통한 데이터 공유보다는 Spring에서 제공하는 DI(Dependency Injection)를 활용하여 동일한 인스턴스를 서비스 전역에서 사용하도록 설계하는 것이 권장됩니다.
    private static Map<Long,Member> store = new HashMap<>();

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
