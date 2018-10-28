package com.forezp.config;

import com.forezp.service.security.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    /*   ClientDetailsServiceConfigurer ：配置客户端信息 。
       AuthorizationServerEndpointsConfigurer：配置授权 Token 的节点和 Token 服务 。
       AuthorizationServerSecurityConfigurer：配置 Token 节点的安全策略 。

AuthorizationServer的配置比较复杂细节较多，通过在实现了 AuthorizationServerConfigurer
接口的类上加＠EnableAuthorizationServer 注解， 开启 AuthorizationServer 的功能， 井注入 IoC 容器中 。
然后需要配置 ClientDetailsServiceConfigurer、 AuthorizationServerSecurityConfigurer 和 AuthorizationServerEndpointsConfigurer ，它们有很多可选的配置，
       */
    private TokenStore tokenStore = new InMemoryTokenStore();

    //JdbcTokenStore tokenStore=new JdbcTokenStore(dataSource);

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceDetail userServiceDetail;


    /*   (1).客户端的配置信息既可以放在内存中 ，也可以放在数据库中，需要配置以下信息 。*/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
     /*   clientld ： 客户端 Id ， 需要在 Authorization Server 中是唯一 的 。
        secret：客户端的密码。
        scope ：客户端的域。
       authorizedGrantTypes ：认证类型。
       authorities ：权限信息。*/
        //  客户端信息可以存储在数据库中 ，这样就可以通过更改数据库来实 时更新客户端信息 的数 据。 Spring0Auth2 己经设计好了数据库的表，且不可变
        clients.inMemory()
                .withClient("browser")
                .authorizedGrantTypes("refresh_token", "password")
                .scopes("ui")
                .and()
                .withClient("service-hi")
                .secret("123456")
                .authorizedGrantTypes("client_credentials", "refresh_token", "password")
                .scopes("server");
    }

    /* (2) AuthorizationServerEndpointsConfigurer
        在默认情况下，AuthorizationServerEndpointsConfigurer配置开启了所有的验证类型， 除了密码类型的验证，密码验证只有配置了authenticationManager 的配置才会开启。 AuthorizationServerEndpointsConfigurer 配置由 以下5项组成。
        authenticationManager：只有配置了该选项，密码认证才会开启。在大多数情况下都 是密码验证，所以一般都会配置这个选项。
        userDetailsService ：配置获取用户 认证信息的接口，和上一章实现的 userDetailsService 类似。
        authorizationCodeServices ：配置验证码服务 。
        implicitGrantService ：配置管理 implict 验证的状态 。
        token Grant曰：配置 Token Granter 。
        另外，简要设置Token的管理策略，目前支持以下 3 种 。
        InMemoryTokenStore: Token 存储在内存中 。
        JdbcTokenStore: Token 存储在数据库 中 。需要 引入 spring才dbc 的依赖包，并配置数 据源，以及初始化 Spring 0Auth2 的数据库脚本，即上一节的数据库脚本 。
        JwtTokenStore ： 采用 JWT 形式，这种形式没有做任何的存储，因为 JWT 本身包含了 用户验证的所有信息，不需要存储。采用这种形式 ， 需要引入 spring-jwt 的依赖
        */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore) /*授权管理策略与存储token的策略*/
                .authenticationManager(authenticationManager)/*这个Bean来源于 WebSecurityConfigurerAdapter 中的配置 ，只有配置了这个 Bean才会 开启密码类型的验证。最后配置了 userDetailsService，用来读取验证用户的信息。*/
                .userDetailsService(userServiceDetail);
    }

    /* (3)AuthorizationServerSecurityConfigurer
        如果资源服务和授权服务是在同 一个服务中，用默认的配置即可，不需要做其他任何的配 置。但是如果 资源服务和授权服务不在同 一个服务中，则需要做一些额外配置 。
       如果采用 RemoteTokenServices （远程 Token 校验），资源服务器的每次请求所携带的 Token 都需要从授 权服务做校验 。这时需要配置“／oauth/check token ”
       校验节点的校验策略。
    */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {

        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");

    }
}
