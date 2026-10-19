package com.expencetracker.eventproducer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoEvent {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String userId;
}
