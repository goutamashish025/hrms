package org.example.hrms.team.dto;

import java.util.List;

public class TeamMemberRequestDTO {

    private List<Long> userIds;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}