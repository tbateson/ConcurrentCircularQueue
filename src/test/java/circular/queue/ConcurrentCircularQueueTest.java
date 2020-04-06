package circular.queue;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class ConcurrentCircularQueueTest {

	@Test
	public void testBufferAdd() {
		final ConcurrentCircularQueue<String> buf = new ConcurrentCircularQueue<>(4);
		buf.add("1");
		Assert.assertEquals(1, buf.size());
		Assert.assertArrayEquals(new String[] { "1" }, buf.toArray());
		buf.add("2");
		buf.add("3");
		Assert.assertEquals(3, buf.size());
		Assert.assertArrayEquals(new String[] { "1", "2", "3" }, buf.toArray());
		buf.add("4");
		buf.add("5");
		buf.add("6");
		Assert.assertEquals(4, buf.size());
		Assert.assertArrayEquals(new String[] { "3", "4", "5", "6"}, buf.toArray());
		Assert.assertEquals("3", buf.poll());
		Assert.assertArrayEquals(new String[] { "4", "5", "6"}, buf.toArray());
		buf.clear();
		Assert.assertEquals(0, buf.size());
	}

	@Test
	public void testBufferAddAll() {
		final ConcurrentCircularQueue<String> buf = new ConcurrentCircularQueue<>(4);
		buf.addAll(Arrays.asList("1", "2", "3"));
		Assert.assertEquals(3, buf.size());
		Assert.assertArrayEquals(new String[] { "1", "2", "3" }, buf.toArray());
		buf.addAll(Arrays.asList("4", "5", "6", "7"));
		Assert.assertEquals(4, buf.size());
		Assert.assertArrayEquals(new String[] { "4", "5", "6", "7" }, buf.toArray());
		buf.clear();
		Assert.assertEquals(0, buf.size());
	}

}