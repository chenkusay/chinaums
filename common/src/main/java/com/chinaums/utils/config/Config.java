package com.chinaums.utils.config;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    private static final String DEFAULT_CONFIG_NAME = "config.xml";
    private static final String MODULE_CONFIG_ELEMENT = "module-config";
    private static final String GLOBAL_PROPERTYS_ELEMENT = "global-propertys";
    private static final String PROPERTY_ELEMENT = "property";
    private static final String MODULES_ELEMENT = "modules";
    private static final String MODULE_ELEMENT = "module";
    private static Map instances = new HashMap(5);

    private Properties globalProp = null;

    private Map modulesMap = null;

    private String cfgFileName = null;

    private long cfgLastModified = System.currentTimeMillis();

    private File pFile = null;

    private Config(String fileName)
    {
        this.cfgFileName = fileName;
        try {
            this.pFile = Resources.getResourceAsFile(this.cfgFileName);
        } catch (IOException ioe) {
            throw new RuntimeException("初始化配置文件出错:" + ioe.getMessage());
        }

        readConfig();
    }

    public static synchronized Config getInstance() {
        return getInstance("config.xml");
    }

    public static synchronized Config getInstance(String name)
    {
        if (instances.get(name) == null) {
            Config config = new Config("config.xml");
            instances.put(name, config);
            return config;
        }
        return (Config)instances.get(name);
    }

    private void readConfig()
    {
        InputStream is = null;
        Document doc = null;
        try {
            if (this.pFile == null) {
                this.pFile = Resources.getResourceAsFile(this.cfgFileName);
            }
            is = new FileInputStream(this.pFile);
            doc = XmlTransformUtil.builerDocument(is);

            Element root = doc.getDocumentElement();
            String rootname = root.getNodeName();
            if (!"module-config".equals(rootname)) {
                throw new IllegalArgumentException(
                        "Error while configuring Module.  The root tag of the MODULE configuration XML document must be 'module-config'.");
            }

            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == 1)
                    if ("global-propertys".equals(child.getNodeName()))
                        this.globalProp = parseGlobal((Element)child);
                    else if ("modules".equals(child.getNodeName()))
                        this.modulesMap = parseModules((Element)child);
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            throw new RuntimeException("初始化配置文件出错:" + e.getMessage());
        }
        finally
        {
            try {
                is.close();
                doc = null;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void resetConfig()
    {
        if (this.globalProp != null) {
            this.globalProp.clear();
        }
        if (this.modulesMap != null) {
            this.modulesMap.clear();
        }
        readConfig();
    }

    protected static Properties parseGlobal(Element globalEle)
    {
        NodeList children = globalEle.getChildNodes();

        Properties globalProp = new Properties();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if ((node.getNodeType() == 1) &&
                    ("property".equals(node.getNodeName()))) {
                String propKey = ((Element)node).getAttribute("name");
                String propValue = ((Element)node).getAttribute("value");
                if ((propKey == null) || (propKey.equals(""))) {
                    System.err
                            .println("WARN: Config.parseGlobal Cause: name attribute must be not null " +
                                    ((Element)node).toString());
                }

                if (globalProp.getProperty(propKey) != null)
                    System.err
                            .println("WARN: Config.parseGlobal Cause: name attribute already define " +
                                    ((Element)node).toString());
                else {
                    globalProp.put(propKey, propValue);
                }
            }
        }

        return globalProp;
    }

    protected static Map parseModules(Element modulesEle)
    {
        NodeList children = modulesEle.getChildNodes();
        Map tempMap = new HashMap();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if ((node.getNodeType() == 1) &&
                    ("module".equals(node.getNodeName()))) {
                Element tempEle = (Element)node;
                Module module = new Module();
                if (tempEle.getAttribute("id") != null) {
                    module.setID(tempEle.getAttribute("id"));
                }

                if (!"".equals(tempEle.getAttribute("ip"))) {
                    module.setIP(tempEle.getAttribute("ip"));
                }
                if (!"".equals(tempEle.getAttribute("port"))) {
                    module.setPort(Integer.parseInt(tempEle
                            .getAttribute("port")));
                }
                if (!"".equals(tempEle.getAttribute("name"))) {
                    module.setName(tempEle.getAttribute("name"));
                }

                NodeList paraChilden = tempEle.getChildNodes();
                Properties prop = new Properties();

                for (int j = 0; j < paraChilden.getLength(); j++) {
                    Node paraNode = paraChilden.item(j);
                    if (paraNode.getNodeType() == 1) {
                        String key = ((Element)paraNode)
                                .getElementsByTagName("name").item(0)
                                .getFirstChild().getNodeValue();
                        String value = ((Element)paraNode)
                                .getElementsByTagName("value").item(0)
                                .getFirstChild().getNodeValue();
                        prop.put(key, value);
                    }
                }
                module.setProperties(prop);
                tempMap.put(module.getID(), module);
            }
        }

        return tempMap;
    }

    public Properties getGlobalProp() {
        synchFileUpdate();
        return this.globalProp;
    }

    public String getGlobalProp(String key) {
        if (this.globalProp == null) {
            return null;
        }
        synchFileUpdate();
        String value = this.globalProp.getProperty(key);

        return value;
    }

    public void setGlobalProp(Properties globalProp) {
        this.globalProp = globalProp;
    }

    public void setModulesMap(Map modulesMap) {
        this.modulesMap = modulesMap;
    }

    public Map getModulesMap() {
        synchFileUpdate();
        return this.modulesMap;
    }

    public Module getModule(String key) {
        synchFileUpdate();
        Object value = this.modulesMap.get(key);

        return (Module)value;
    }

    public static int getInt(String key) {
        String value = getInstance().getGlobalProp(key);
        return Integer.parseInt(value);
    }

    public static String getString(String key) {
        String value = getInstance().getGlobalProp(key);

        return value;
    }

    private static void checkNULL(String key, Object value) {
        if (value == null)
            throw new RuntimeException("not found key:" + key);
    }

    private void synchFileUpdate()
    {
        long newTime = this.pFile.lastModified();
        if ((newTime == 0L) || (this.cfgLastModified == 0L))
            System.err.println(this.pFile + " file does not exist!");
        else if (newTime > this.cfgLastModified) {
            resetConfig();
        }
        this.cfgLastModified = newTime;
    }

    public static void main(String[] args)
            throws Exception
    {
        try
        {
            for (int i = 0; i < 100; i++) {
                Config config = getInstance();

                String var = config.getGlobalProp("alertPort");

                System.out.println(var);
                Thread.sleep(5000L);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
