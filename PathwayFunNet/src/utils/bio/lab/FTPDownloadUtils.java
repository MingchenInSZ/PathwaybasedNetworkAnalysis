package utils.bio.lab;

import download.bio.lab.FTPDownload;

public class FTPDownloadUtils {

	public static void main(String [] args){
		String server = "ftp.ncbi.nlm.nih.gov";
		String user = "anonymous";
		String passwd = "aaa";
		FTPDownload ftpd = new FTPDownload(server, user, passwd);
		ftpd.downloadFileInProgress("gene/DATA", "gene2unigene","dataRepository");
		ftpd.close();
	}
}
