package study.deliveryhanghae.global.config.security.owner;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import study.deliveryhanghae.domain.owner.entity.Owner;

import java.util.Collection;

@Getter
public class OwnerDetailsImpl implements UserDetails {

    private final Owner owner;

    public OwnerDetailsImpl(Owner user) {
        this.owner = user;
    }

    public Owner getUser() {
        return owner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return owner.getPassword();
    }

    @Override
    public String getUsername() {
        return owner.getEmail();
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