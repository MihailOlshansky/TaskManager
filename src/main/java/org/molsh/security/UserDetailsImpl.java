package org.molsh.security;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.molsh.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    private final Boolean isActive;

    private final List<GrantedAuthority> grantedAuthorities;

    public UserDetailsImpl(User user) {
        this.user = user;
        isActive = true;

        this.grantedAuthorities = new LinkedList<>();
        this.grantedAuthorities.add(roleFormatter(user.getRoles().name()));
    }

    private SimpleGrantedAuthority roleFormatter(String role) {
        return new SimpleGrantedAuthority("ROLE_" + role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }
}
