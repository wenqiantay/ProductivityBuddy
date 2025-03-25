package vttp.miniproj.App_Server.models;

import java.util.List;

public class MessageData {
    private String command;
    private Data data;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<String> uris;
        private long positionMs;

        public List<String> getUris() {
            return uris;
        }

        public void setUris(List<String> uris) {
            this.uris = uris;
        }

        public long getPositionMs() {
            return positionMs;
        }

        public void setPositionMs(long positionMs) {
            this.positionMs = positionMs;
        }

    }
}
