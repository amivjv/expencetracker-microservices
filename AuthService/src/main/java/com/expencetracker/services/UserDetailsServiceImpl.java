package com.expencetracker.services;

import com.expencetracker.entities.UserInfo;
import com.expencetracker.eventproducer.UserInfoEvent;
import com.expencetracker.eventproducer.UserInfoProducer;
import com.expencetracker.modals.UserInfoDto;
import com.expencetracker.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserInfoProducer userInfoProducer;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user " + username + " !!"));
        return new CustomUserDetails(userInfo);
    }

    public UserInfo checkIfUserAlreadyExists(String username) {
        return userRepository.findByUsername(username).isPresent() ? userRepository.findByUsername(username).get() : null;
    }

    public String signUpUser(UserInfoDto userInfoDto) {
        if (Objects.nonNull(checkIfUserAlreadyExists(userInfoDto.getUsername()))) {
            return null;
        }
        String userId = UUID.randomUUID().toString();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUsername(userInfoDto.getUsername());
        userInfo.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        userInfo.setRoles(new HashSet<>());
        userRepository.save(userInfo);
        userInfoProducer.sendEventToKafkaTopic(userInfoDtoToUserInfoEvent(userInfoDto, userId));
        return userId;
    }

    private UserInfoEvent userInfoDtoToUserInfoEvent(UserInfoDto userInfoDto, String userId) {
        return UserInfoEvent.builder().firstName(userInfoDto.getFirstName()).lastName(userInfoDto.getLastName()).userId(userId).email(userInfoDto.getEmail()).phoneNumber(userInfoDto.getPhoneNumber()).build();
    }
}
