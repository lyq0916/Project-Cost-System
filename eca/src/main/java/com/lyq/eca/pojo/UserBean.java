package com.lyq.eca.pojo;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserBean {
    int id;
    String username;
    List<Integer> roles;
}
