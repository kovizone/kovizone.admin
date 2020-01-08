package com.kovizone.admin.constant;

/**
 * Shiro拦截用户标识
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-08 KoviChen 新建类
 */
public class ShiroFilterConstant {

	/**
	 * 无参，开放权限，可以理解为匿名用户或游客
	 */
	public static final String ANON = "anon";

	/**
	 * 无参，需要认证
	 */
	public static final String AUTHC = "authc";

	/**
	 * 无参，注销
	 */
	public static final String LOGOUT = "logout";

	/**
	 * 无参，表示 httpBasic 认证
	 */
	public static final String AUTHC_BASIC = "authcBasic";

	/**
	 * 无参，表示必须存在用户，当登入操作时不做检查
	 */
	public static final String USER = "user";

	/**
	 * 无参，表示安全的URL请求，协议为 https
	 */
	public static final String SSL = "ssl";

	/**
	 * 参数可写多个，表示需要某个或某些权限才能通过，多个参数时写 perms[“user, admin”]，当有多个参数时必须每个参数都通过才算通过
	 */
	public static final String PERMS = "perms";

	/**
	 * 参数可写多个，表示是某个或某些角色才能通过，多个参数时写 roles[“admin，user”]，当有多个参数时必须每个参数都通过才算通过
	 */
	public static final String ROLES = "roles";

	/**
	 * 根据请求的方法，相当于 perms[user:method]，其中 method 为 post，get，delete 等
	 */
	public static final String REST = "rest";

	/**
	 * 当请求的URL端口不是8081时，跳转到schemal://serverName:8081?queryString 其中 schmal 是协议 http
	 * 或 https 等等，serverName 是你访问的 Host，8081 是 Port 端口， queryString 是你访问的 URL 里的 ?
	 * 后面的参数
	 */
	public static final String PORT = "port";
}
