package com.th3.appms.domain;

import javax.validation.constraints.*;

import org.springframework.stereotype.Component;

/**
 * A NotificationContent.
 */
@Component
public class NotificationContent {

    @NotNull
    private String imei;

    @NotNull
    private String target;

    @NotNull
    private String name;

    @NotNull
    private String description;
    
    public String getImei() {
        return imei;
    }
    
    public String getTarget() {
        return target;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Imei{" +
            "id=" + name +
            ", imei='" + imei + "'" +
            ", email='" + description + "'" +
            '}';
    }
}
