/*
 * Project: viaRules
 * Package: com.github.sebhoss.reguloj.implementation
 * File   : RuleImplementationTest.java
 * Created: Nov 10, 2010 - 5:55:55 PM
 *
 *
 * Copyright 2010 viadee IT Unternehmensberatung GmbH
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
 *
 */
package com.github.sebhoss.reguloj.implementation;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import com.github.sebhoss.reguloj.Conclusion;
import com.github.sebhoss.reguloj.InferenceContext;
import com.github.sebhoss.reguloj.Rule;
import com.github.sebhoss.reguloj.implementation.RuleImplementation;
import com.google.common.base.Predicate;


/**
 * <p>Test cases for the {@link RuleImplementation}.</p>
 *
 * @author  Sebastian Hoß (sebastian.hoss@viadee.de)
 * @see     RuleImplementation
 * @since   1.0.0
 */
@SuppressWarnings("static-method")
public final class RuleImplementationTest {

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // *                                                  CONSTANTS                                                  *
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    /** Constant name for all rules inside this test. */
    private static final String NAME   = "test rule";             //$NON-NLS-1$

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // *                                                  ATTRIBUTES                                                 *
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    /** Checks expected exception inside single test cases. */
    @org.junit.Rule
    public ExpectedException    thrown = ExpectedException.none();

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // *                                                    TESTS                                                    *
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    /**
     * <p>Test method for {@link RuleImplementation#RuleImplementation(String, 
     * com.google.common.base.Predicate, com.github.sebhoss.reguloj.Conclusion)}</p>
     * 
     * <p>Ensures that <code>null</code> values are not permitted.</p>
     */
    @Test
    public void shouldNotAcceptNullValues() {
        // given
        this.thrown.expect(NullPointerException.class);

        // when
        final Rule<InferenceContext<Object>> rule = new RuleImplementation<InferenceContext<Object>>(null, null, null);

        // then
        Assert.assertThat(rule, Is.is(IsNull.nullValue()));
    }

