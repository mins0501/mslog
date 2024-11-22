package com.mslog.repository;


import com.mslog.domain.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    public Optional<Member> findByEmailAndPassword(String email, String password);
    public Optional<Member> findByEmail(String email);

}
