# KOVIZONE.ADMIN

## 目录结构说明

```text
config - 环境配置包
src/main
├── java - 代码包
|    └── com.kovizone.admin
|         ├── anno - 自定义注解层*
|         ├── bo - 业务对象层（BusinessObject）
|         ├── config - 配置层*
|         ├── constant - 常量层
|         ├── controller - 控制层*
|         ├── filter - 过滤层*
|         ├── mapper - Mybatis映射接口层（持久层）
|         ├── po - 持久对象层（PersistentObject）
|         ├── realm - shiro领域层*
|         ├── service - 服务接口层
|         |    └── impl - 服务实现层
|         └── util - 工具层
└── resources - 资源包
     ├── logback - Logback日志配置包
     ├── mapper - mybatis映射配置包
     ├── static - 静态资源包
     └── templates - 视图包
```

> 标[*]表示该层正常情况下不要二次开发

## 开发说明

### 1. 控制层权限验证框架Shiro

在application中controllerScan指定的包（及其子包）中开发控制层方法，使用@PostMapping注解类或方法产生的URL将会生成为权限

添加@PermissionScanIgnore注解将不生成权限，即该控制层方法为全角色公用

### 2. 持久框架Mybatis-plus

在mybatis-plus.mapperScan指定的包中定义接口

继承com.baomidou.mybatisplus.core.mapper.BaseMapper\<T\>，泛型指定持久对象，以使用mybatis-plus封装的crud方法（若需要）

### 3. 数据源Druid

数据源监控平台URL：[server.ip]:[server.port]/durid/login.html

### 4. 自定义配置

在com.airepay.score.system.wallet.config新增属性及其getter&setter，在application配置文件中新增com.airepay.score.system.wallet.[属性名]即可

服务中直接注入com.airepay.score.system.wallet.config，通过get方法获取该属性即可进行使用

### 5. 视图开发

#### 5.1 核心框架layui

