package org.example.hrms.team.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamMemberResponseDTO {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String designation;
    private Boolean isManager;
    private String managerName;
}
