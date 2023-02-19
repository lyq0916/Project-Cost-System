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
@Table(name = "indirect")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Indirect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String type1;
    String type2;
    String note;
    int mid;
}
