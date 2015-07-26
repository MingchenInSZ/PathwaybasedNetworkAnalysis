package utils.bio.lab;

import xmlparse.bio.lab.XMLparser;

public class XMLparserUtils {
	public static void main(String[] args) {
		XMLparser xp = new XMLparser();
		xp.parseUtil("kegg", "hsa");
		xp.dumpPathway("keggpathway.out");

	}

}
