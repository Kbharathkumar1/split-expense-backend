package com.splitexpense.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.splitexpense.entity.Settlement;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByGroupId(Long groupId);
    List<Settlement> findByPaidById(Long userId);
    List<Settlement> findByPaidToId(Long userId);
}