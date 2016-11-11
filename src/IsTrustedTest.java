import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class IsTrustedTest {
	
	private User user1;
	private User user2;
	private User user3;
	private User user4;
	private User user5;
	private User user6;

	@Before
	public void setUp() throws Exception {
		user1 = new User("001");
		user2 = new User("002");
		user3 = new User("003");
		user4 = new User("004");
		user5 = new User("005");
		user6 = new User("006");
	}

	/* Test two users who have made payment to each other */
	@Test
	public void testFirstFriend() {
		// user1 -- user2
		user1.addFriend(user2);
		user2.addFriend(user1);
		assertTrue(user1.isTrusted(user2, 1));
		assertTrue(user2.isTrusted(user1, 1));
	}
	
	/* Test feature 1 implementation on three users */
	@Test
	public void testFirstFeature() {
		// user1 -- user2 -- user3
		user1.addFriend(user2);
		user2.addFriend(user1);
		user2.addFriend(user3);
		user3.addFriend(user2);
		assertFalse(user1.isTrusted(user3, 1));
		assertFalse(user3.isTrusted(user1, 1));	
	}
	
	/* Test feature 2 implementation on four users */
	@Test
	public void testSecondFeature() {
		// user1 -- user2 -- user3 -- user4
		user1.addFriend(user2);
		user2.addFriend(user1);
		user2.addFriend(user3);
		user3.addFriend(user2);
		user3.addFriend(user4);
		user4.addFriend(user3);
		
		// 1st degree user network
		assertTrue(user1.isTrusted(user2, 2));
		assertTrue(user2.isTrusted(user1, 2));
		assertTrue(user2.isTrusted(user3, 2));
		assertTrue(user3.isTrusted(user2, 2));
		// 2nd degree user network
		assertTrue(user1.isTrusted(user3, 2));
		assertTrue(user3.isTrusted(user1, 2));
		assertTrue(user2.isTrusted(user4, 2));
		assertTrue(user4.isTrusted(user2, 2));
		// 3rd degree user network
		assertFalse(user1.isTrusted(user4, 2));
		assertFalse(user4.isTrusted(user1, 2));
	}
	
	/* Test five friends, implementing feature 3 */
	@Test
	public void testThirdFeature() {
		// user1 -- user2 -- user3 -- user4 -- user5 -- user6
		user1.addFriend(user2);
		user2.addFriend(user1);
		user2.addFriend(user3);
		user3.addFriend(user2);
		user3.addFriend(user4);
		user4.addFriend(user3);
		user4.addFriend(user5);
		user5.addFriend(user4);
		user5.addFriend(user6);
		user6.addFriend(user5);
		
		// 1st degree user network
		assertTrue(user3.isTrusted(user4, 4));
		assertTrue(user4.isTrusted(user3, 4));
		// 2nd degree user network
		assertTrue(user2.isTrusted(user4, 4));
		assertTrue(user4.isTrusted(user2, 4));
		// 3rd degree user network
		assertTrue(user1.isTrusted(user4, 4));
		assertTrue(user4.isTrusted(user1, 4));
		// 4th degree user network
		assertTrue(user1.isTrusted(user5, 4));
		assertTrue(user5.isTrusted(user1, 4));
		// 5th degree user network
		assertFalse(user1.isTrusted(user6, 4));
		assertFalse(user6.isTrusted(user1, 4));
	}

}
