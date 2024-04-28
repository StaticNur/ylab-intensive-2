package com.ylab.intensive.model.dto;

import com.ylab.intensive.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditDto {
    private UUID uuid;
    /**
     * The email address of the user
     */
    private String email;
    /**
     * The role of the user (e.g., ADMIN, USER)
     */
    private Role role;
    /**
     * The list of actions performed by the user (Audit)
     */
    private Map<LocalDateTime, String> action;
}
