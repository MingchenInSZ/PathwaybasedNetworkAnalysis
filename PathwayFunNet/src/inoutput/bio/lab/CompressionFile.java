package inoutput.bio.lab;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import logging.bio.lab.LogUtils;

public class CompressionFile {
	private static final String wdir = "dataRepository";
	private static DecimalFormat df = new DecimalFormat("#.00%");

	/**
	 * zip file to zip just single file mode
	 * 
	 * @param fileName
	 */
	public static void zip(String fileName) {
		String[] paths = fileName.split("/");
		String outFile = wdir + File.separator + paths[paths.length -1] + ".zip";
		try {
			LogUtils.log("creating zip file..");
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(outFile)));
			FileInputStream fis = new FileInputStream(new File(wdir+File.separator+fileName));
			byte[] buff = new byte[1024];
			int len = 0, compr = 0, total = fis.available();
			zos.putNextEntry(new ZipEntry(fileName));
			while ((len = fis.read(buff)) > 0) {
				compr += len;
				zos.write(buff, 0, len);
				LogUtils.log("progress:" + df.format(compr * 1.0 / total));
			}
			zos.finish();
			zos.close();
			fis.close();
			LogUtils.log("zip done! [" + outFile + "]");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * gzip file to gz mode(just single file)
	 * 
	 * @param fileName
	 */
	public static void gzip(String fileName) {
		String[] paths = fileName.split("/");
		String outFile = wdir + File.separator + paths[paths.length -1] + ".gz";
		try {
			LogUtils.log("creating gzip file..");
			GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(new File(outFile)));
			FileInputStream fis = new FileInputStream(new File(wdir+File.separator+fileName));
			byte[] buff = new byte[1024];
			int len = 0, compr = 0, total = fis.available();
			while ((len = fis.read(buff)) > 0) {
				gos.write(buff, 0, len);
				compr += len;
				LogUtils.log("progress:" + df.format(compr * 1.0 / total));
			}
			gos.finish();
			gos.close();
			fis.close();
			LogUtils.log("gzip file [" + outFile + "]");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * gun zip file in local file
	 * 
	 * @param fileName
	 */
	public static void gunzip(String fileName) {
		String realFile = wdir + File.separator + fileName;
		try {
			ZipInputStream zin = new ZipInputStream(new FileInputStream(
					new File(realFile)));

			BufferedInputStream bis = new BufferedInputStream(zin);
			ZipEntry entry = null;
			String parent = wdir;
			while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {

				File file = new File(parent,entry.getName());
				if (!file.exists()) {
					file.getParentFile().mkdirs();
				}
				FileOutputStream out = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(out);
				int b;
				while ((b = bis.read()) != -1) {
					bos.write(b);
				}
				bos.close();
				out.close();
			}
			bis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * gun gz file to local file
	 * 
	 * @param fileName
	 */
	public static void gungzip(String fileName) {
		String realFile = wdir + File.separator + fileName;
		File file = new File(realFile);
		try {
			GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(
					file));
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(new File(file.getPath().replace(".gz",
							""))));
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = gzip.read(buff)) > 0) {
				bos.write(buff, 0, len);
			}
			bos.close();
			gzip.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
