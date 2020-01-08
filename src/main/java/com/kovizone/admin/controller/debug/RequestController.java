package com.kovizone.admin.controller.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kovizone.admin.constant.UrlConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 模拟请求控制
 * <P/>
 * URL字典
 * <TR>
 * <TD>/request/view.do</TD>
 * <TD>跳转到模拟请求页</TD>
 * </TR>
 * <TR>
 * <TD>/request/post.do</TD>
 * <TD>模拟post请求</TD>
 * </TR>
 * <TR>
 * <TD>/request/get.do</TD>
 * <TD>模拟get请求</TD>
 * </TR>
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-16 KoviChen 新建类
 */
@Controller
@RequestMapping(UrlConstant.REQUEST)
public class RequestController {

	private Logger logger = LoggerFactory.getLogger(RequestController.class);

	@RequestMapping(UrlConstant.VIEW_DO)
	public ModelAndView view() {
		return new ModelAndView("debug/request");
	}

	@PostMapping("/post.do")
	public void post(HttpServletRequest request, HttpServletResponse response) {
		get(request, response);
	}

	@GetMapping("/get.do")
	public void get(HttpServletRequest request, HttpServletResponse response) {
		String debugUrl = request.getParameter("debugUrl");
		try {
			request.getRequestDispatcher(debugUrl).forward(request, response);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

}
