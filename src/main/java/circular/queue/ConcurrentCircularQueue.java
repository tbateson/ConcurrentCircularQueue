package circular.queue;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentCircularQueue<T> extends AbstractQueue<T> {

	private class Node {

		private final T item;
		private Node next;

		private Node(final T item) {
			this.item = Objects.requireNonNull(item);
		}

	}

	private class Data implements Iterable<T> {

		private Node head; // Oldest item in the queue
		private Node tail; // Newest item in the queue
		private int size; // Cached size, since it would take O(n) to calculate this

		@Override
		public Iterator<T> iterator() {
			return new Iterator<>() {
				Node curNode = head;
				int index;
				@Override
				public boolean hasNext() {
					return index < size;
				}
				@Override
				public T next() {
					if (!hasNext()) {
						throw new NoSuchElementException();
					}
					index++;
					final T item = curNode.item;
					curNode = curNode.next;
					return item;
				}
			};
		}

	}

	private final AtomicReference<Data> data;
	private final int capacity;

	public ConcurrentCircularQueue(final int capacity) {
		this.data = new AtomicReference<>(new Data());
		this.capacity = capacity;
	}

	@Override
	public boolean offer(final T e) {
		final Node newNode = new Node(e);
		final Data newData = new Data();
		newData.tail = newNode;
		Data curData;
		do {
			curData = data.get();
			if (curData.size + 1 > capacity) {
				newData.head = curData.head.next;
				curData.tail.next = newNode;
				newData.size = curData.size;
			} else {
				if (curData.size == 0) {
					newData.head = newNode;
				} else {
					newData.head = curData.head;
					curData.tail.next = newNode;
				}
				newData.size = curData.size + 1;
			}
		} while (!data.compareAndSet(curData, newData));
		return true;
	}

	@Override
	public T peek() {
		return data.get().head.item;
	}

	@Override
	public T poll() {
		final Data newData = new Data();
		Node head;
		Data curData;
		do {
			curData = data.get();
			head = curData.head;
			if (head == null) {
				return null;
			}
			newData.head = head.next;
			newData.tail = curData.tail;
			newData.size = curData.size - 1;
		} while (!data.compareAndSet(curData, newData));
		return head.item;
	}

	@Override
	public Iterator<T> iterator() {
		return data.get().iterator();
	}

	@Override
	public void clear() {
		data.set(new Data());
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return data.get().size;
	}

	public int capacity() {
		return capacity;
	}

}