[开发文档](https://www.layui.com/doc/)

> 参考src/main/resources/templates/system/user.html，该视图可作为模板

#### 5.2 引入的layui第三方组件：

##### 5.2.1 下拉复选框：formSelects/formSelects-v4（参考：用户管理编辑时添加多角色）

[开发文档](https://github.com/hnzzmsf/layui-formSelects/)（已停止维护）

##### 5.2.2 树形表格：treetable-lay/treetable（参考：权限管理表格）

[开发文档](https://gitee.com/whvse/treetable-lay)

##### 5.2.3 图标选择器：iconPicker/iconPicker（参考：权限管理新增权限） 

[开发文档](https://gitee.com/wujiawei0926/iconpicker)

##### 5.2.4 树形复选框：treeSelect/treeSelect（参考：权限管理新增权限选中上级目录）

[开发文档](https://gitee.com/wujiawei0926/treeselect)

### 6. 控制层开发

#### 6.1 针对跳转式控制方法强制要求

> 即无@RestController注解类，且无@ResponseBody注解的方法

##### 6.1.1 仅允许返回ModelAndView

即便此次请求仅做一次跳转，也不要使用String作为返回值，这种规范是出于可读性的考虑，返回ModelAndView可明确理解为页面跳转，但是返回String往往需要综合考虑，索性强制返回ModelAndView；

###### 正例

```JAVA
@RequestMapping(UrlConstant.VIEW_DO)
public ModelAndView view() {
	return new ModelAndView(ViewConstant.ROLE_VIEW);
}
```

###### 反例

```JAVA
@RequestMapping(UrlConstant.VIEW_DO)
public String view() {
	return ViewConstant.ROLE_VIEW;
}
```

#### 6.2 针对非跳转式控制方法强制要求

> 即@RestController注解类，或有@ResponseBody注解的方法

##### 6.2.1. 不允许返回String，尽量返回GeneralData对象

不论是需要返回数组、对象、或是简单的字符串，也要使用GeneralData封装一次再返回，前端需要多加判断，此举是为了兼容会话超时的统一处理，关于会话超时在后面有提到；

###### 关于GeneralData对象
```JAVA
public class GeneralData {

	/**
	 * 服务结果
	 */
	private boolean result;

	/**
	 * 服务响应码<BR/>
	 * 403 - 会话超时
	 */
	private int code;

	/**
	 * 服务响应信息
	 */
	private String msg;

	/**
	 * 存储List集
	 */
	private List<?> list;

	/**
	 * 存储Map集
	 */
	private Map<String, Object> map;
	// getter and setter ...
}
```

> GeneralData 已能满足大部分返回数据结构需求

###### 正例
```JAVA
// 后台
@RequestMapping("/systemRoleList.do")
public GeneralData systemRoleList() {
	List<SystemRole> systemRoleList = systemRoleService.list(null, 0, 0);
	GeneralData generalData = new GeneralData(true, "");
	generalData.setList(systemRoleList);
	return generalData;
}
```

```javascript
// 前端
$.post('/systemRoleList.do',function(generalData){
	if(!generalData.result){
		if(generalData.code === 403){
			// 会话超时
			window.location.href='/error.do';
		}else{
			// do something;
		}
	}
	var systemRoleList = generalData.list;
	// do something;
});
```

###### 反例
```JAVA
// 后台
@RequestMapping("/systemRoleList.do")
public List<SystemRole> systemRoleList() {
	return systemRoleService.list(null, 0, 0);
}
```

```javascript
// 前端
$.post('/systemRoleList.do',function(systemRoleService){
	// 无法统一处理会话超时的情况
	// do something;
});
```

> 会话超时相关解释将在后文描述

##### 6.2.2 前端控件严格要求返回格式

若前端控件严格要求返回格式，应当忽略2.2.1；

如layui的table组件，本系统已封装了该对象，即TableData，只需要返回TableData即可，layui的table组件是可以兼容本系统的统一会话失效处理的；

#### 6.3 会话超时处理

> 会话超时时将不会进入URL对应的控制层方法

##### 6.3.1 AJAX情况

会话超时时，若是AJAX请求，则响应一个GeneralData对象（非json），其值为{result=false,code=403,msg='会话已断开'}，代码应判断result和code值，并进行跳转，如下所示：

```javascript
$.post(url,function(generalData){
	if(!generalData.result){
		if(generalData.code === 403){
			// 会话超时
			window.location.href='/error.do';
		}else{
			// do something;
		}
	}
	// do something;
});
```

因这个机制，所以所有非跳转（返回json或对象）控制方法**半强制**只允许返回GeneralData对象对象；

###### 特殊情况

layui的table组件原本应当识别特定数据格式，但当会话超时返回GeneralData对象时，它仍能解析到generalData.msg，并作出相应提示，所以对于layui的table组件对后台进行的AJAX请求是不需要进行处理的；

##### 6.3.2 非AJAX情况

无需干涉，系统将会跳转到错误页面；

#### 6.4 权限认证失败处理

> 权限认证失败时将不会进入URL对应的控制层方法

#### 6.4.1 AJAX情况

同2.7.3.1 唯一的不同是响应的GeneralData对象值为{result=false,code=403,msg='权限认证失败'}；

#### 6.4.2 非AJAX情况

同2.7.3.2；

### 7. Shiro权限控制

只要在application中controllerScan指定正确的控制层包名，系统初始化时将会自动扫描该包（及其子包）内的方法和对应的URL，并将该URL识别为该方法的权限

当数据库中的权限表（system_permission）中没有找到该权限（url）时，或者在数据库中存在，但是控制层没有对应的控制方法时，会在控制台输出日志进行提示，如下所示：

```LOG
待实现地址权限：[/permission/view.do]
待注册地址权限：[/test/test.do]
```

> 待注册：指数据库中不存在该权限

> 待实现：指数据库中的该权限没有对应的控制方法