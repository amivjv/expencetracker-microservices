package com.expencetracker.modals;

import com.expencetracker.entities.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoDto extends UserInfo {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
