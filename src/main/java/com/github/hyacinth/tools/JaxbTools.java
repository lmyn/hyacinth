package com.github.hyacinth.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * 提供xml格式数据与bean之间序列化与反序列化的操作
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/7/26
 * Time: 13:57
 */
public class JaxbTools {

    private static Logger LOGGER = LoggerFactory.getLogger(JaxbTools.class);

    /**
     * 反序列化 string to bean
     *
     * @param src   string
     * @param clazz bean class
     * @return bean obj
     */
    public static <T> T unmarshal(String src, Class<T> clazz) {
        T result = null;
        try {
            Unmarshaller avm = JAXBContext.newInstance(clazz).createUnmarshaller();
            result = (T) avm.unmarshal(new StringReader(src));
        } catch (JAXBException e) {
            LOGGER.error("转换失败", e);
        }
        return result;
    }

    /**
     * 反序列化 xmlFile to bean
     *
     * @param xmlFile xmlFile
     * @param clazz   bean class
     * @param <T>     bean obj
     * @return
     */
    public static <T> T unmarshal(File xmlFile, Class<T> clazz) {
        T result = null;
        try {
            Unmarshaller avm = JAXBContext.newInstance(clazz).createUnmarshaller();
            result = (T) avm.unmarshal(xmlFile);
        } catch (JAXBException e) {
            LOGGER.error("转换失败", e);
        }
        return result;
    }

    /**
     * 序列化 bean to xml string
     *
     * @param jaxbElement bean obj
     * @return xml string
     */
    public static String marshal(Object jaxbElement) {
        StringWriter sw = null;
        try {
            Marshaller fm = JAXBContext.newInstance(jaxbElement.getClass()).createMarshaller();
            fm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            sw = new StringWriter();
            fm.marshal(jaxbElement, sw);
        } catch (JAXBException e) {
            LOGGER.error("转换失败", e);
        }
        return sw == null ? null : sw.toString();
    }
}
