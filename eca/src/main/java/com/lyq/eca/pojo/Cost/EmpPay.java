package com.lyq.eca.pojo.Cost;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "emppay")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class EmpPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String eid;
    @JsonFormat
    @Column(length = 10, scale = 2)
    BigDecimal basic;
    @JsonFormat
    @Column(length = 10, scale = 2)
    BigDecimal allowance;
    @JsonFormat
    @Column(length = 10, scale = 2)
    BigDecimal push;
    int mid;
}
