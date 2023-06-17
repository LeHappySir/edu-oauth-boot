package com.lagou.edu.oauth.entity;

import com.lagou.edu.common.web.entity.vo.BaseVo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role
 *
 * @author xianhongle
 * @data 2022/5/23 22:43
 **/
@Data
@NoArgsConstructor
public class Role extends BaseVo {

    private String code;

    private String name;

    private String description;
}
