package com.manufacturing.inventory.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUser {
    private Long userId;
    private Long tenantId;
    private String username;
}
