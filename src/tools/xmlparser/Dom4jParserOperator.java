package tools.xmlparser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Dom4jParserOperator {
	private Document doc;
	private String path = "";
	
	public Dom4jParserOperator(String path) {
		this.path = path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void load() {
		File xml = new File(path);
		SAXReader reader = new SAXReader();
		
		try {
			doc = reader.read(xml);
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public Document getDocument() {
		return doc;
	}
	
	public Element getRootElement() {
		return doc.getRootElement();
	}
	
	public Element getElement(String key) {
		return getRootElement().element(key);
	}
	
	/**
	 * ͨ��ָ���� �ڵ��� �� ������Ϣ ��ȡElement
	 * 
	 * @param key ָ���Ľڵ���
	 * @param attrKey ָ����������
	 * @param attrVal ��Ӧ����ֵ
	 * @return ����Ŀ��ڵ�,���Ҳ�������Ҫ��Ľڵ�,�򷵻�null
	 */
	public Element getElement(String key, String attrKey, String attrVal) {
		Element tar = null;
		
		Iterator<Element> itor = getRootElement().elementIterator(key);
		while (itor.hasNext()) {
			Element e = itor.next();
			if (e.attribute(attrKey).getValue().equals(attrVal)) {
				tar = e;
				break;
			}
		}
		
		return tar;
	}
	
	public static void main(String[] args) {
		Dom4jParserOperator dpo = new Dom4jParserOperator("D://test.xml");
		dpo.load();
		
		System.err.println(dpo.getElement("book").elementText("author"));
		System.err.println(dpo.getElement("book").element("author").getText());
		
		/* test1 */
//		Iterator<Element> itor = dpo.getRootElement().elementIterator("book");
//		while (itor.hasNext()) {
//			Element e = itor.next();
//
//			System.out.println("--------------------------------------------");
//			System.out.println("Book " + e.attribute("id").getText() + " is print started!");
//			Iterator<Element> subItor = e.elementIterator();
//			while (subItor.hasNext()) {
//				Element subE = subItor.next();
//				System.out.println("Element Name: " + subE.getName() + " is " + subE.getStringValue());
// }
// }

		/* test2 */
		Element e = dpo.getElement("book", "id", "2");

		System.out.println("--------------------------------------------");
		System.out.println("Book " + e.attribute("id").getText() + " is print started!");
		Iterator<Element> subItor = e.elementIterator();
		while (subItor.hasNext()) {
			Element subE = subItor.next();
			System.out.println("Element Name: " + subE.getName() + " is " + subE.getStringValue());
		}
	}
}
