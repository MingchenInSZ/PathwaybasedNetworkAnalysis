package obodocumentparser.bio.lab;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.obolibrary.oboformat.model.Clause;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatConstants.OboFormatTag;
import org.obolibrary.oboformat.parser.OBOFormatParser;

import semanticsimilarity.bio.lab.relationship;
import serialization.bio.lab.Serialization;

public class OBODocumentParser {

	private void parse(String file) {
		String realFile = "dataRepository/" + file;
		OBODoc obo;
		try {
			obo = new OBOFormatParser().parse(new File(realFile));

			LinkedList<String> llist = new LinkedList<String>();
			LinkedList<String> ungo = new LinkedList<String>();
			HashMap<String, LinkedList<relationship>> pmap = new HashMap<String, LinkedList<relationship>>();
			for (Frame c : obo.getTermFrames()) {

				if (c.getTagValue("is_obsolete") != null) {
					System.out.println("is_obsolete "+ c.getTagValue("is_obsolete"));
					llist.add(c.getTagValue("is_obsolete").toString());
				} else { // if the term is not obsolete
					ungo.add(c.getId());
					LinkedList<relationship> pts = new LinkedList<relationship>();
					// store all the relationships
					if (c.getTagValue("is_a") != null) {
						// extract all the is_a relationship
						for (Object obj : c.getTagValues("is_a")) {
							relationship rels = new relationship(obj.toString(),(float) 0.8);
							pts.add(rels);
							System.out.println("is_a " + obj.toString());
						}
					}
					if (c.getTagValue("relationship") != null) {
						// extract all the part_of relationship
						for (Clause cc : c.getClauses(OboFormatTag.TAG_RELATIONSHIP)) {
							relationship rels = new relationship(cc.getValue2().toString(), (float) 0.6);
							pts.add(rels);
							System.out.println("part_of  " + cc.getValue2());
						}
					}
					pmap.put(c.getId(), pts); // put the (key,value) pair

				}
			}

			// dump all the objects
			Serialization.save(llist, "obsolete.out");
			Serialization.save(pmap, "parents.out");
			Serialization.save(ungo, "uniquegoes.out");
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
