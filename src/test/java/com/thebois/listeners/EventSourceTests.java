package com.thebois.listeners;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventSourceTests {

    @Test
    public void sendEventCallsAllListeners() {
        // Arrange
        final List<IEventListener<TestEvent>> listeners = mockListeners();
        final AbstractEventSource<TestEvent> cut = new TestEventSource();
        for (final IEventListener<TestEvent> listener : listeners) {
            cut.addListener(listener);
        }

        // Act
        cut.send(new TestEvent());

        // Assert
        listeners.forEach(listener -> verify(listener, times(1)).onEvent(any()));
    }

    private List<IEventListener<TestEvent>> mockListeners() {
        return new ArrayList<>(List.of(mockListener(), mockListener(), mockListener()));
    }

    @SuppressWarnings("unchecked")
    private IEventListener<TestEvent> mockListener() {
        // Cast needed because mocks don't have type parameters.
        return (IEventListener<TestEvent>) mock(IEventListener.class);
    }

    @Test
    public void sendEventWithEventDataSendsEventDataToAllListeners() {
        // Arrange
        final List<IEventListener<TestEvent>> listeners = mockListeners();
        final AbstractEventSource<TestEvent> cut = new TestEventSource();
        for (final IEventListener<TestEvent> listener : listeners) {
            cut.addListener(listener);
        }

        // Act
        final int testData = 149;
        cut.send(new TestEvent(testData));

        // Assert
        for (final IEventListener<TestEvent> listener : listeners) {
            final ArgumentCaptor<TestEvent> eventCaptor = ArgumentCaptor.forClass(TestEvent.class);
            verify(listener).onEvent(eventCaptor.capture());
            assertThat(eventCaptor.getValue().getTestData()).isEqualTo(testData);
        }
    }

    @Test
    public void sendEventDoesNotNotifyUnregisteredListener() {
        // Arrange
        final IEventListener<TestEvent> listenerToRemove = mockListener();
        final List<IEventListener<TestEvent>> listenersToKeep = mockListeners();
        final AbstractEventSource<TestEvent> cut = new TestEventSource();
        for (final IEventListener<TestEvent> listener : listenersToKeep) {
            cut.addListener(listener);
        }
        cut.addListener(listenerToRemove);

        // Act
        cut.removeListener(listenerToRemove);
        cut.send(new TestEvent());

        // Assert
        listenersToKeep.forEach(listener -> verify(listener, times(1)).onEvent(any()));
        verify(listenerToRemove, times(0)).onEvent(any());
    }

    private static class TestEvent extends AbstractEvent {

        private final int testData;

        TestEvent() {
            testData = 0;
        }

        TestEvent(final int testData) {
            this.testData = testData;
        }

        public int getTestData() {
            return testData;
        }

    }

    private static class TestEventSource extends AbstractEventSource<TestEvent> {

    }

}
