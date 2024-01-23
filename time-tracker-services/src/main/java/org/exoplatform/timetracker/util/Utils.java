package org.exoplatform.timetracker.util;

import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;

public class Utils {
    public static final Identity getCurrentUserIdentityId(IdentityManager identityManager) {
        String currentUser = getCurrentUser();
        Identity identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, currentUser);
        return identity ;
    }

    public static final String getCurrentUser() {
        return ConversationState.getCurrent().getIdentity().getUserId();
    }


}
