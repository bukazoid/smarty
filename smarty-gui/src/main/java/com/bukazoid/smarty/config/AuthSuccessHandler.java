package com.bukazoid.smarty.config;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	public AuthSuccessHandler() {
		super();
		setAlwaysUseDefaultTargetUrl(false);
	}

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		String rolesCookie = roles.stream().collect(Collectors.joining(":"));
		Cookie cookie = new Cookie("userRoles", rolesCookie);
		response.addCookie(cookie);

		Cookie userLogin = new Cookie("userLogin", authentication.getName());
		response.addCookie(userLogin);

		if (savedRequest == null) {
			if (roles.contains("ROLE_ADMIN")) {
				response.sendRedirect("/admin/views");
			} else {
				response.sendRedirect("/user/views");
			}
	
			return;
		}
		String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}

		clearAuthenticationAttributes(request);

		// Use the DefaultSavedRequest URL
		String targetUrl = savedRequest.getRedirectUrl();
		log.info("Redirecting to DefaultSavedRequest Url: " + targetUrl);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

}
