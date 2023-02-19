package com.lyq.eca.pojo;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ConstLogBean {
    int id;
    int pid;
    String pname;
    String note;
    Date date;
    String picture;
}
