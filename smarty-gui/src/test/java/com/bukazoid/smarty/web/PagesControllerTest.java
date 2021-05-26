package com.bukazoid.smarty.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.bukazoid.smarty.config.AppProps;
import com.bukazoid.smarty.domain.store.User;
import com.bukazoid.smarty.domain.store.UserType;
import com.bukazoid.smarty.service.UserSecurityService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeController.class)
public class PagesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserSecurityService userService;

	@Mock
	RedisTemplate redisTemplate;// to prevent error

	@BeforeEach
	public void setUp() {
		User admin = new User();
		admin.setLogin("admin");
		admin.setPassword("admin");
		admin.setType(UserType.ADMIN);
		List<GrantedAuthority> adminAuthorities = UserSecurityService.getAuthorities(admin);
		UserDetails adminDetails = new org.springframework.security.core.userdetails.User(admin.getLogin(), admin.getPassword(), adminAuthorities);
		Mockito.when(userService.loadUserByUsername(admin.getLogin())).thenReturn(adminDetails);

		User user = new User();
		user.setLogin("user");
		user.setPassword("user");
		user.setType(UserType.USER);
		List<GrantedAuthority> userAuthorities = UserSecurityService.getAuthorities(user);
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), userAuthorities);
		Mockito.when(userService.loadUserByUsername(user.getLogin())).thenReturn(userDetails);
	}

	@WithMockUser(username = "admin", authorities = { "ROLE_ADMIN" })
	@Test
	public void testAuthenticatedOnAdmin() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

	@WithMockUser(username = "user", authorities = { "ROLE_USER" })
	@Test
	public void testAccessDevicesByUser() throws Exception {
		mockMvc.perform(get("/admin/devices")).andExpect(status().isOk());
	}

	@WithMockUser(username = "user", authorities = { "ROLE_USER" })
	@Test
	public void testAccessApiDevicesByUser() throws Exception {
		mockMvc.perform(get("/api/devices")).andExpect(status().isForbidden());
	}

	@TestConfiguration
	static class TestConfig {

		@Bean
		JedisConnectionFactory jedisConnectionFactory() {
			return new JedisConnectionFactory();
		}

		// WHYYYYYYYY ?!
		@Bean
		public RedisTemplate<String, Object> redisTemplate() {
			RedisTemplate<String, Object> template = new RedisTemplate<>();
			template.setConnectionFactory(jedisConnectionFactory());
			return template;
		}

		@Bean
		public AppProps props()
		{
			return new AppProps();
		}
	}
}
