package pobj.pinboard.editor.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import javafx.scene.paint.Color;
import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipRect;
import pobj.pinboard.document.MyColor;
import pobj.pinboard.editor.Clipboard;
import pobj.pinboard.editor.ClipboardListener;

public class ClipboardListenerTest {

	private Clip[] clip = {
	        new ClipRect(100., 100, 200., 200., new MyColor(Color.RED))
	};

	private Clip[] clip2 = {
	        new ClipRect(150., 100, 200., 200., new MyColor(Color.RED))
	};

	
	private class EventCounter implements ClipboardListener {
		public int count;
		@Override public void clipboardChanged() { count++; }
	};
	
	@BeforeEach
	public void beforeTest() {
		Clipboard.getInstance().clear();
	}

	@Test
	public void testCopy() {
		Clipboard c = Clipboard.getInstance();
		EventCounter e = new EventCounter();
		c.addListener(e);
		int count1 = e.count;
		c.copyToClipboard(Arrays.asList(clip));
		int count2 = e.count;
		assertTrue(count2 > count1);
	}

	@Test
	public void testCopyClear() {
		Clipboard c = Clipboard.getInstance();
		EventCounter e = new EventCounter();
		c.addListener(e);
		c.copyToClipboard(Arrays.asList(clip));
		int count1 = e.count;
		c.clear();
		int count2 = e.count;
		assertTrue(count2 > count1);
	}

	@Test
	public void testCopyMulti() {
		Clipboard c = Clipboard.getInstance();
		EventCounter e1 = new EventCounter();
		EventCounter e2 = new EventCounter();
		c.addListener(e1);
		c.copyToClipboard(Arrays.asList(clip));
		c.addListener(e2);
		int count1 = e1.count;
		int count2 = e2.count;
		c.copyToClipboard(Arrays.asList(clip2));
		assertTrue(e1.count > count1);
		assertTrue(e2.count > count2);
	}
	
	@Test
	public void testRemove() {
		Clipboard c = Clipboard.getInstance();
		EventCounter e = new EventCounter();
		c.addListener(e);
		c.copyToClipboard(Arrays.asList(clip));
		c.removeListener(e);
		int count1 = e.count;
		c.copyToClipboard(Arrays.asList(clip2));
		assertTrue(e.count == count1);
	}

	@Test
	public void testRemoveMulti() {
		Clipboard c = Clipboard.getInstance();
		EventCounter e1 = new EventCounter();
		EventCounter e2 = new EventCounter();
		c.addListener(e1);
		c.addListener(e2);
		c.copyToClipboard(Arrays.asList(clip));
		c.removeListener(e1);
		int count1 = e1.count;
		int count2 = e2.count;
		c.copyToClipboard(Arrays.asList(clip2));
		assertTrue(e1.count == count1);
		assertTrue(e2.count > count2);
	}

}
