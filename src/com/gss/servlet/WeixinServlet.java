package com.gss.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.gss.po.TextMessage;
import com.gss.util.CheckUtil;
import com.gss.util.MessageUtil;

public class WeixinServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String echostr = req.getParameter("echostr");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		
		PrintWriter out = resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String FromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			String message = null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)) {
				//此处判断发送内容，可根据内容去做反馈
				if("1".equals(content)) {
					message = MessageUtil.initText(toUserName, FromUserName, MessageUtil.firstMenu());
				}else if("2".equals(content)){
					message = MessageUtil.initText(toUserName, FromUserName, MessageUtil.secondMenu());
				}else if("?".equals(content) || "？".equals(content)) {
					message = MessageUtil.initText(toUserName, FromUserName, MessageUtil.menuText());
				}else {	//其他内容
					message = MessageUtil.initText(toUserName, FromUserName, "您发送的内容为：" + content);
				}
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){	//接收事件推送
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {	//关注
					message = MessageUtil.initText(toUserName, FromUserName, MessageUtil.menuText());
				}
			}else {
				message = MessageUtil.initText(toUserName, FromUserName, "您发送的内容类型为：" + msgType);
			}
			System.out.println(message);
			out.print(message);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			out.close();
		}
		
	}
}
