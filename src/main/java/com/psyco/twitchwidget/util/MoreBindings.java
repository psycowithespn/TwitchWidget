package com.psyco.twitchwidget.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ObservableNumberValue;

public class MoreBindings {

    public static NumberBinding mod(ObservableNumberValue a, ObservableNumberValue b) {
        return Bindings.createDoubleBinding(() -> a.doubleValue() % b.doubleValue(), a, b);
    }

    public static IntegerBinding floor(ObservableNumberValue a) {
        return Bindings.createIntegerBinding(a::intValue, a);
    }
}
