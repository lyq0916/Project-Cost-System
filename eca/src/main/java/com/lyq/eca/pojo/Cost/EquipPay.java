package com.lyq.eca.pojo.Cost;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "equippay")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class EquipPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String eid;
    String note;
    int mid;
}
