package pobj.pinboard.editor.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipEllipse;
import pobj.pinboard.document.ClipRect;
import pobj.pinboard.document.MyColor;
import pobj.pinboard.editor.Selection;

public class SelectionTest {
	
	private Selection s = new Selection();
	private Clip rect = new ClipRect(100., 100., 200., 200., new MyColor(javafx.scene.paint.Color.RED));
	private Clip ellipse = new ClipEllipse(200., 100., 300., 200., new MyColor(javafx.scene.paint.Color.BLUE));
	private Board b = new Board();

	
	@BeforeEach
	public void beforeTest() {
		b.addClip(rect);
		b.addClip(ellipse);
	}
	
	@Test
	public void testSelect() {
		assertTrue(s.getContents().isEmpty());
		s.select(b,150.,150.);
		assertEquals(1, s.getContents().size());
		assertEquals(rect, s.getContents().get(0));
		s.clear();
		assertTrue(s.getContents().isEmpty());
	}

	@Test
	public void testReselect() {
		s.select(b,150.,150.);
		s.select(b,250.,150.);
		assertEquals(1, s.getContents().size());
		assertEquals(ellipse, s.getContents().get(0));
	}

	@Test
	public void testSelectMiss() {		
		s.select(b,50.,150.);
		assertTrue(s.getContents().isEmpty());
		s.select(b,205.,105.);
		assertTrue(s.getContents().isEmpty());
	}

	@Test
	public void testSelectRemiss() {		
		s.select(b,250.,150.);
		assertEquals(1, s.getContents().size());
		assertEquals(ellipse, s.getContents().get(0));
		s.select(b,205.,105.);
		assertTrue(s.getContents().isEmpty());
	}

	@Test
	public void testToogle() {
		s.select(b,150.,150.);
		assertEquals(1, s.getContents().size());
		assertEquals(rect, s.getContents().get(0));
		s.toogleSelect(b,250.,150.);
		assertEquals(2, s.getContents().size());
		assertTrue(s.getContents().contains(rect));
		assertTrue(s.getContents().contains(ellipse));
	}
	
	@Test
	public void testRetoogle() {
		s.toogleSelect(b,150.,150.);
		assertEquals(1, s.getContents().size());
		assertEquals(rect, s.getContents().get(0));
		s.toogleSelect(b,250.,150.);
		assertEquals(2, s.getContents().size());
		s.toogleSelect(b,150.,150.);
		assertEquals(1, s.getContents().size());
		assertEquals(ellipse, s.getContents().get(0));
		s.toogleSelect(b,250.,150.);
		assertTrue(s.getContents().isEmpty());
	}

	@Test
	public void testToogleMiss() {
		s.toogleSelect(b,205.,100.);
		assertTrue(s.getContents().isEmpty());
		s.toogleSelect(b,150.,150.);
		assertEquals(1, s.getContents().size());
		assertEquals(rect, s.getContents().get(0));
		s.toogleSelect(b,205.,105.);
		assertEquals(1, s.getContents().size());
		assertEquals(rect, s.getContents().get(0));
	}

}
