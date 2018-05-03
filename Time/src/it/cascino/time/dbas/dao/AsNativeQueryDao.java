package it.cascino.time.dbas.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface AsNativeQueryDao{
	BigDecimal getDaMovtr0f_MtquaDaMtcodAndMtdpa(String mtcod, Integer mtdpa);
	
	BigDecimal getDaMovtr0f_MtquaDaMtcodAndMtdpp(String mtcod, Integer mtdpp);

	Timestamp getDaSysdummy1_TimestampAs400();

	void close();
}
