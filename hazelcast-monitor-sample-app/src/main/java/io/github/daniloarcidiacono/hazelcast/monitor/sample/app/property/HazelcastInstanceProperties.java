package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.property;

public class HazelcastInstanceProperties {
    private String instanceName;
    private String groupName;
    private String groupPassword;
    private boolean managementCenterEnabled = false;
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
