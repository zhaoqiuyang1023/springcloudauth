package com.forezp.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {
	/*获取token的信息，一般在资源服务器中会配置此地址*/
/*本案例采用RemoteTokenServices这种方式对 Token进行验证。如果其他资源服务需要验证Token ，
则需要远程调用授权服务暴露的验证 Token 的 API 接口。本案例中 验证 Token的 API 接口的代码如下：*/
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Principal getUser(Principal principal) {
		return principal;
	}


}
