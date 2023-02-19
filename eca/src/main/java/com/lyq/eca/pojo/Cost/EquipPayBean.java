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

public class EquipPayBean {
    int id;
    String ename;
    String note;
    BigDecimal money;
    String project;
    Date date;
}
