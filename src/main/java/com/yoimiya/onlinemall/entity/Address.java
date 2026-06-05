package com.yoimiya.onlinemall.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Address {
    private Long id;
    private Long userId;
    private String receiverName;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
