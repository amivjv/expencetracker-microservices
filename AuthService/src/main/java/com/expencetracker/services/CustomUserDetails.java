package com.expencetracker.services;

import com.expencetracker.entities.UserInfo;
import com.expencetracker.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends UserInfo implements UserDetails {
    public Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo byUsername) {
        super.setUserId(byUsername.getUserId());
        super.setUsername(byUsername.getUsername());
        super.setPassword(byUsername.getPassword());
        super.setRoles(byUsername.getRoles());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (UserRole userRole : byUsername.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(userRole.getRoleName().toUpperCase()));
        }
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.copyOf(authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
