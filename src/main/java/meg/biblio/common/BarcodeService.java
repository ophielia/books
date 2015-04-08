package meg.biblio.common;

import java.util.Locale;

import meg.biblio.common.db.dao.ClientDao;
import meg.biblio.common.report.BarcodeSheet;



public interface BarcodeService {

	public final static class CodeType {
		public static final String BOOK = "B";
		public static final String PERSON = "A";
	}
	
	public final static String codecountlkup="codecount";
	

	BarcodeSheet assembleBarcodeSheetForClass(Long classId, Long clientid, Locale locale);
	
	BarcodeSheet assembleBarcodeSheetForBooks(int count, int startcode,
			Long clientkey, Locale locale);
	
	String getBookBarcodeForClientid(ClientDao client, String clientbookid);


}
