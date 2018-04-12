package tools.xmlparser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomParserOperator {
	private class CacheData {
		private Map<String, String> mapAttr;
		private Map<String, CacheData> mapChild;
		
		private String val = null;
		private String nodeName = "";
		
		CacheData(String name) {
			this.nodeName = name;
		}
		
		private void setAttrMap(Map<String, String> map) {
			this.mapAttr = map;
		}
		
		private void putAttr(String key, String val) {
			if (this.mapAttr == null) {
				this.mapAttr = new HashMap<String, String>();
			}
			
			this.mapAttr.put(key, val);
		}
		
		private Map<String, String> getAttrMap() {
			return this.mapAttr;
		}
		
		private void setChildMap(Map<String, CacheData> map) {
			this.mapChild = map;
		}
		
		private void putChild(String key, CacheData val) {
			if (val == null) {
				return;
			}
			
			if (this.mapChild == null) {
				this.mapChild = new HashMap<String, CacheData>();
			}
			
			this.mapChild.put(key, val);
		}
		
		private Map<String, CacheData> getChildMap() {
			return this.mapChild;
		}
	}
	
	private Document doc = null;
	private String path = "";
	
	private CacheData cacheData = null;
	
	public DomParserOperator(String path) {
		setPath(path);
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void parseTest() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			this.doc = db.parse(path);
			NodeList nodelist = doc.getElementsByTagName("book");
			System.out.println("nodelength: " + nodelist.getLength());

			for (int i = 0; i < nodelist.getLength(); i++) {
				Node node = nodelist.item(i);

				NamedNodeMap attrs = node.getAttributes();
				System.out.println("NamedNodeMap length: " + attrs.getLength());
				for (int j = 0; j < attrs.getLength(); j++) {
					Node attr = attrs.item(j);

					System.out.print("attr localname: " + attr.getLocalName());
					System.out.print(" xx nodename: " + attr.getNodeName());
					System.out.print(" xx val: " + attr.getNodeValue());
					System.out.println(" xx textcontent: " + attr.getTextContent());
				}

				NodeList subNode = node.getChildNodes();
				for (int j = 0; j < subNode.getLength(); j++) {
					Node n = subNode.item(j);
					if (n.getNodeType() == Node.ELEMENT_NODE) {
						System.out.print("Element localname: " + n.getLocalName());
						System.out.print(" xx nodename: " + n.getNodeName());
						System.out.print(" xx val: " + n.getFirstChild().getNodeValue());
						System.out.println(" xx textcontent: " + n.getTextContent());
					}
					else if (n.getNodeType() == Node.COMMENT_NODE) {
						System.out.print("Comment localname: " + n.getLocalName());
						System.out.print(" xx nodename: " + n.getNodeName());
						System.out.println(" xx textcontent: " + n.getTextContent());
					}
					else {
						System.out.print("Other localname: " + n.getLocalName());
						System.out.print(" xx nodename: " + n.getNodeName());
						System.out.print(" xx val: " + n.getNodeValue());
						System.out.println(" xx textcontent: " + n.getTextContent());
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			this.doc = db.parse(path);

			cacheData = parse(doc.getDocumentElement());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private CacheData parse(Node node) {
		CacheData data = null;
		
		if (node.hasAttributes()) {
			/* 拥有属性 */
			Map<String, String> mapAttr_ = parseAttr(node.getAttributes());
			if (mapAttr_ != null) {
				data = new CacheData(node.getNodeName());
				data.setAttrMap(mapAttr_);
			}
		}
		
		if (node.hasChildNodes()) {
			/* 拥有子节点 */
			NodeList childNodes_ = node.getChildNodes();
			for (int cInd = 0; cInd < childNodes_.getLength(); cInd++) {
				Node child_ = childNodes_.item(cInd);

				switch (child_.getNodeType()) {
				case Node.TEXT_NODE:
					if (!child_.getNodeValue().replaceAll("\\s", "").equals("")) {
						data = data == null ? new CacheData(node.getNodeName()) : data;
						data.val = child_.getNodeValue();
					}
					
					break;
				case Node.COMMENT_NODE:
					/* 注释行 */
					
					break;
				case Node.ELEMENT_NODE:
					if (data == null) {
						data = new CacheData(node.getNodeName());
					}
					
					CacheData cd = parse(child_);
					data.putChild(child_.getNodeName(), cd);
					
					break;
				}
			}
		}
		
		return data;// 有可能为null
	}
	
	private Map<String, String> parseAttr(NamedNodeMap attrs) {
		Map<String, String> map = new HashMap<String, String>();
		for (int j = 0; j < attrs.getLength(); j ++) {
			Node attr = attrs.item(j);

			map.put(attr.getNodeName(), attr.getNodeValue());
			
			print("attr", attr);
		}
		
		return map;
	}
	
	private void print(String key, Node node) {
		System.out.print(key + " localname: " + node.getLocalName());
		System.out.print(" xx nodename: " + node.getNodeName());
		System.out.println(" xx val: " + node.getNodeValue());
	}
	
	private void printCacheData(CacheData cacheData) {
		if (cacheData == null) {
			return;
		}
		
//		System.out.println("############ NodeName: " + cacheData.nodeName + " print started! ###########");
		
		Map<String, String> mapA = cacheData.getAttrMap();
		if (mapA != null) {
//			System.out.println("--------------- Attribute Start ---------------");
			Iterator<Entry<String, String>> itor = mapA.entrySet().iterator();
			while (itor.hasNext()) {
				Entry<String, String> entry = itor.next();
				System.out.println("attrName: " + entry.getKey() + " ----- attrVal: " + entry.getValue());
			}
//			System.out.println("--------------- Attribute End ---------------");
		}
		
		Map<String, CacheData> mapC = cacheData.getChildMap();
		if (mapC != null) {
//			System.out.println("--------------- Child Start ---------------");
			Iterator<Entry<String, CacheData>> itor = mapC.entrySet().iterator();
			while (itor.hasNext()) {
				Entry<String, CacheData> entry = itor.next();
				CacheData c = entry.getValue();
				printCacheData(c);
			}
//			System.out.println("--------------- Child End ---------------");
		}
		
		if (cacheData.val != null) {
//			System.out.println("--------------- Val Start ---------------");
			System.out.println("valName: " + cacheData.nodeName + " ----- valVal: " + cacheData.val);
//			System.out.println("--------------- Val End ---------------");
		}
		
//		System.out.println("############ NodeName: " + cacheData.nodeName + " print End! ###########");
	}
	
	public static void main(String[] args) {
		System.err.println(String.class.getSimpleName());
		
		DomParserOperator d = new DomParserOperator("D:\\test.xml");
		d.load();
		d.printCacheData(d.cacheData);
		
		if (true)
			return;
		
		//创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //创建一个DocumentBuilder的对象
        try {
            //创建DocumentBuilder对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过DocumentBuilder对象的parser方法加载books.xml文件到当前项目下
            Document document = db.parse("d:\\book.xml");
            //获取所有book节点的集合
            NodeList bookList = document.getElementsByTagName("book");
            //通过nodelist的getLength()方法可以获取bookList的长度
            System.out.println("一共有" + bookList.getLength() + "本书");
            //遍历每一个book节点
            for (int i = 0; i < bookList.getLength(); i++) {
                System.out.println("=================下面开始遍历第" + (i + 1) + "本书的内容=================");
                //通过 item(i)方法 获取一个book节点，nodelist的索引值从0开始
                Node book = bookList.item(i);
                System.err.println("xxxxxxx bookTyoe: " + book.getNodeType());
                //获取book节点的所有属性集合
                NamedNodeMap attrs = book.getAttributes();
                System.out.println("第 " + (i + 1) + "本书共有" + attrs.getLength() + "个属性");
                //遍历book的属性
                for (int j = 0; j < attrs.getLength(); j++) {
                    //通过item(index)方法获取book节点的某一个属性
                    Node attr = attrs.item(j);
                    System.err.println("xxxxxxx attrTyoe: " + attr.getNodeType());
                    //获取属性名
                    System.out.print("属性名：" + attr.getNodeName());
                    //获取属性值
                    System.out.println("--属性值" + attr.getNodeValue());
                }
                //解析book节点的子节点
                NodeList childNodes = book.getChildNodes();
                //遍历childNodes获取每个节点的节点名和节点值
                System.out.println("第" + (i+1) + "本书共有" + 
                childNodes.getLength() + "个子节点");
                for (int k = 0; k < childNodes.getLength(); k++) {
                    //区分出text类型的node以及element类型的node

                    System.err.println("xxxxxxx chilkdTyoe: " + childNodes.item(k).getNodeType());
//                    System.err.println("xxx   " + childNodes.item(1).getChildNodes().get);
                    if (false || childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        //获取了element类型节点的节点名
                        System.out.print("第" + (k + 1) + "个节点的节点名：" 
                        + childNodes.item(k).getNodeName());
                        //获取了element类型节点的节点值
                        System.out.println("--是否有子节点：" + childNodes.item(k).hasChildNodes());
                        System.out.println("--是否有子节点22：" + childNodes.item(k).getChildNodes().item(0).getNodeValue());
                        System.out.println("--是否有子节点33：" + childNodes.item(k).getChildNodes().item(0).getNodeType());
                        System.out.println("--子节点num：" + childNodes.item(k).getChildNodes().getLength());
                        System.out.println("--是否有属性：" + childNodes.item(k).hasAttributes());
                        System.out.println("--节点值是：" + childNodes.item(k).getFirstChild().getNodeValue());
                        System.out.println("--节点值是：" + childNodes.item(k).getFirstChild().getNodeType());
                        System.out.println("--节点值是：" + childNodes.item(k).getTextContent());
                    }
                }
                System.out.println("======================结束遍历第" + (i + 1) + "本书的内容=================");
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
}
