package org.example.hrms.team.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.hrms.model.User;

@Getter
@Entity
@Table(name = "team_members")
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Boolean isManager = false;

    @Setter
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}