    /**
     * <p>Test method for {@link RuleImplementation#RuleImplementation(String, 
     * com.google.common.base.Predicate, com.github.sebhoss.reguloj.Conclusion)}</p>
     * 
     * <p>Ensures that a valid rule will be built once all values are given.</p>
     */
    @Test
    public void shouldCreateRuleIfAllValuesAreSet() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertThat(rule, Is.is(IsNull.notNullValue()));
    }

    /**
     * <p>Test method for {@link RuleImplementation#run(InferenceContext)}</p>
     * 
     * <p>Ensures that <code>false</code> is returned if the predicate does not apply.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void shouldReturnFalseWhenPremiseDoesNotApply() {
        // given
        final InferenceContext<Object> context = Mockito.mock(InferenceContext.class);
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        BDDMockito.given(predicate.apply(context)).willReturn(Boolean.FALSE);

        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertFalse(rule.run(context));
    }

    /**
     * <p>Test method for {@link RuleImplementation#run(InferenceContext)}</p>
     * 
     * <p>Ensures that <code>false</code> is returned if the conclusion does not apply.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void shouldReturnFalseWhenConclusionDoesNotApply() {
        // given
        final InferenceContext<Object> context = Mockito.mock(InferenceContext.class);
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        BDDMockito.given(predicate.apply(context)).willReturn(Boolean.TRUE);

        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);
        BDDMockito.given(conclusion.apply(context)).willReturn(Boolean.FALSE);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertFalse(rule.run(context));
    }

    /**
     * <p>Test method for {@link RuleImplementation#run(InferenceContext)}</p>
     * 
     * <p>Ensures that <code>false</code> is returned if the conclusion does not apply.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void shouldReturnTrueWhenPremiseAndConclusionApply() {
        // given
        final InferenceContext<Object> context = Mockito.mock(InferenceContext.class);
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        BDDMockito.given(predicate.apply(context)).willReturn(Boolean.TRUE);

        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);
        BDDMockito.given(conclusion.apply(context)).willReturn(Boolean.TRUE);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertTrue(rule.run(context));
    }

    /**
     * <p>Test method for {@link RuleImplementation#fires(InferenceContext)}</p>
     * 
     * <p>Ensures that <code>true</code> is returned if the predicate does apply.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void shouldFireWhenPremiseApplies() {
        // given
        final InferenceContext<Object> context = Mockito.mock(InferenceContext.class);
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        BDDMockito.given(predicate.apply(context)).willReturn(true);

        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertTrue(rule.fires(context));
    }

    /**
     * <p>Test method for {@link RuleImplementation#fires(InferenceContext)}</p>
     * 
     * <p>Ensures that <code>false</code> is returned if the predicate does not apply.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void shouldNotFireWhenPremiseDoesNotApply() {
        // given
        final InferenceContext<Object> context = Mockito.mock(InferenceContext.class);
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        BDDMockito.given(predicate.apply(context)).willReturn(false);

        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertFalse(rule.fires(context));
    }

    /**
     * <p>Test method for {@link RuleImplementation#getName()}</p>
     * 
     * <p>Ensures that the previously set name is returned.</p>
     */
    @Test
    public void shouldReturnTheSetName() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertThat(rule.getName(), Is.is(RuleImplementationTest.NAME));
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals is reflexive</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsIsReflexive() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertThat(rule.equals(rule), Is.is(true));
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals is symmetric</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsIsSymmetric() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule1 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);
        final Rule<InferenceContext<Object>> rule2 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertThat(rule1.equals(rule2), Is.is(true));
        Assert.assertThat(rule2.equals(rule1), Is.is(true));
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals is transitive</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsIsTransitive() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule1 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);
        final Rule<InferenceContext<Object>> rule2 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);
        final Rule<InferenceContext<Object>> rule3 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertThat(rule1.equals(rule2) && rule2.equals(rule3), Is.is(true));
        Assert.assertThat(rule3.equals(rule1), Is.is(true));
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals is consistent</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsIsConsistent() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule1 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);
        final Rule<InferenceContext<Object>> rule2 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        final boolean alwaysTheSame = rule1.equals(rule2);

        for (int i = 0; i < 30; i++) {
            Assert.assertThat(rule1.equals(rule2), Is.is(alwaysTheSame));
        }
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals is returns <code>false</code> on <code>null</code> values.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsReturnFalseOnNull() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertThat(rule.equals(null), Is.is(false));
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals is returns <code>false</code> on <code>null</code> values.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsReturnFalseOnWrongClass() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertThat(rule.equals(""), Is.is(false)); //$NON-NLS-1$
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals works with identical instances.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsWorks() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule1 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);
        final Rule<InferenceContext<Object>> rule2 = rule1;

        // then
        Assert.assertThat(rule1.equals(rule2), Is.is(true));
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals works with different names</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsWorksWithDifferentNames() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule1 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);
        final Rule<InferenceContext<Object>> rule2 =
                new RuleImplementation<InferenceContext<Object>>("rule2", predicate, conclusion); //$NON-NLS-1$

        // then
        Assert.assertThat(rule1.equals(rule2), Is.is(false));
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals works with different predicates.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsWorksWithDifferentPremises() {
        // given
        final Predicate<InferenceContext<Object>> predicate1 = Mockito.mock(Predicate.class);
        final Predicate<InferenceContext<Object>> predicate2 = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule1 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate1, conclusion);
        final Rule<InferenceContext<Object>> rule2 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate2, conclusion);

        // then
        Assert.assertThat(rule1.equals(rule2), Is.is(false));
    }

    /**
     * <p>Test method for {@link RuleImplementation#equals(Object)}</p>
     * 
     * <p>Ensures that equals works with different conclusions.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void equalsWorksWithDifferentConclusions() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion1 = Mockito.mock(Conclusion.class);
        final Conclusion<InferenceContext<Object>> conclusion2 = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule1 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion1);
        final Rule<InferenceContext<Object>> rule2 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion2);

        // then
        Assert.assertThat(rule1.equals(rule2), Is.is(false));
    }

    /**
     * <p>Test method for {@link RuleImplementation#hashCode()}</p>
     * 
     * <p>Ensures that hashCode() always returns the same value for a same instance.</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void hashCodeIsSelfConsistent() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        final int alwaysTheSame = rule.hashCode();

        for (int i = 0; i < 30; i++) {
            Assert.assertThat(rule.hashCode(), Is.is(alwaysTheSame));
        }
    }

    /**
     * <p>Test method for {@link RuleImplementation#hashCode()}</p>
     * 
     * <p>Ensures that hashCode() is consistent with equals().</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void hashCodeIsConsistentWithEquals() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule1 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);
        final Rule<InferenceContext<Object>> rule2 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertThat(rule1.equals(rule2), Is.is(true));
        Assert.assertThat(rule1.hashCode(), Is.is(rule2.hashCode()));
    }

    /**
     * <p>Test method for {@link RuleImplementation#compareTo(com.github.sebhoss.reguloj.Rule)}</p>
     * 
     * <p>Ensures that compareTo() is consistent with equals().</p>
     */
    @SuppressWarnings("boxing")
    @Test
    public void shoudCompareToOtherRules() {
        // given
        final Predicate<InferenceContext<Object>> predicate = Mockito.mock(Predicate.class);
        final Conclusion<InferenceContext<Object>> conclusion = Mockito.mock(Conclusion.class);

        // when
        final Rule<InferenceContext<Object>> rule1 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);
        final Rule<InferenceContext<Object>> rule2 =
                new RuleImplementation<InferenceContext<Object>>(RuleImplementationTest.NAME, predicate, conclusion);

        // then
        Assert.assertThat(rule1.compareTo(rule2), Is.is(0));
    }
}
