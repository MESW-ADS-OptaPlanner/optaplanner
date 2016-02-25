/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.persistence.jpa.impl.score.buildin.simpledouble;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.TypeDef;
import org.junit.Test;
import org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScore;
import org.optaplanner.persistence.jpa.impl.score.AbstractScoreHibernateTypeTest;

import javax.persistence.Column;
import javax.persistence.Entity;

public class SimpleDoubleScoreHibernateTypeTest extends AbstractScoreHibernateTypeTest {

    @Test
    public void persistAndMerge() {
        Long id = persistAndAssert(new TestJpaEntity(null));
        findAssertAndChangeScore(TestJpaEntity.class, id, null, SimpleDoubleScore.valueOf(-10.01));
        findAndAssert(TestJpaEntity.class, id, SimpleDoubleScore.valueOf(-10.01));
    }

    @Entity
    @TypeDef(defaultForType = SimpleDoubleScore.class, typeClass = SimpleDoubleScoreHibernateType.class)
    public static class TestJpaEntity extends AbstractTestJpaEntity<SimpleDoubleScore> {

        protected SimpleDoubleScore score;

        private TestJpaEntity() {
        }

        public TestJpaEntity(SimpleDoubleScore score) {
            this.score = score;
        }

        @Columns(columns = {@Column(name = "score")})
        public SimpleDoubleScore getScore() {
            return score;
        }

        public void setScore(SimpleDoubleScore score) {
            this.score = score;
        }

    }

}
