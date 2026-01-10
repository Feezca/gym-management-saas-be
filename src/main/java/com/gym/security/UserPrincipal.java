package com.gym.security;

import com.gym.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.gym.enums.Role;
import java.util.Collection;
import java.util.Set;

public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Role role;
    private final String tenant; // schema único

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.tenant = user.getTenant() != null
                ? user.getTenant()
                : null; // SUPERADMIN
    }

    public Role getRole() {
        return role;
    }

    public String getTenant() {
        return tenant;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(() -> "ROLE_" + role.name());
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
