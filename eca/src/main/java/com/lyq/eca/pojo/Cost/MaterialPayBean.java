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

public class MaterialPayBean {
    int id;
    String material;
    String supplier;
    BigDecimal money;
    String project;
    Date date;
}
