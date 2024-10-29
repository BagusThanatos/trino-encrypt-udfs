/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.starburstdata.udfs.encrypt;

import io.trino.metadata.InternalFunctionBundle;
import io.trino.sql.query.QueryAssertions;
import org.junit.jupiter.api.parallel.Execution;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Execution(CONCURRENT)
public class TestEncrypt
{
    private QueryAssertions assertions;

    @BeforeClass
    public void setUp()
    {
        assertions = new QueryAssertions();
        assertions.addFunctions(InternalFunctionBundle.builder()
                .scalars(EncryptDecrypt.class)
                .build());
    }

    @AfterClass(alwaysRun = true)
    public final void destroyTestFunctions()
    {
        assertions.close();
        assertions = null;
    }

    @Test
    public void testEncrypt()
    {
        assertThat(assertions.expression("encrypt('myvalue','mypassword')"))
                .isEqualTo("/stEnUn+cUs=");
        assertThat(assertions.expression("decrypt('/stEnUn+cUs=','mypassword')"))
                .isEqualTo("myvalue");
    }
}
