package com.lyq.eca.pojo.Menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "menu")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Menu {
    @Id
    @Column(name = "id")
    int id;
    String path;
    String name;
    @Column(name = "name_zh")
    String namezh;
    @Column(name = "icon_cls")
    String icon;
    String component;
    @Column(name = "parent_id")
    int parentid;

    /*
    * 数据库中不存在字段的属性，需要用@Transient标记出来
    * */
    @Transient
    List<Menu> children;
}
