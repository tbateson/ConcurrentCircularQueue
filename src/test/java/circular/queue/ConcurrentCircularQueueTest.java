package circular.queue;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class ConcurrentCircularQueueTest {

	@Test
	public void testBuffer() {
		final ConcurrentCircularQueue<String> buf = new ConcurrentCircularQueue<>(4);
		buf.add("1");
		Assert.assertEquals(1, buf.size());
		Assert.assertArrayEquals(new String[] { "1" }, buf.toArray());
		buf.addAll(Arrays.asList("2", "3"));
		System.out.println(buf);
		Assert.assertEquals(3, buf.size());
		Assert.assertArrayEquals(new String[] { "1", "2", "3" }, buf.toArray());
		buf.addAll(Arrays.asList("4", "5", "6"));
		Assert.assertEquals(4, buf.size());
		Assert.assertArrayEquals(new String[] { "3", "4", "5", "6"}, buf.toArray());
		buf.clear();
		Assert.assertEquals(0, buf.size());
	}

}