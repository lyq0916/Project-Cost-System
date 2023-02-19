package com.lyq.eca.pojo.Project;

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
@Table(name = "bid_project")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class BidProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    int pid;
    String state;
}
