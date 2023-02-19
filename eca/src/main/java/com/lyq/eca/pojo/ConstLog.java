package com.lyq.eca.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "const_log")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class ConstLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    int id;
    int pid;
    String note;
    Date date;
    String picture;
}
