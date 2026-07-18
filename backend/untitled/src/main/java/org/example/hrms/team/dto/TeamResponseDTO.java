package org.example.hrms.team.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamResponseDTO {

    private Long id;
    private String teamName;
    private String description;
}
