package org.lecture.chatservice.enums;

import java.util.Arrays;

public enum Role {
    CONSULTANT("ROLE_CONSULTANT"),
    USER("ROLE_USER");

    String code;

    Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Role fromCode(String code) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getCode().equals(code))
                .findFirst()
                .orElseThrow();
    }
}
