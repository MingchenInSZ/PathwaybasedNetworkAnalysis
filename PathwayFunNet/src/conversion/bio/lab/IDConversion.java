package conversion.bio.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;

import dbconnection.bio.lab.DBConnection;

/**
 * 
 * @author mingchen
 * @date May 1st, 2015
 * 
 */
public class IDConversion {

	private DBConnection dbconn = null;

	public IDConversion() {
		dbconn = new DBConnection();
	}

	/**
	 * get hgnc id from swiss prot ac in spmap database
	 * 
	 * @param spId
	 * @return
	 */
	public String swiss2HGNC(String spId){
		String sql = "select idNum from spmap where dbName ='HGNC' and protAc = '"+spId+"';";
		String hgnc = "";
		ResultSet rs = dbconn.execQuery(sql);
		try {
			while (rs.next()) {
				hgnc = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hgnc;
	}

	/**
	 * get the approved_symbol from hgnc_id in hgnc database
	 * 
	 * @param hgnc
	 * @return
	 */
	public String getApprovedsymbolFromHGNC(String hgnc) {
		String sql = "select approved_symbol from hgnc where hgnc_id = '"+hgnc+"';";
		String symbol = "";
		ResultSet rs = dbconn.execQuery(sql);
		try {
			while (rs.next()) {
				symbol = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return symbol;
	}

	/**
	 * convert swissport id to approved symbol from hgnc database
	 * 
	 * @param swissprot
	 * @return
	 */
	public String swiss2ApprovedSymbol(String swissprot) {
		String symbol = "";
		String sql = "select approved_symbol from hgnc where uniprot_id = \""
				+ swissprot + "\";";
		ResultSet rs = dbconn.execQuery(sql);
		try {
			while (rs.next()) {
				symbol = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return symbol;
	}

	/**
	 * convert from swiss prot id to unigene cluster
	 * 
	 * @param swissprot
	 * @return
	 */
	public String swiss2Unigene(String swissprot) {
		String unigeneCluster = "";
		String sql = "select idNum from spmap where dbName = 'UniGene' and protAc ='"
				+ swissprot + "';";
		ResultSet rs = dbconn.execQuery(sql);
		try {
			while (rs.next()) {
				unigeneCluster = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unigeneCluster;
	}

	/**
	 * map the unigene cluster to unigene id in gene2unigene database
	 * 
	 * @param unigeneCluster
	 * @return
	 */
	public String unigeneCluster2unigene(String unigeneCluster) {
		String unigene = "";
		String sql = "select geneId from gene2unigene where unigeneCluster = '"
				+ unigeneCluster + "';";
		ResultSet rs = dbconn.execQuery(sql);
		try {
			while (rs.next()) {
				unigene = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return unigene;
	}
	/**
	 * convert from unigene to approved symbol in hgnc
	 * 
	 * @param unigene
	 * @return
	 */
	public String unigene2ApprovedSymbol(String unigene) {
		String symbol = "";
		String sql = "select approved_symbol from hgnc where entrez_gene_id = '"
				+ unigene + "';";
		ResultSet rs = dbconn.execQuery(sql);
		try {
			while (rs.next()) {
				symbol = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return symbol;
	}

	/**
	 * map gene to go using the gene ontology annotation
	 * 
	 * @param fileName
	 *            The file name of go annotation
	 * @return
	 */
	public HashMap<String, LinkedHashSet<String>> gene2go(String fileName) {
		HashMap<String, LinkedHashSet<String>> gene2go = new HashMap<String, LinkedHashSet<String>>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("dataRepository/"+fileName)));
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("!")) {
					line = br.readLine();
					continue;
				}
				String[] arr = line.split("\t");
				String gene = arr[2], go = arr[4];
				if (gene2go.containsKey(gene)) {
					LinkedHashSet<String> list = gene2go.remove(gene);
					list.add(go);
					gene2go.put(gene, list);

				} else {
					LinkedHashSet<String> list = new LinkedHashSet<String>();
					list.add(go);
					gene2go.put(gene, list);
				}
				System.out.println(gene + " " + go + " packaged done");
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gene2go;
	}

	/**
	 * extends the gene2go data set by using theirs synonyms in hgnc
	 * 
	 * @param fileName
	 *            The file name of go annotations
	 */
	public void extendGene2go(String fileName) {
		HashMap<String, LinkedHashSet<String>> gene2go = gene2go(fileName);
		for (String gene : gene2go.keySet()) {
			String sql = "select synonums from hgnc where locate('"+gene+"',approved_symbol)>0;";
			ResultSet rs = dbconn.execQuery(sql);
			try {
				while (rs.next()) {
					String syms = rs.getString(1);
					String[] genes = syms.split(",");
					for(String g:genes){
						gene2go.put(g, gene2go.get(gene));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Testing in main function here
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// String swiss = "Q15945";
		IDConversion idc = new IDConversion();
		// String hgnc_id = idc.swiss2HGNC(swiss);
		// String sym = idc.getApprovedsymbolFromHGNC(hgnc_id);
		// System.out.println(swiss + "->" + hgnc_id + "->" + sym);
		idc.extendGene2go("gene_association.goa_human");


	}
}
