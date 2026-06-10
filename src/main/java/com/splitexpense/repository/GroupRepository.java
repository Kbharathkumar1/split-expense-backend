package com.splitexpense.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.splitexpense.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByCreatedById(Long userId); //ఒక user create చేసిన అన్ని groups తీసుకొస్తుంది
}