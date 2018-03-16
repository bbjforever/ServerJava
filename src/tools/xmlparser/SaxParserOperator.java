package tools.xmlparser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mysql.jdbc.log.Log;

public class SaxParserOperator {
	private SAXParser parser = null;
	private SaxHandler handler = null;
	private String path = "";

	Map<String, String> mapTarget = new HashMap<String, String> ();
	
	public SaxParserOperator(String path) {
		this.path = path;
		
		handler = new SaxHandler();
		mapTarget.clear();
	}
	
	public void parser() {
		SAXParserFactory saxPF = SAXParserFactory.newInstance();
		try {
			parser = saxPF.newSAXParser();
			parser.parse(path, handler);
		}
		catch (SAXException e) {
	        System.out.println("SAX解析器解析函数异常 SAXException");
	        e.printStackTrace();
		}
		catch (IOException e) {
	        System.out.println("SAX解析器解析函数异常 IOException");
		}
		catch (ParserConfigurationException e) {
	        System.out.println("SAX解析器构造函数异常 ParserConfigurationException");
		}
		finally {
			
		}
	}
	
	public void addTarget(String target) {
		mapTarget.put(target, "");
	}
	
	public void getMapTarget(Map<String, String> map) {
		mapTarget = map;
	}
	
	public Map<String, String> getMapTarget() {
		return mapTarget;
	}
	
	public String getAttrVal(String key) {
		return mapTarget.get(key);
	}
	
	public void printMap() {
		System.out.println("Map target print start!");
		Iterator<Entry<String, String>> itor = mapTarget.entrySet().iterator();
		while (itor.hasNext()) {
			Entry<String, String> entry = itor.next();
			System.out.println("key: " + entry.getKey() + " xx val: " + entry.getValue());
		}
 	}
	
	/* 解析处理类 */
	private class SaxHandler extends DefaultHandler {
		String targElement = "";
		String fatherElement = "";
		Stack<String> stackTarget = new Stack<String> ();
		
		public SaxHandler() {
			
		}
		
		@Override
	    public void startDocument() throws SAXException {
	        super.startDocument();
	        
	        System.out.println("SAX解析开始");
	    }

		@Override
	    public void endDocument() throws SAXException {
	        super.endDocument();
	        
	        targElement = "";
	        stackTarget.clear();
	        
	        System.out.println("SAX解析结束");
	    }
		
	    @Override
	    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	        //调用DefaultHandler类的startElement方法
	        super.startElement(uri, localName, qName, attributes);

	        stackTarget.push(qName);
	        targElement = qName;

//			System.out.println("======================开始遍历某一元素的内容=================");
//			System.out.println("startElement uri: " + uri + " xx localName: " + localName + " xx qName: " + qName);
			// 不知道属性的名称以及个数，遍历属性名以及属性值
			int num = attributes.getLength();
			for (int i = 0; i < num; i++) {
//				System.out.print("第" + (i + 1) + "个属性名是：" + attributes.getQName(i));
//				System.out.println("---属性值是：" + attributes.getValue(i));
				
//				if (attributes.getQName(i).equals("id")) {
//					attributes.getValue(i);
//				}
			}
		}
	    
	    @Override
	    public void endElement(String uri, String localName, String qName) throws SAXException {
	    	super.endElement(uri, localName, qName);
	    	
	    	targElement = "";
	        stackTarget.pop();
	    	
//            System.out.println("endElement uri: " + uri + " xx localName: " + localName + " xx qName: " + qName);
//            System.out.println("======================遍历某一元素的内容结束=================");
	    }
	    
	    @Override
	    public void characters(char[] ch, int start, int length) throws SAXException {
	        super.characters(ch, start, length);
	        
	        if (stackTarget.peek().equals("")) {
	        	/* TODO:这里用来查看父结点 */
	        }
	        
	        System.out.println("targElement: " + targElement);
	        if (mapTarget.containsKey(targElement)) {
		        System.out.println("contains targAttr: " + targElement);
	        	mapTarget.put(targElement, new String(ch, start, length));
	        }

            System.out.println("characters string: " + new String(ch, start, length));
	    }
	}
	
	public static void main(String[] args) {
		SaxParserOperator op = new SaxParserOperator("D:\\WeaponGirl\\Server\\WeaponGirl\\conf\\argument.xml");
		op.addTarget("maxlevel");
		op.addTarget("startnum");
		op.addTarget("playeratttime");
		op.addTarget("openTime");
		
		op.parser();
		op.printMap();
	}
}
