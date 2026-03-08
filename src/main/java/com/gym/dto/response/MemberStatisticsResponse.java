package com.gym.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberStatisticsResponse {
    private Long totalMembers;
    private Long activeMembers;
    private Integer monthlyGrowthPercentage;
    private Long newMembersThisMonth;
}
