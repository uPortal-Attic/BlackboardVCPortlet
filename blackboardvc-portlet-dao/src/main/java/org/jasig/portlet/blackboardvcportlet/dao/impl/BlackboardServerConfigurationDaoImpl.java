package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jasig.portlet.blackboardvcportlet.dao.ServerConfigurationDao;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elluminate.sas.ServerConfigurationResponse;

@Repository
public class BlackboardServerConfigurationDaoImpl extends BaseJpaDao implements ServerConfigurationDao {
    private static final Pattern DATE_TZ_PATTERN = Pattern.compile(".+ \\(.+, [^ ]+ ([^:]+):([^\\)]+)\\)");
    
    @Override
    @Transactional
    public ServerConfigurationImpl createOrUpdateConfiguration(ServerConfigurationResponse configurationResponse) {
        ServerConfigurationImpl serverConfiguration = this.getServerConfiguration();
        if (serverConfiguration == null) {
            serverConfiguration = new ServerConfigurationImpl();
        }
        
        serverConfiguration.setBoundaryTime(configurationResponse.getBoundaryTime());
        serverConfiguration.setMaxAvailableTalkers(configurationResponse.getMaxAvailableTalkers());
        serverConfiguration.setMaxAvailableCameras(configurationResponse.getMaxAvailableCameras());
        serverConfiguration.setRaiseHandOnEnter(configurationResponse.isRaiseHandOnEnter());
        serverConfiguration.setMayUseTelephony(configurationResponse.isMayUseTelephony());
        serverConfiguration.setMayUseSecureSignOn(configurationResponse.isMayUseSecureSignOn());
        serverConfiguration.setMustReserveSeats(configurationResponse.isMustReserveSeats());
        
        final String timeZoneStr = configurationResponse.getTimeZone();
        final Matcher tzMatcher = DATE_TZ_PATTERN.matcher(timeZoneStr);
        if (tzMatcher.matches()) {
            int hours = Integer.parseInt(tzMatcher.group(1));
            int minutes = Integer.parseInt(tzMatcher.group(2));

            final DateTimeZone tz = DateTimeZone.forOffsetHoursMinutes(hours, minutes);
            serverConfiguration.setTimezone(tz);
            
            logger.debug("Parsed timezone string '{}' to {}", timeZoneStr, tz);
        }
        else {
            final DateTimeZone tz = DateTimeZone.getDefault();
            serverConfiguration.setTimezone(tz);
            logger.warn("Failed to parse timezone string '{}' defaulting to {}", timeZoneStr, tz);
        }
        
        this.getEntityManager().persist(serverConfiguration);
        
        return serverConfiguration;
    }

    @Override
    public ServerConfigurationImpl getServerConfiguration() {
        return this.getEntityManager().find(ServerConfigurationImpl.class, ServerConfigurationImpl.CONFIG_ID);
    }

    @Override
    @Transactional
    public void deleteServerConfiguration() {
        ServerConfigurationImpl serverConfiguration = this.getServerConfiguration();
        if (serverConfiguration != null) {
            this.getEntityManager().remove(serverConfiguration);
        }
    }
}
