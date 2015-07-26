package download.bio.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.text.DecimalFormat;

import logging.bio.lab.LogUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * @author mingchen
 * @date May 1st, 2015
 * 
 */

public class FTPDownload {
	private final String wdir = "dataRepository";
	private FTPClient ftp = null;

	public FTPDownload(String server, String user, String passwd) {
		initConnection(server, user, passwd);
	}

	/**
	 * Connect to ftp server
	 * 
	 * @param server
	 *            the host you connect
	 * @param user
	 *            the user account to login [anonymous as default]
	 * @param passwd
	 *            the password according to you account, and if the account is
	 *            anonymous "" as default
	 */
	public void initConnection(String server, String user, String passwd) {
		ftp = new FTPClient();
		try {
			ftp.connect(server);
			ftp.login(user, passwd);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				LogUtils.log("disconnect after login");
			}
			LogUtils.log("Connected to " + server + " with name [" + user + "]");
		} catch (SocketException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * download file from remote host without progress percentage display
	 * 
	 * @param folder
	 *            remote directory
	 * @param fileName
	 *            the file you want to download
	 * @param destfolder
	 *            the destination folder you will store the file
	 */
	public void downloadFile(String folder, String fileName, String destfolder) {
		try {
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(folder);
			LogUtils.log("Changing to directory:[" + folder + "]");
			String realFile = destfolder + File.separator + fileName;
			File localFile = new File(realFile);
			FileOutputStream fos = new FileOutputStream(localFile);
			FileInputStream fis = new FileInputStream(localFile);
			LogUtils.log("Start downloading..");
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile f : fs) {
				if (f.getName().equals(fileName)) {
					ftp.retrieveFile(f.getName(), fos);
					LogUtils.log("Download done!");
					break;
				}
			}
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * download file from remote host and display the downloaded percentage
	 * 
	 * @param folder
	 *            remote directory
	 * @param fileName
	 *            the file you want to download
	 * @param destfolder
	 *            the destination folder you will store the file
	 */
	public void downloadFileInProgress(String folder, String fileName, String destfolder) {
		try {
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(folder);
			LogUtils.log("Changing to directory:[" + folder + "]");
			String realFile = destfolder + File.separator + fileName;
			File localFile = new File(realFile);
			FileOutputStream fos = new FileOutputStream(localFile);
			LogUtils.log("Start downloading..");
			FTPFile[] fs = ftp.listFiles();
			DecimalFormat df = new DecimalFormat("#.00%");
			for (FTPFile f : fs) {
				if (f.getName().equals(fileName)) {
					InputStream is = ftp.retrieveFileStream(f.getName());
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line = br.readLine();
					long transfered = 0, total = f.getSize();
					double ratio = 0.0;
					while (line != null) {
						byte[] buff = line.getBytes();
						transfered += buff.length;
						if (transfered * 1.0 / total - ratio >= 0.01) {
							ratio = transfered * 1.0 / total;
							LogUtils.log("Download size : " + df.format(ratio));
						}
						fos.write(buff);
						line = br.readLine();
					}
					is.close();
					ftp.logout();
					ftp.disconnect();

					LogUtils.log("Download done!");
				}
			}
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * logout and disconnect
	 */
	public void close() {
		try {
			ftp.logout();
			ftp.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
