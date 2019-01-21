package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("sampleapp")
public class SampleAppProperties {
    private String instanceName = "dev";
    private String groupName = "group";
    private String groupPassword = "pass";
    private boolean managementCenterEnabled;
    private String managementCenterUrl = "http://localhost:8080/mancenter";

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    public boolean isManagementCenterEnabled() {
        return managementCenterEnabled;
    }

    public void setManagementCenterEnabled(boolean managementCenterEnabled) {
        this.managementCenterEnabled = managementCenterEnabled;
    }

    public String getManagementCenterUrl() {
        return managementCenterUrl;
    }

    public void setManagementCenterUrl(String managementCenterUrl) {
        this.managementCenterUrl = managementCenterUrl;
    }
}
