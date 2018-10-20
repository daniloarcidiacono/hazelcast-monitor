package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.util.ArrayList;
import java.util.List;

@TypescriptDTO
public class MembersProduct extends Product {
    @TypescriptDTO(identifier = "MemberSummaryDTO")
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

    private List<MemberSummary> objects = new ArrayList<>();

    public MembersProduct() {
    }

    public List<MemberSummary> getObjects() {
        return objects;
    }

    public void setObjects(final List<MemberSummary> objects) {
        this.objects = objects;
    }
}
