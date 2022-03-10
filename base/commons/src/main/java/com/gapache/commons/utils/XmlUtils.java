//package com.gapache.commons.utils;
//
//import com.gapache.commons.model.ResponseBody;
//import com.gapache.commons.model.ResponseHead;
//import com.gapache.commons.model.XmlResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.dom4j.dom.DOMElement;
//
//import java.io.*;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author HuSen
// * @since 2021/1/18 10:31 上午
// */
//@Slf4j
//public final class XmlUtils {
//
//    private final static String HEAD_KEY = "head";
//    private final static String RESP_INFO_KEY = "RespInfo";
//
//    /**
//     * 读取报文格式
//     *
//     * @param file
//     * @return
//     * @throws FileNotFoundException
//     */
//    private String sendStyle(File file) throws FileNotFoundException {
//        InputStream is = new FileInputStream(file);
//        return getStyle(is);
//    }
//
//
//    private String getStyle(InputStream is) {
//        BufferedReader br = null;
//        StringBuilder sb = new StringBuilder();
//        try {
//            br = new BufferedReader(new InputStreamReader(is));
//            String s;
//            while ((s = br.readLine()) != null) {
//                sb.append(s);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (br != null) {
//                    br.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 封装报文
//     * @param form
//     * @param headFileInputStream
//     * @param bodyFileInputStream
//     * @return
//     */
//    String format(Map<String, String> form, InputStream headFileInputStream, InputStream bodyFileInputStream) throws DocumentException {
//        //报文头
//        Element headElement = formatHead(form, headFileInputStream);
//        //报文体
//        Element bodyElement = formatBody(form, bodyFileInputStream);
//
//        Document docRes = DocumentHelper.createDocument();
//        Element rootElement = docRes.addElement("document");
//        Element requestElement = rootElement.addElement("request").addAttribute("id", "request");
//        requestElement.add(headElement);
//        requestElement.add(bodyElement);
//
//        return docRes.asXML();
//    }
//
//    /**
//     * 封装报文头
//     * @param form
//     * @param inputStream
//     * @return
//     * @throws DocumentException
//     * @throws FileNotFoundException
//     */
//    public Element formatHead(Map<String, String> form, InputStream inputStream) throws DocumentException {
//        Document docStyle = DocumentHelper.parseText(getStyle(inputStream));
//        Element headStyle = docStyle.getRootElement();
//        List<Element> list = headStyle.elements();
//        Element headElement = new DOMElement("head");
//        handleElement(list,headElement,form);
//        return headElement;
//    }
//
//    /**
//     * 封装报文体
//     * @param form 接口请求对象的属性map
//     * @param inputStream 对应接口的bodyXml
//     * @return
//     * @throws DocumentException
//     */
//    private Element formatBody(Map<String, String> form, InputStream inputStream) throws DocumentException {
//        Document docStyle = DocumentHelper.parseText(getStyle(inputStream));
//        Element bodyStyle = docStyle.getRootElement();
//        List<Element> list = bodyStyle.elements();
//        Element bodyElement = new DOMElement("body");
//        handleElement(list,bodyElement,form);
//        return bodyElement;
//    }
//    private void handleElement(List<Element> list,Element bodyElement,Map<String, String> form){
//        for (Element e : list) {
//            String tagName = e.attributeValue("tagName");
//            String value = form.get(tagName);
//            if (value == null) {
//                String defaultValue = e.attributeValue("defaultValue");
//                if (defaultValue != null) {
//                    value = defaultValue;
//                }
//            }
//            if (value != null) {
//                bodyElement.addElement(tagName).setText(value);
//            }
//        }
//    }
//
//    /**
//     * 解析回调报文
//     * @param response XML响应字符串
//     * @return 返回解析后的原始MAP
//     * @throws DocumentException 如果文档解析异常抛出该异常
//     */
//    public Map<String, Object> parse(String response) throws DocumentException {
//        Map<String, Object> returnMap = new HashMap<>();
//        Document docStyle = DocumentHelper.parseText(response);
//        List<Element> headList = docStyle.getRootElement().element("request").element("head").elements();
//        Map<String, Object> headMap = parseHead(headList);
//
//        List<Element> bodyList = docStyle.getRootElement().element("request").element("body").elements();
//        Map<String, Object> bodyMap = parseBody(bodyList);
//
//
//        returnMap.putAll(headMap);
//        returnMap.putAll(bodyMap);
//        return returnMap;
//    }
//
//    /**
//     * 解析报文为指定的实体类
//     *
//     * @param response XML响应字符串
//     * @param t 泛型实例
//     * @return 返回响应报文对应的实体类
//     * @throws DocumentException 如果文档解析异常抛出该异常
//     */
//    @SuppressWarnings("unchecked")
//    @Deprecated
//    public <T extends XmlResponse> T parse(String response, T t) throws DocumentException {
//        Map<String, Object> headMap;
//        Map<String, Object> bodyMap;
//        final Map<String, Object> headMapLowerCase = new HashMap<>();
//        final Map<String, Object> bodyMapLowerCase = new HashMap<>();
//        Document docStyle = DocumentHelper.parseText(response);
//        // 获取HEAD节点
//        List<Element> headList = docStyle.getRootElement().element("response").element("head").elements();
//        headMap = parseHead(headList);
//        headMap.forEach((key, value) -> {
//            String keyLowerCase = StringUtils.uncapitalize(key);
//            if (!HEAD_KEY.equalsIgnoreCase(key)) {
//                headMapLowerCase.putIfAbsent(keyLowerCase, value);
//            }
//        });
//        // 获取BODY节点
//        List<Element> bodyList = docStyle.getRootElement().element("response").element("body").elements();
//        bodyMap = parseBody(bodyList);
//        bodyMap.forEach((key, value) -> {
//            String keyLowerCase = StringUtils.uncapitalize(key);
//            if (!RESP_INFO_KEY.equalsIgnoreCase(key)) {
//                bodyMapLowerCase.putIfAbsent(keyLowerCase, value);
//            }
//        });
//        try {
//            BeanUtils.populate(t, bodyMapLowerCase);
//            // 设置head
//            ResponseHead headData = new ResponseHead();
//            headMap.forEach((key, value) -> headMapLowerCase.putIfAbsent(StringUtils.uncapitalize(key), value));
//            BeanUtils.populate(headData, headMapLowerCase);
//            t.setHead(headData);
//            // 设置respInfo
//            Map<String, Object> respMap = (Map<String, Object>) bodyMap.get(RESP_INFO_KEY);
//            Map<String, Object> respMapLowerCase = new HashMap<>(bodyMap.size());
//            ResponseBody respInfo = new ResponseBody();
//            respMap.forEach((key, value) -> respMapLowerCase.putIfAbsent(StringUtils.uncapitalize(key), value));
//            BeanUtils.populate(respInfo, respMapLowerCase);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            log.error("解析报文为指定的实体类 {} 出错：{}", t.getClass().getName(), e);
//        }
//        return t;
//    }
//
//    /**
//     * 解析报文头
//     * @param list
//     * @return
//     */
//    public Map<String, Object> parseHead(List<Element> list){
//        Map<String, Object> headMap = new HashMap<>();
//        for (Element e : list) {
//            if (e.getText() != null && !"".equals(e.getText())) {
//                headMap.put(StringUtils.uncapitalize(e.getName()), e.getText());
//            }
//        }
//        return headMap;
//    }
//
//    /**
//     * 解析RespInfo
//     * @param list
//     * @return
//     */
//    public Map<String, Object> parseRespInfo(List<Element> list){
//        Map<String, Object> headMap = new HashMap<>();
//        for (Element e : list) {
//            if (e.getText() != null && !"".equals(e.getText())) {
//                headMap.put(StringUtils.uncapitalize(e.getName()), e.getText());
//            }
//        }
//        return headMap;
//    }
//
//    /**
//     * 解析报文体
//     * @param list
//     * @return
//     * @throws DocumentException
//     */
//    public Map<String, Object> parseBody(List<Element> list) {
//        Map<String, Object> bodyMap = new HashMap<>();
//        for (Element e : list) {
//            //包含子元素
//            final String name = StringUtils.uncapitalize(e.getName());
//            if (e.elements().size() != 0) {
//                //重复标签合并成list
//                if (bodyMap.containsKey(name)) {
//                    Object o = bodyMap.get(name);
//                    if (o instanceof ArrayList) {
//                        ArrayList<Object> oList = (ArrayList<Object>) o;
//                        oList.add(parseBody(e.elements()));
//                    } else {
//                        ArrayList<Object> oList = new ArrayList<>();
//                        oList.add(o);
//                        oList.add(parseBody(e.elements()));
//                        bodyMap.put(name, oList);
//                    }
//                } else {
//                    bodyMap.put(name, parseBody(e.elements()));
//                }
//                //无子元素且非空
//            } else if (e.getText() != null && !"".equals(e.getText())) {
//                //重复标签合并成list
//                if (bodyMap.containsKey(name)) {
//                    Object o = bodyMap.get(name);
//                    if (o instanceof ArrayList) {
//                        ArrayList<Object> oList = (ArrayList<Object>) o;
//                        oList.add(e.getText());
//                    } else {
//                        ArrayList<Object> oList = new ArrayList<>();
//                        oList.add(o);
//                        oList.add(e.getText());
//                        bodyMap.put(name, oList);
//                    }
//                } else {
//                    bodyMap.put(name, e.getText());
//                }
//            }
//        }
//        return bodyMap;
//    }
//}
