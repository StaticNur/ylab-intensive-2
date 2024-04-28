package com.ylab.intensive.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    private int id;
    private int userId;
    private LocalDateTime dateOfAction;
    private String action;
}
