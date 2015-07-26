package xmlparse.bio.lab;

import java.io.File;
import java.io.FileFilter;

/**
 * 
 * @author mingchen
 * @date 2015Äê5ÔÂ3ÈÕ
 */
public class XMLFilter implements FileFilter {
	@Override
	public boolean accept(File file){
		if (file.getName().endsWith(".xml")) {
			return true;
		}
		return false;
	}

}
