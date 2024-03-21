package study.deliveryhanghae.global.config.security;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.owner.repository.OwnerRepository;
import study.deliveryhanghae.domain.user.entity.User;
import study.deliveryhanghae.domain.user.repository.UserRepository;
import study.deliveryhanghae.global.config.security.owner.OwnerDetailsImpl;
import study.deliveryhanghae.global.config.security.user.UserDetailsImpl;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private final OwnerRepository ownerRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, OwnerRepository ownerRepository) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (ownerRepository.existsByEmail(username)) {
            Owner owner = ownerRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
            return new OwnerDetailsImpl(owner);
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        return new UserDetailsImpl(user);
    }
}