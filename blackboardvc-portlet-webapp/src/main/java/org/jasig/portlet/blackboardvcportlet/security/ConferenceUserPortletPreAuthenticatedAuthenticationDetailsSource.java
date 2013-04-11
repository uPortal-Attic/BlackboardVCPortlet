package org.jasig.portlet.blackboardvcportlet.security;

import java.util.Collection;

import javax.portlet.PortletRequest;

import org.jasig.springframework.security.portlet.authentication.PortletPreAuthenticatedAuthenticationDetailsSource;
import org.jasig.springframework.security.portlet.authentication.PreAuthenticatedGrantedAuthoritiesPortletAuthenticationDetails;
import org.springframework.security.core.GrantedAuthority;

public class ConferenceUserPortletPreAuthenticatedAuthenticationDetailsSource extends
        PortletPreAuthenticatedAuthenticationDetailsSource {

    @Override
    public PreAuthenticatedGrantedAuthoritiesPortletAuthenticationDetails buildDetails(PortletRequest context) {

        Collection<? extends GrantedAuthority> userGas = buildGrantedAuthorities(context);

        PreAuthenticatedGrantedAuthoritiesPortletAuthenticationDetails result =
                new PreAuthenticatedGrantedAuthoritiesPortletAuthenticationDetails(context, userGas);

        return result;
    }

}
