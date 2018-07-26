package it.xdnl.hazelcast.monitor.agent.product;

import it.xdnl.chat.typescript.annotation.TypescriptDTO;

import java.util.ArrayList;
import java.util.List;

public class MembersProduct implements Product {
    @TypescriptDTO
    public static class MemberSummary {
        private String address;
        private int port;
        private String version;
        private String uuid;

        public MemberSummary(String address, int port, String version, String uuid) {
            this.address = address;
            this.port = port;
            this.version = version;
            this.uuid = uuid;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }

    private List<MemberSummary> members = new ArrayList<>();

    public MembersProduct() {
    }

    public List<MemberSummary> getMembers() {
        return members;
    }

    public void setMembers(List<MemberSummary> members) {
        this.members = members;
    }
}
