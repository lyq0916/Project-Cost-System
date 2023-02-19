package com.lyq.eca.pojo.Project;

import com.lyq.eca.pojo.Project.Project;
import lombok.*;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConstBean {
    int pid;
    String pname;
    Project project;
    String state;
    Date estart;
    Date start;
    Date eend;
    Date end;
    Double budget;
    double cost;
}
