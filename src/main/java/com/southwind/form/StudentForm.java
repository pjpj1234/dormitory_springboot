package com.southwind.form;

import lombok.Data;

@Data
public class StudentForm {
    private Integer id;
    private String username;
    private String password;
    private String number;
    private String name;
    private String gender;
    private Integer introduction;
    private Integer buildingId;
    private Integer DormitoryId;
    private Integer oldDormitoryId;
    private String state;
    private String createDate;
}
