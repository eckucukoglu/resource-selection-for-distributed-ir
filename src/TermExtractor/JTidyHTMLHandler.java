package TermExtractor;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

public class JTidyHTMLHandler {
	
	String getDocument(InputStream is) {
		
		Properties oProps = new Properties();
		oProps.setProperty("new-blocklevel-tags", "doc docno dochdr");

		Tidy tidy = new Tidy();
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setShowErrors(0);
		tidy.setConfigurationFromProps(oProps);
		Document root = tidy.parseDOM(is, null);
		
		Element rawDoc = root.getDocumentElement();
		String body = getBody(rawDoc);
		
		return body;
	}
	
	protected String getBody(Element rawDoc) {
		if (rawDoc == null) {
			return null;
		}
		
		String body = "";
		NodeList children = rawDoc.getElementsByTagName("body");
		if (children.getLength() > 0) {
			body = getText(children.item(0));
		}
		return body;
	}
	
	protected String getText(Node node) {
		NodeList children = node.getChildNodes();
		StringBuffer sb = new StringBuffer();
		final char[] htmlComment = { '<', '!', '-', '-'};
        char[] dst = new char[4];
		
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
            if (child.getNodeName() != null && 
            		( child.getNodeName().equals("dochdr") || 
            		child.getNodeName().equals("docno")) )
            	continue;
            
			switch (child.getNodeType()) {
				case Node.ELEMENT_NODE:
				sb.append(getText(child));
				sb.append(" ");
				break;
				case Node.TEXT_NODE:
				
		        if (child.getNodeValue().length() >= 4) {
	        		child.getNodeValue().getChars(0, 4, dst, 0);
		            
		            if (Arrays.equals(htmlComment, dst)) {
		            	continue;
		            }
		        }
		            
				sb.append(((Text) child).getData());
	            
				break;
			}
		}
		return sb.toString();
	}
}