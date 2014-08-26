package com.sparql;

import com.sparql.DBPediaDAO;
import static org.junit.Assert.*;
import org.junit.*;

public class DBPediaDAOTest {
	
	@Test
	public void testJUnit(){
		DBPediaDAO dbPedia = new DBPediaDAO();
		assertEquals("Yes", dbPedia.PrintAwesome());
	}

}