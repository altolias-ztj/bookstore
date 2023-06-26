package com.bookstore.common.entity.wx;

import lombok.Data;

@Data
public class TicketData {
    private int expire_seconds;
    private String action_name;
    private ActionInfo action_info;

    public TicketData(int expire, String scene_str) {
        this.expire_seconds = expire;
        action_name = "QR_STR_SCENE";
        this.action_info = new ActionInfo(scene_str);
    }

    @Data
    public static class ActionInfo {

        private Scene scene;

        public ActionInfo(String str) {
            this.scene = new Scene(str);
        }

        @Data
        public static class Scene {
            private String scene_str;

            public Scene(String scene_str) {
                this.scene_str = scene_str;
            }
        }
    }


}
