package study.deliveryhanghae.global.config.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import study.deliveryhanghae.global.config.security.jwt.JwtAuthenticationFilter;
import study.deliveryhanghae.global.config.security.jwt.JwtAuthorizationFilter;
import study.deliveryhanghae.global.config.security.jwt.JwtUtil;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    String[] APP_WHITE_LIST = {"/v2/login-page", "/v2/login", "/v2/signup", "/v1/login", "/v1/signup", "/signup", "/mailSend", "/favicon.ico"};

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CorsConfigurationSource corsConfigurationSource;


    @Autowired
    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration, CorsConfigurationSource corsConfigurationSource) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.corsConfigurationSource = corsConfigurationSource;
    }


    public JwtAuthenticationFilter ownerLoginFilter() throws Exception {
        JwtAuthenticationFilter ownerFilter = new JwtAuthenticationFilter(jwtUtil);
        // owner Login 링크 연결
        ownerFilter.setFilterProcessesUrl("/v2/login");
        ownerFilter.setAuthenticationManager(authenticationManager());
        return ownerFilter;
    }

    public JwtAuthenticationFilter userLoginFilter() throws Exception {
        JwtAuthenticationFilter userFilter = new JwtAuthenticationFilter(jwtUtil);
        // user Login 링크 연결
        userFilter.setFilterProcessesUrl("/login");
        userFilter.setAuthenticationManager(authenticationManager());
        return userFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + exception.getMessage());
        };
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 로그인 성공 후의 동작을 정의
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Login successful");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(APP_WHITE_LIST).permitAll()
                        .requestMatchers("/v1/**").permitAll()
                        .anyRequest().authenticated());

        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(ownerLoginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(userLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
