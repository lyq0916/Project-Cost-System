package com.lyq.eca.pojo.Cost;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class EmpPayBean {
    int id;
    String eid;
    BigDecimal basic;
    BigDecimal allowance;
    BigDecimal push;
    BigDecimal money;
    String project;
    Date date;
}
