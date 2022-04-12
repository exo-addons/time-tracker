package org.exoplatform.timetracker.service;

import org.exoplatform.commons.exception.ObjectNotFoundException;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.timetracker.storage.ClientStorage;
import org.junit.After;
import org.junit.Before;

public abstract class BaseTimeTrackerTest {

	protected PortalContainer container;

	protected ClientStorage clientStorage;

	@Before
	public void setUp() throws ObjectNotFoundException {
		container = PortalContainer.getInstance();
		initServices();
		begin();
	}

	private void initServices() {
		clientStorage = container.getComponentInstanceOfType(ClientStorage.class);
	}

	@After
	public void tearDown() throws ObjectNotFoundException {
		end();
	}

	protected void begin() {
		ExoContainerContext.setCurrentContainer(container);
		RequestLifeCycle.begin(container);
	}

	protected void end() {
		RequestLifeCycle.end();
	}

}
