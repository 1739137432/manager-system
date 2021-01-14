package com.zero.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @Classname Role
 * @Description 角色实体类
 * @Date 2019/7/18 8:34
 * @Created by WDD
 */
@Data
public class Role implements GrantedAuthority {
    private Integer id;
    private String name;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
