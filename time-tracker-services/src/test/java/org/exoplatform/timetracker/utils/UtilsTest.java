package org.exoplatform.timetracker.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.timetracker.util.Utils;
import org.junit.Before;
import org.junit.Test;
public class UtilsTest {
	public IdentityManager identityManager;
	Utils utils;
	@Before
	public void setUp() throws Exception { // NOSONAR
		identityManager = mock(IdentityManager.class);
		
		utils = new Utils();

	}
//@Test
//public void testGetCurrentUserIdentityId() {
//	Identity identity= new Identity("test","test");
//	when(Utils.getCurrentUser()).thenReturn("test");
//	when(identityManager.getOrCreateIdentity(any(), any())).thenReturn(identity);
//	when(OrganizationIdentityProvider.NAME).thenReturn("test");
//	Identity identityNew = Utils.getCurrentUserIdentityId(identityManager);
//	assertEquals(identity, identityNew);
//}
}
