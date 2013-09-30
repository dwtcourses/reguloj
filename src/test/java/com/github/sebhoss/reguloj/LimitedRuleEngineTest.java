package com.github.sebhoss.reguloj;

import com.github.sebhoss.common.annotation.CompilerWarnings;
import com.google.common.collect.ImmutableList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for LimitedRuleEngine.
 */
@SuppressWarnings({ CompilerWarnings.BOXING, CompilerWarnings.NULL, CompilerWarnings.UNCHECKED })
public class LimitedRuleEngineTest {

    private RuleEngine<Context<String>> engine;
    private Context<String>             context;
    private Rule<Context<String>>       rule1;
    private Rule<Context<String>>       rule2;

    /**
     * Creates rule engine and context.
     */
    @Before
    public void setup() {
        engine = new LimitedRuleEngine<>(2);
        context = Mockito.mock(Context.class);
        rule1 = Mockito.mock(Rule.class);
        rule2 = Mockito.mock(Rule.class);
    }

    /**
     * Ensures that rules will be called a maximum of two times (as specified in {@link #setup()} with matching rules.
     */
    @Test
    public void shouldRunTwoTimesWithMatchingRules() {
        Mockito.when(rule1.run(context)).thenReturn(Boolean.TRUE);
        Mockito.when(rule2.run(context)).thenReturn(Boolean.TRUE);

        engine.infer(ImmutableList.of(rule1, rule2), context);

        Mockito.verify(rule1, Mockito.times(2)).run(context);
        Mockito.verify(rule2, Mockito.times(2)).run(context);
    }

    /**
     * Ensures that all rules will be only called once, if there are no matching rules.
     */
    @Test
    public void shouldRunOnceWithNonMatchingRules() {
        Mockito.when(rule1.run(context)).thenReturn(Boolean.FALSE);
        Mockito.when(rule2.run(context)).thenReturn(Boolean.FALSE);

        engine.infer(ImmutableList.of(rule1, rule2), context);

        Mockito.verify(rule1, Mockito.times(1)).run(context);
        Mockito.verify(rule2, Mockito.times(1)).run(context);
    }

}