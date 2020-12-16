package com.cb.cloud.auth.entity;


import lombok.*;

import java.io.Serializable;
import java.util.List;



@Data
@Builder
@AllArgsConstructor //全参构造函数
@NoArgsConstructor  //无参构造函数
public class UserEntity implements Serializable {
	private Long id;
	private String username;
	private String password;
	private Integer status;
	private List<String> roles;
}

