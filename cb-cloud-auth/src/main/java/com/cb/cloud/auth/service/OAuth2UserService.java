package com.cb.cloud.auth.service;

import cn.hutool.core.collection.CollUtil;
import com.cb.cloud.auth.constant.MessageConstant;
import com.cb.cloud.auth.dao.UserDao;
import com.cb.cloud.auth.entity.SecurityUser;
import com.cb.cloud.auth.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
public class OAuth2UserService implements UserDetailsService {
	@Autowired
	private UserDao userDao;

	private List<UserEntity> userList;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//模拟登陆，通过上面initData方法初始化了一个账号
		// List<UserEntity> findUserList = userList.stream().filter(item -> item.getLoginname().equals(username)).collect(Collectors.toList());

		List<UserEntity> findUserList = null;
		try{
			findUserList=userDao.findByUsername(username);

		}catch(Exception e){
			e.printStackTrace();
		}

		if (CollUtil.isEmpty(findUserList)) {
			throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
		}
		//此处应该读库，获取用户的角色list,这里应该和client中配置的角色权限一致
		//自定义的角色名叫：ADMIN，但是client中配置的是ROLE_ADMIN，所以加一个ROLE_
		UserEntity user = findUserList.get(0);
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_ADMIN");
		user.setRoles(roles);

		SecurityUser securityUser = new SecurityUser(user);
		if (!securityUser.isEnabled()) {
			throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
		} else if (!securityUser.isAccountNonLocked()) {
			throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
		} else if (!securityUser.isAccountNonExpired()) {
			throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
		} else if (!securityUser.isCredentialsNonExpired()) {
			throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
		}
		return securityUser;
	}

}
