package tree;

import java.util.Optional;

public class RWLock {

	/**
	 * Number of active readers holding this lock.
	 * 
	 * Class Invariant: If this is non-zero, no writer is holding this lock.
	 */
	int numReaders = 0;

	/**
	 * Number of writers waiting for this lock.
	 * 
	 * Class Invariant: If this is non-zero, no new readers may acquire this lock.
	 */
	int numWritersWaiting = 0;

	/**
	 * Number of times the active writer is holding this lock, or 0 if not locked by
	 * a writer.
	 */
	int heldCount = 0;

	/**
	 * The active writer thread, or empty if not locked by a writer.
	 * 
	 * Class Invariant: If this is non-empty, no readers are holding this lock.
	 */
	Optional<Thread> writer = Optional.empty();

	Lock rdLock = new ReadLock();
	Lock wrLock = new WriteLock();

	public Lock readLock() {
		return rdLock;
	}

	public Lock writeLock() {
		return wrLock;
	}

	class ReadLock implements Lock {
		@Override
		public void lock() {
			synchronized (RWLock.this) {

				while (writer.isPresent() || numWritersWaiting != 0) { // TODO: What two conditions should the reader
																		// wait on?
					try {
						RWLock.this.wait();
					} catch (InterruptedException ignored) {
					}
				}

				numReaders++;
				// TODO: If the code gets here, what has happened?
				// What must we do to reestablish the class invariant?
			}
		}

		@Override
		public void unlock() {
			synchronized (RWLock.this) {
				numReaders = 0;

				if (numReaders == 0) { // TODO: Under what condition would you notify?
					RWLock.this.notifyAll();
				}
			}
		}
	}

	public class WriteLock implements Lock {
		@Override
		public void lock() {
			Thread me = Thread.currentThread();
			synchronized (RWLock.this) {

				// Reentrant lock: if we're already holding this lock, we can just grab it
				// again. This code already works; you need not modify it.
				if (writer.orElse(null) == me) {
					heldCount++;
					return;
				}

				// We are waiting for the lock
				numWritersWaiting++;

				while (numReaders > 0 || writer.isPresent()) { // TODO: What two conditions should the writer wait on?
					try {
						RWLock.this.wait();
					} catch (InterruptedException ignored) {
					}
				}

				numWritersWaiting--;
				writer = Optional.of(me);

				// TODO: If the code gets here, what has happened?
				// What must we do to reestablish the class invariant?
			}
		}

		@Override
		public void unlock() {
			// This code already works; you need not modify it.
			synchronized (RWLock.this) {
				heldCount--;
				if (heldCount > 0) {
					// Reentrant lock: we still have this lock
					return;
				}
				writer = Optional.empty();
				RWLock.this.notifyAll();
			}
		}
	}
}

