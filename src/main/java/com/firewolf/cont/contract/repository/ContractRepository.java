package com.firewolf.cont.contract.repository;

import com.firewolf.cont.contract.entity.Contract;
import com.firewolf.cont.user.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract,Long> {
    Page<Contract> findByMemberOrderByCreatedDateDesc(Member member, Pageable pageable);
}
