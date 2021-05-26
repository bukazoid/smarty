package com.bukazoid.smarty.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bukazoid.smarty.service.UserSecurityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final AppProps props;
	private final UserSecurityService userSecurityService;

	public HttpSecurity security(HttpSecurity http) throws Exception {
		if (props.isSecure()) {
			return http.csrf().disable().requiresChannel().anyRequest().requiresSecure().and();
		} else {
			return http.csrf().disable();
		}
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		log.info("props: {}", props);
		// @formatter:off
			security(http)
			.authorizeRequests().antMatchers("/built/**").permitAll()
			.and()
			.authorizeRequests().antMatchers("/login*").permitAll()
			.and()
			.logout().deleteCookies("JSESSIONID").logoutUrl("/logout")
			.and()
			.formLogin().loginPage("/login")
			.usernameParameter("one2login").passwordParameter("pass2login")
			.defaultSuccessUrl("/admin/values", false)// may depends on user type
			.successHandler(new AuthSuccessHandler()	)
			.and()
			.authorizeRequests().antMatchers("/api/users").hasRole("ADMIN")
			.and()
			.authorizeRequests().antMatchers("/api/devices").hasRole("ADMIN")
//			.and()
//			.authorizeRequests().antMatchers("/api/values").hasRole("ADMIN")
//			.and()
//			.authorizeRequests().antMatchers("/api/views/userViews").authenticated()			
//			.and()
//			.authorizeRequests().antMatchers("/api/views").hasRole("ADMIN")
			.and()
			.authorizeRequests().anyRequest().authenticated()
			.and()
			.rememberMe().key("smarty_secrect"/*set an UUID here if you like, but maybe machine id likeIP/MAC would be enough*/).userDetailsService(userSecurityService);
		// @formatter:on
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence password) {
				// encode only plain passwords, not encoded ones
				if (password == null) {
					return null;
				}
				if (password.toString().startsWith("$2a")) {
					return password.toString();
				}
				return encoder.encode(password);
			}

			@Override
			public boolean matches(CharSequence charSequence, String s) {
				log.info("matches: {}, {}", charSequence, s);
				if (s.toLowerCase().startsWith("$2a")) {
					// BCrypt
					boolean match = encoder.matches(charSequence, s);
					boolean match3 = encoder.matches("test", encoder.encode("test"));
					log.info("bcrypt, match: {}, match3: {}", match, match3);
					return match;
				}
				// support of raw, not encrypted passwords
				return charSequence.toString().equals(s);
			}
		};
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userSecurityService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
}
