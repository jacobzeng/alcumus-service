package org.fangzz.alcumus.alcumusservice.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final static Log log = LogFactory.getLog(JWTAuthorizationFilter.class.getSimpleName());

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        String requestUrl = req.getRequestURI();
        if (header == null || requestUrl.contains("/guest/")) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        if (null != authentication)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String tokenStr = request.getHeader(SecurityConstants.HEADER_STRING);
        if (!StringUtils.isEmpty(tokenStr)) {
            try {
                // parse the token.
                DecodedJWT token = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                        .build()
                        .verify(tokenStr);
                String username = token.getSubject();
                String[] rolesStr = token.getClaim("roles").asArray(String.class);

                if (username != null) {
                    List<SimpleGrantedAuthority> roles = Lists.newArrayList();
                    for (String roleStr : rolesStr) {
                        roles.add(new SimpleGrantedAuthority(roleStr));
                    }
                    return new UsernamePasswordAuthenticationToken(username, null, roles);
                }
            } catch (JWTDecodeException ex) {
                log.error("解析后台管理访问token出错," + tokenStr, ex);
                return null;
            } catch (JWTVerificationException ex) {
                log.error("Token验证出错," + tokenStr, ex);
                return null;
            }
            return null;
        }
        return null;
    }


}
