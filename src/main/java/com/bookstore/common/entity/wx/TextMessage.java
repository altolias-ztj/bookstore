package com.bookstore.common.entity.wx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.Map;

@Data
@JacksonXmlRootElement(localName = "xml")
public class TextMessage {
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "ToUserName")
    private String ToUserName;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "FromUserName")
    private String FromUserName;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "CreateTime")
    private String CreateTime;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "MsgType")
    private String MsgType = "text";
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Content")
    private String Content;

    public TextMessage(Map<String, String> map) {
        this.ToUserName = map.get("FromUserName");
        this.FromUserName = map.get("ToUserName");
        this.CreateTime = System.currentTimeMillis() / 1000 + "";
    }
}
