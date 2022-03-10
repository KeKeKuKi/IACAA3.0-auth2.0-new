//package com.gapache.security.filter;
//
//import com.alibaba.fastjson.JSONObject;
//import com.gapache.commons.model.JsonResult;
//import com.gapache.commons.security.RSAUtils;
//import com.gapache.commons.utils.ContextUtils;
//import com.gapache.commons.utils.IStringUtils;
//import com.gapache.security.holder.AccessCardHolder;
//import com.gapache.security.interfaces.ClientLoader;
//import com.gapache.security.model.AccessCard;
//import com.gapache.security.model.ClientDTO;
//import com.gapache.security.model.SecurityError;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.core.annotation.Order;
//import org.springframework.util.FileCopyUtils;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeMap;
//
///**
// * @author HuSen
// * @since 2021/1/25 4:54 下午
// */
//@Slf4j
//@Order(1)
//@WebFilter(filterName = "signCheckFilter", urlPatterns = "/*")
//public class SignCheckFilter implements Filter {
//
//    private static final String FILTER_APPLIED = "__scf_applied";
//
//    private static final String SIGN_FIELD = "sign";
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//
//        if (request.getAttribute(FILTER_APPLIED) != null) {
//            // 确保一次请求中这个filter只应用一次
//            chain.doFilter(request, response);
//            return;
//        }
//
//        request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
//
//        // 校验签名
//        AccessCard accessCard = AccessCardHolder.getContext();
//        if (accessCard.getSign()) {
//            try {
//                ClientLoader clientLoader = ContextUtils.getApplicationContext().getBean(ClientLoader.class);
//                ClientDTO clientDTO = clientLoader.load(accessCard.getClientId());
//                String privateKey = clientDTO.getPrivateKey();
//                // 没有公钥，不需要签名
//                if (StringUtils.isBlank(privateKey)) {
//                    chain.doFilter(req, res);
//                    return;
//                }
//
//                byte[] bytes = FileCopyUtils.copyToByteArray(request.getInputStream());
//                String body = IStringUtils.newString(bytes);
//                // 转json
//                JSONObject jsonObject = JSONObject.parseObject(body);
//                String sign = jsonObject.getString(SIGN_FIELD);
//                Set<String> keySet = jsonObject.keySet();
//                // 利用TreeMap来存储，键的顺序会自动排好
//                Map<String, Object> treeMap = new TreeMap<>();
//                keySet.forEach(key -> {
//                    Object o = jsonObject.get(key);
//                    // null的不需要参与签名计算 sign也不需要参与计算
//                    if (o != null && !StringUtils.equals(SIGN_FIELD, key)) {
//                        treeMap.put(key, o);
//                    }
//                });
//                StringBuilder sb = new StringBuilder();
//                sb.append(clientDTO.getClientId());
//                treeMap.forEach((k, o) -> sb.append(k).append(o.toString()));
//                sb.append(clientDTO.getClientId());
//
//                byte[] decryptSign = RSAUtils.decrypt(IStringUtils.getBytes(sign), RSAUtils.getPrivateKey(privateKey));
//                String check = IStringUtils.newString(decryptSign);
//
//                // 判断内容是否相同
//                if (StringUtils.equals(check, sb.toString())) {
//                    chain.doFilter(req, res);
//                    return;
//                }
//
//                // 验签失败
//                PrintWriter writer = res.getWriter();
//                writer.write(JSONObject.toJSONString(JsonResult.of(SecurityError.SIGN_INVALID)));
//                writer.flush();
//            } catch (Exception e) {
//                log.error("check sign fail:{}", accessCard.getClientId(), e);
//            } finally {
//                request.removeAttribute(FILTER_APPLIED);
//            }
//        } else {
//            chain.doFilter(req, res);
//        }
//    }
//}
