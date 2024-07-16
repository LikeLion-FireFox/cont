package com.firewolf.cont.contract.repository;

import com.firewolf.cont.contract.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract,Long> {
}
