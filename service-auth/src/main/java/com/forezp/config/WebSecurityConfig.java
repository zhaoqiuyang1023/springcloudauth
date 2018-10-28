package com.forezp.config;

import com.forezp.service.security.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) /*设置全局安全方法控制，设置在方法调用之前拦截，并进行权限验证*/
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*WebSecurityConfig 类通过＠EnableWebSecurity 注解开启 Web 保护功能，
    通过@EnableGlobalMethodSecurity 注解开启在方法上的保护功能 。
    WebSecurityConfig 类继承了 WebSecurityConfigurerAdapter类 ，并复写了 以下3个方法来做相关的配置:
      (1)configure(HttpSecurity http): HttpSecurity 中配置了所有的请求都需要安全验证,
      (2)configure(AuthenticationManagerBuilder auth): AuthenticationManagerBuilder中 配置了 验证 的用户信息源和密码加密的策略，并且向IoC容器注入了 AuthenticationManager对象。
      这需要在 0Auth2 中配置，因为在 0Auth2中配置了 AuthenticationManager, 密码验证才会开启。在本例中，采用的是(密码)验证 。
      (3)authenticationManagerBean（）：配置了验证管理的 Bean 。
    UserServiceDetail 这个类和 13.3.4 节中的 UserService是一样的，实现了 UserDetailsService 接口，并使用了 BCryptPasswordEncoder 对密码进行加密*/
    @Autowired
    private UserServiceDetail userServiceDetail;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable();
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {//添加用户角色
        auth.userDetailsService(userServiceDetail).passwordEncoder(new BCryptPasswordEncoder());

    }

    /*authenticationManager需要配置 AuthenticationManager 这个 Bean ，这个Bean来源于 WebSecurityConfigurerAdapter 中的配置 ，
    只有配置了这个 Bean才会 开启密码类型的验证。*/
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
