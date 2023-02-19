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

public class IndirectBean {
    int id;
    String type1;
    String type2;
    String note;
    BigDecimal money;
    String project;
    Date date;
}
