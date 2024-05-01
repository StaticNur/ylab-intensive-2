package com.ylab.intensive.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents an audit record.
 * <p>
 * This class encapsulates information about an audit record, including its unique identifier, the user ID associated with the action,
 * the date and time of the action, and a description of the action.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    /**
     * The unique identifier of the audit record.
     */
    private int id;

    /**
     * The user ID associated with the action.
     */
    private int userId;

    /**
     * The date and time of the action.
     */
    private LocalDateTime dateOfAction;

    /**
     * A description of the action.
     */
    private String action;
}

