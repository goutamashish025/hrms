package org.example.hrms.team.repository;

import org.example.hrms.team.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findByTeam_Id(Long teamId);

    boolean existsByTeam_IdAndUser_Id(Long teamId, Long userId);

}