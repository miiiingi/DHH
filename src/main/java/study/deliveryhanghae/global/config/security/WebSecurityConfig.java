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
import study.deliveryhanghae.global.config.security.jwt.*;
import study.deliveryhanghae.global.handler.CustomAccessDeniedHandler;
import study.deliveryhanghae.global.handler.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    String[] APP_WHITE_LIST = {
            "/v2/login-page",
            "/v2/login",
            "/v2/signup",
            "/signup",
            "/mailSend",
            "/actuator/**",
            "/error",
            "/v1/**",
            "/logout"
    };

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;


    @Autowired
    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider, AuthenticationConfiguration authenticationConfiguration, CorsConfigurationSource corsConfigurationSource, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, TokenService tokenService, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationConfiguration = authenticationConfiguration;
        this.corsConfigurationSource = corsConfigurationSource;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }


    public JwtAuthenticationFilter ownerLoginFilter() throws Exception {
        JwtAuthenticationFilter ownerFilter = new JwtAuthenticationFilter(jwtTokenProvider, tokenService);
        // owner Login 링크 연결
        ownerFilter.setFilterProcessesUrl("/v2/login");
        ownerFilter.setAuthenticationManager(authenticationManager());
        return ownerFilter;
    }

    public JwtAuthenticationFilter userLoginFilter() throws Exception {
        JwtAuthenticationFilter userFilter = new JwtAuthenticationFilter(jwtTokenProvider, tokenService);
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // cors 처리
                .csrf(AbstractHttpConfigurer::disable) // csrf off
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // static resources permitAll
                        .requestMatchers(APP_WHITE_LIST).permitAll()
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JwtFilter(jwtTokenProvider, userDetailsService), JwtAuthenticationFilter.class)
                .addFilterBefore(ownerLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(userLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionConfig) -> exceptionConfig
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 401 처리
                        .accessDeniedHandler(customAccessDeniedHandler)); // 403 처리

        return http.build();
    }


}
