package org.example.hrms.team.service;

import jakarta.transaction.Transactional;
import org.example.hrms.exception.ResourceNotFoundException;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.example.hrms.team.dto.TeamMemberRequestDTO;
import org.example.hrms.team.dto.TeamMemberResponseDTO;
import org.example.hrms.team.dto.TeamRequestDTO;
import org.example.hrms.team.dto.TeamResponseDTO;
import org.example.hrms.team.entity.Team;
import org.example.hrms.team.entity.TeamMember;
import org.example.hrms.team.repository.TeamMemberRepository;
import org.example.hrms.team.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    public TeamService(
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            UserRepository userRepository) {

        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userRepository = userRepository;
    }

    public TeamResponseDTO createTeam(TeamRequestDTO dto) {

        Team team = new Team();
        team.setTeamName(dto.getTeamName());
        team.setDescription(dto.getDescription());

        return toResponseDTO(teamRepository.save(team));
    }

    public List<TeamResponseDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addMembers(Long teamId, TeamMemberRequestDTO dto) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        for (Long userId : dto.getUserIds()) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            boolean exists =
                    teamMemberRepository.existsByTeam_IdAndUser_Id(teamId, userId);

            if (!exists) {

                TeamMember member = new TeamMember();
                member.setTeam(team);
                member.setUser(user);
                member.setIsManager(false);

                teamMemberRepository.save(member);
            }
        }
    }

    public List<TeamMemberResponseDTO> getTeamMembers(Long teamId) {

        return teamMemberRepository.findByTeam_Id(teamId).stream()
                .map(m -> {
                    User user = m.getUser();
                    User manager = user.getManager();
                    String managerName = manager != null
                            ? manager.getFirstName() + " " + manager.getLastName()
                            : null;

                    return TeamMemberResponseDTO.builder()
                            .id(m.getId())
                            .userId(user.getId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .designation(user.getDesignation())
                            .isManager(m.getIsManager())
                            .managerName(managerName)
                            .build();
                })
                .collect(Collectors.toList());

    }

    private TeamResponseDTO toResponseDTO(Team team) {
        return TeamResponseDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .description(team.getDescription())
                .build();
    }
}