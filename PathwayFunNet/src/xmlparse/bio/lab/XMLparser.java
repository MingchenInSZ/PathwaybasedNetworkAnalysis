package xmlparse.bio.lab;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import logging.bio.lab.LogUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pathway.bio.lab.KEGGPathwayFilter;
import serialization.bio.lab.Serialization;
import dbconnection.bio.lab.DBConnection;

/**
 * XMLparser
 * 
 * @author mingchen
 * @date 2015Äê5ÔÂ3ÈÕ
 */
public class XMLparser {
	private SAXReader reader = null;
	private HashMap<String, HashSet<String>> pathways = null;
	private DBConnection dbconn = null;
	public XMLparser() {
		reader = new SAXReader();
		pathways = new HashMap<String, HashSet<String>>();
		dbconn = new DBConnection();
	}

	/**
	 * parse the xml (pathway file) and change the geneid to gene
	 * approved_symbol in hgnc
	 * 
	 * @param file
	 *            The file name of xml pathway file
	 */
	public void parse(File file) {
		try {
			Document doc = reader.read(file);
			HashSet<String> set = new HashSet<String>();
			Element root = doc.getRootElement();
			List<Element> list = root.elements("entry");
			for (Element e : list) {
				String type = e.attributeValue("type");
				if (type.trim().equals("gene")) {
					for(String g:e.attributeValue("name").split(" ")){
						String num = g.substring(4);
						String sql = "select approved_symbol from hgnc where entrez_gene_id=\""+num+"\";";
						ResultSet rs = dbconn.execQuery(sql);
						if (rs.next()) {
							set.add(rs.getString("approved_symbol"));
						}
					}
				}
			}
			String key = file.getName().substring(0, file.getName().length()-4);
			pathways.put(key, set);
			LogUtils.log("Pathway:"+key.substring(0,8)+" import done!["+set.size()+"]");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * parse all the xml in the directory
	 * 
	 * @param dirName
	 */
	public void parseUtil(String db,String org) {
		String fileName = "dataRepository/pathway"+File.separator + db+File.separator+org;
		File file = new File(fileName);
		if (!file.isDirectory()) {
			throw new RuntimeException("[" + fileName + "] is not a dir!");
		}
		for (File f : file.listFiles(new XMLFilter())) {
			parse(f);
		}
	}

	/**
	 * dump the kegg pathway to file
	 * 
	 * @param file
	 *            the file name of dumped keggpathway
	 */
	public void dumpPathway(String file){
		pathways = KEGGPathwayFilter.keggFilter(pathways, 5, 500);
		Serialization.save(pathways, file);
	}


}
