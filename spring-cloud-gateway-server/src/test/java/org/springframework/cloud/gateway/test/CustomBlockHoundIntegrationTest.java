/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.gateway.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.blockhound.BlockingOperationError;
import reactor.core.scheduler.Schedulers;

import org.springframework.cloud.gateway.config.GlobalTestExtension;

/**
 * @author Tim Ysewyn
 */
@ExtendWith(GlobalTestExtension.class)
class CustomBlockHoundIntegrationTest {

	private static final Log log = LogFactory.getLog(CustomBlockHoundIntegrationTest.class);

//	@BeforeEach
//	void setUp() {
//		BlockHound.install();
//	}

	@Test
	void shouldThrowErrorForBlockingCallWithCustomBlockHoundIntegration()
			throws InterruptedException, TimeoutException {
		log.info("[TEST] koko");


//		ServiceLoader<TestExecutionListener> loader = ServiceLoader.load(TestExecutionListener.class);
//		for (TestExecutionListener listener : loader) {
//			log.info("[Loaded] listener: " + listener.getClass().getName());
//		}

		try {
			FutureTask<?> task = new FutureTask<>(() -> {
				Thread.sleep(0);
				return "";
			});
			Schedulers.parallel().schedule(task);

			task.get(10, TimeUnit.SECONDS);
			Assertions.fail("should fail");
		}
		catch (ExecutionException e) {
			Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
		}
	}
}
