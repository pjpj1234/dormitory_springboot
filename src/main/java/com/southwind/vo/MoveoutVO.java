package com.southwind.vo;

import lombok.Data;

@Data
public class MoveoutVO {
    private Integer id;
    private String studentName;
    private String buildingName;
    private String dormitoryName;
    private String gender;
    private String reason;
    private String createDate;
}
