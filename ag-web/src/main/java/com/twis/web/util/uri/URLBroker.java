package com.twis.web.util.uri;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author yxm
 * @since 2018/3/18
 */
public abstract class URLBroker {

    private String serverUrl;

    private String serverDomain;

    private String serverPort;

    private String protocol;

    private String path;

    private Map<String, Object> queryDatas = new HashMap<String, Object>();

    private Set<String> tokens = new HashSet<String>();

    private boolean isRendered;

    private String renderedUrl;

    public Set<String> getTokens() {
        return tokens;
    }

    protected URLBroker(URLBroker urlBroker) {
        this.serverUrl = urlBroker.serverUrl;

        this.path = urlBroker.getPath();
        this.serverDomain = urlBroker.serverDomain;
        this.serverPort = urlBroker.serverPort;
        this.protocol = urlBroker.protocol;
        this.tokens = urlBroker.tokens;
    }

    public URLBroker() {

    }

    public String toString() {
        if (!isRendered) {
            renderedUrl = render();
            this.isRendered = true;
        }
        return renderedUrl;
    }

    /**
     * 执行url渲染,serverUrl如果以/结尾去掉/ path不为空才拼接到serverUrl上，path不为空要判断前面有没有/，没有要加上
     *
     * @return
     */
    private String render() {
        if (StringUtils.isNotBlank(serverUrl)) {
            StringBuilder result = new StringBuilder();
            boolean deletePrefix = false;
            if (serverUrl.endsWith("/")) {
                deletePrefix = true;
            }
            result.append(serverUrl);
            if (StringUtils.isNotBlank(path)) {
                if (path.startsWith("/")) {
                    if (deletePrefix) {
                        result.append(path.replaceFirst("/", ""));
                    }
                    else {
                        result.append(path);
                    }
                }
                else {
                    if (deletePrefix) {
                        result.append(path);
                    }
                    else {
                        result.append("/" + path);
                    }
                }
            }
            String resultUrl = performTokens(result.toString());
            return resultUrl + renderQueryData();
        }
        return "";
    }

    private String performTokens(String serverUrl) {
        for (String token : tokens) {
            serverUrl = serverUrl.replaceAll("\\{" + token + "\\}", queryDatas.get(token) == null ? "" : String.valueOf(queryDatas.get(token)));
            queryDatas.remove(token);
        }
        return serverUrl;
    }

    private String renderQueryData() {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (String key : queryDatas.keySet()) {
            if (i == 0) {
                result.append("?");
            }
            if (i > 0) {
                result.append("&");
            }
            result.append(key);
            result.append("=");
            Object value = queryDatas.get(key);
            result.append(value == null ? "" : value);
            i++;
        }
        return result.toString();
    }

    public URLBroker addQueryData(String key, Object value) {
        this.queryDatas.put(key, value);
        return this;
    }

    public abstract URLBroker newInstance();

    public void addToken(String token) {
        tokens.add(token);
    }

    /** @return the serverUrl */
    String getServerUrl() {
        return serverUrl;
    }

    /** @param serverUrl the serverUrl to set */
    void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /** @return the serverDomain */
    String getServerDomain() {
        return serverDomain;
    }

    /** @param serverDomain the serverDomain to set */
    void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }

    /** @return the serverPort */
    String getServerPort() {
        return serverPort;
    }

    /** @param serverPort the serverPort to set */
    void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    /** @return the protocol */
    String getProtocol() {
        return protocol;
    }

    /** @param protocol the protocol to set */
    void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /** @return the path */
    String getPath() {
        return path;
    }

    /** @param path the path to set */
    void setPath(String path) {
        this.path = path;
    }

}
