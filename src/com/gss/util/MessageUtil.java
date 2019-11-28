package com.gss.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gss.po.TextMessage;
import com.thoughtworks.xstream.XStream;

public class MessageUtil {
	
	public static final String MESSAGE_TEXT = "text";	//文本消息
	public static final String MESSAGE_IMAGE = "image";	//图片消息
	public static final String MESSAGE_VOICE = "voice";	//语音消息
	public static final String MESSAGE_VIDEO = "video";	//视频消息
	public static final String MESSAGE_SHORTVIDEO = "shortvideo";	//小视频消息
	public static final String MESSAGE_LOCATION = "location";		//地理位置消息
	public static final String MESSAGE_LINK = "link";	//链接消息
	public static final String MESSAGE_EVENT = "event";	//事件
	public static final String MESSAGE_SUBSCRIBE = "subscribe";	//subscribe(订阅)
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";	//unsubscribe(取消订阅)
	public static final String MESSAGE_TICKET = "TICKET";	//<Ticket>未关注时扫描
	public static final String MESSAGE_SCAN = "SCAN";	//已关注时扫描
	public static final String MESSAGE_LOCATIONS = "LOCATION";	//上报地理位置(客户同意后，5秒上报一次)
	public static final String MESSAGE_CLICK = "CLICK";	//自定义菜单
	public static final String MESSAGE_VIEW = "VIEW";	//点击菜单跳转链接时的时间推送
	
	/**
	 * 将xml类型转换为Map类型
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String ,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		//Map集合
		Map<String, String> map = new HashMap<>();
		//reader对象
		SAXReader reader = new SAXReader();
		
		//从request中获取输入流
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		//获取xml的根元素
		Element root = doc.getRootElement();
		
		//把根元素中的子元素放到list中
		List<Element> list = root.elements();
		//将子元素放到map集合中
		for(Element e : list) {
			map.put(e.getName(), e.getText());
		}
		//关闭输入流
		ins.close();
		
		return map;
	}
	
	/**
	 * 将文本消息对象转为xml
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	public static String initText(String toUserName, String FromUserName, String content) {
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(FromUserName);
		text.setCreateTime(new Date().getTime());
		text.setMsgType(MESSAGE_TEXT);
		text.setContent("您发送的内容为：" + content);
		return textMessageToXml(text);
	}
	
	/**
	 * 主菜单
	 * @return
	 */
	public static String menuText() {
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎关注：\n\n");
		sb.append("1.自我介绍\n");
		sb.append("2.联系我们\n\n");
		sb.append("1.回复？调出此菜单。");
		return sb.toString();
	}
	
	/**
	 * 自我介绍
	 * @return
	 */
	public static String firstMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("我叫郭山，来自于河北邯郸。\n");
		return sb.toString();
	}
	
	/**
	 * 联系我们
	 * @return
	 */
	public static String secondMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("电话：13672071171\n");
		return sb.toString();
	}
	
	
}
