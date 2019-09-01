package cc.mrbird.febs.common.options;

import com.wuwenze.poi.config.Options;

public class TypeOption implements Options{

	@Override
	public String[] get() {
		String[] s = {"avi","doc","pdf","xls","txt","zip","image"};
		return s;
	}

}
