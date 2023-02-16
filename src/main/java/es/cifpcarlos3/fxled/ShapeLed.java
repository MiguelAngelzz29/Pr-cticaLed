/*
 * Copyright (c) 2013. by Gerrit Grunwald
 *
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
package es.cifpcarlos3.fxled;

import java.io.Serializable;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.LongProperty;
import javafx.beans.property.LongPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Control;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 * Created by User: hansolo Date: 05.04.13 Time: 14:39
 */
public class ShapeLed extends Region implements Serializable {

    private static final double PREFERRED_SIZE = 16;
    private static final double MINIMUM_SIZE = 8;
    private static final double MAXIMUM_SIZE = 1024;
    private static final long SHORTEST_INTERVAL = 50_000_000l;
    private static final long LONGEST_INTERVAL = 5_000_000_000l;
    private static final int RGB_MIN = 0;
    private static final int RGB_MAX = 255;
    private static final int RGB_RANGE = RGB_MAX + 1;
    public static final Color BORDER_DEFAULT = Color.GRAY;

    private ObjectProperty<Color> ledColor;
    private ObjectProperty<Color> borderColor;
    private BooleanProperty on;
    private boolean _blinking = false;
    private BooleanProperty blinking;
    private boolean _frameVisible = true;
    private BooleanProperty frameVisible;
    private InnerShadow ledOnShadow;
    private InnerShadow ledOffShadow;
    private LinearGradient frameGradient;
    private LinearGradient ledOnGradient;
    private LinearGradient hoverGradient;
    private LinearGradient ledOffGradient;
    private RadialGradient highlightGradient;
    private long lastTimerCall;
    private long _interval = 500_000_000l;
    private LongProperty interval;
    private final AnimationTimer timer;
    private Color color;

    // ******************** Constructors **************************************
    public ShapeLed() {
        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(final long NOW) {
                if (NOW > lastTimerCall + getInterval()) {
                    setOn(!isOn());
                    lastTimerCall = NOW;
                }
            }
        };
        init();
        initGraphics();
        registerListeners();
    }

    // ******************** Initialization ************************************
    private void init() {
        if (getWidth() <= 0 || getHeight() <= 0
                || getPrefWidth() <= 0 || getPrefHeight() <= 0) {
            setPrefSize(PREFERRED_SIZE, PREFERRED_SIZE);
        }
        if (getMinWidth() <= 0 || getMinHeight() <= 0) {
            setMinSize(MINIMUM_SIZE, MINIMUM_SIZE);
        }
        if (getMaxWidth() <= 0 || getMaxHeight() <= 0) {
            setMaxSize(MAXIMUM_SIZE, MAXIMUM_SIZE);
        }
    }

    private void initGraphics() {
//        canvas = new Canvas();
//        ctx    = canvas.getGraphicsContext2D();
//        getChildren().add(canvas);
        this.setBorderColor(BORDER_DEFAULT);

    }

    // Edited method
    private void registerListeners() {
        widthProperty().addListener(observable -> recalc());
        heightProperty().addListener(observable -> recalc());
        frameVisibleProperty().addListener(observable -> draw());
        onProperty().addListener(observable -> draw());
        ledColorProperty().addListener(observable -> recalc());
        //************Ejercicio 3 ************************
        borderColorProperty().addListener(observable -> recalc());
        setOnMouseClicked((MouseEvent evt) -> setBlinking(!isBlinking()));
        setOnMouseEntered((MouseEvent evt) -> {
            color = getLedColor();
        });
        setOnMouseExited((MouseEvent evt) -> setLedColor(color));
    }

    //*********************Ejercicio3*******************************
    public Color getBorderColor() {
        return null == borderColor ? BORDER_DEFAULT : borderColor.get();
    }

    public void setBorderColor(final Color borderColor) {
        borderColorProperty().set(borderColor);
    }

    public final ObjectProperty<Color> borderColorProperty() {
        if (null == borderColor) {
            borderColor = new SimpleObjectProperty<>(this, "borderColor",
                     BORDER_DEFAULT);
        }
        return borderColor;
    }

    // ******************** Methods *******************************************
    public final boolean isOn() {
        return null == on ? false : on.get();
    }

    public final void setOn(final boolean ON) {
        onProperty().set(ON);
    }

    public final BooleanProperty onProperty() {
        if (null == on) {
            on = new SimpleBooleanProperty(this, "on", false);
        }
        return on;
    }

    public final boolean isBlinking() {
        return null == blinking ? _blinking : blinking.get();
    }

    public final void setBlinking(final boolean BLINKING) {
        if (null == blinking) {
            _blinking = BLINKING;
            if (BLINKING) {
                timer.start();
            } else {
                timer.stop();
                setOn(false);
            }
        } else {
            blinking.set(BLINKING);
        }
    }

    public final BooleanProperty blinkingProperty() {
        if (null == blinking) {
            blinking = new BooleanPropertyBase() {
                @Override
                public void set(final boolean BLINKING) {
                    super.set(BLINKING);
                    if (BLINKING) {
                        timer.start();
                    } else {
                        timer.stop();
                        setOn(false);
                    }
                }

                @Override
                public Object getBean() {
                    return ShapeLed.this;
                }

                @Override
                public String getName() {
                    return "blinking";
                }
            };
        }
        return blinking;
    }

    public final long getInterval() {
        return null == interval ? _interval : interval.get();
    }

    public final void setInterval(final long INTERVAL) {
        if (null == interval) {
            _interval = clamp(SHORTEST_INTERVAL, LONGEST_INTERVAL, INTERVAL);
        } else {
            interval.set(INTERVAL);
        }
    }

    public final LongProperty intervalProperty() {
        if (null == interval) {
            interval = new LongPropertyBase() {
                @Override
                public void set(final long INTERVAL) {
                    super.set(clamp(SHORTEST_INTERVAL, LONGEST_INTERVAL, INTERVAL));
                }

                @Override
                public Object getBean() {
                    return ShapeLed.this;
                }

                @Override
                public String getName() {
                    return "interval";
                }
            };
        }
        return interval;
    }

    public final boolean isFrameVisible() {
        return null == frameVisible ? _frameVisible : frameVisible.get();
    }

    public final void setFrameVisible(final boolean FRAME_VISIBLE) {
        if (null == frameVisible) {
            _frameVisible = FRAME_VISIBLE;
        } else {
            frameVisible.set(FRAME_VISIBLE);
        }
    }

    public final BooleanProperty frameVisibleProperty() {
        if (null == frameVisible) {
            frameVisible = new SimpleBooleanProperty(this, "frameVisible", _frameVisible);
        }
        return frameVisible;
    }

    public final Color getLedColor() {
        return null == ledColor ? Color.RED : ledColor.get();
    }

    public final void setLedColor(final Color ledColor) {
        ledColorProperty().set(ledColor);
    }

    public final ObjectProperty<Color> ledColorProperty() {
        if (null == ledColor) {
            ledColor = new SimpleObjectProperty<>(this, "ledColor", Color.RED);
        }
        return ledColor;
    }

    // ******************** Utility Methods ***********************************
    public static long clamp(final long MIN, final long MAX, final long VALUE) {
        if (VALUE < MIN) {
            return MIN;
        }
        if (VALUE > MAX) {
            return MAX;
        }
        return VALUE;
    }

    // ******************** Resize/Redraw *************************************
    private void recalc() {
        double size, offsetWidth, offsetHeight;
        double wsize = getWidth();

        if (getWidth() < getHeight()) {
            size = getWidth();
            offsetWidth = 0;
            offsetHeight = (getHeight() - getWidth()) / 2;
        } else {
            size = getHeight();
            offsetWidth = (getWidth() - getHeight()) / 2;
            offsetHeight = 0;
        }

        ledOffShadow = new InnerShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.65), 0.07 * size, 0, 0, 0);

        ledOnShadow = new InnerShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.65), 0.07 * size, 0, 0, 0);
        ledOnShadow.setInput(new DropShadow(BlurType.TWO_PASS_BOX, ledColor.get(), 0.36 * size, 0, 0, 0));

        frameGradient = new LinearGradient(0.14 * size, 0.14 * size,
                0.84 * size, 0.84 * size,
                false, CycleMethod.NO_CYCLE,
                new Stop(0.0, borderColor.get()
                        .deriveColor(0D,
                                1D,
                                0.77,
                                0.2D)),
                new Stop(1.0, borderColor.get()));

        hoverGradient = new LinearGradient(0.25 * size, 0.25 * size,
                0.74 * size, 0.74 * size,
                false, CycleMethod.NO_CYCLE,
                new Stop(0.0, ledColor.get().deriveColor(0d, 1d, 0.77, 1d)),
                new Stop(0.49, ledColor.get().deriveColor(0d, 1d, 0.5, 1d)),
                new Stop(1.0, ledColor.get()));

        ledOnGradient = new LinearGradient(0.25 * size, 0.25 * size,
                0.74 * size, 0.74 * size,
                false, CycleMethod.NO_CYCLE,
                new Stop(0.0, ledColor.get().deriveColor(0d, 1d, 0.77, 1d)),
                new Stop(0.49, ledColor.get().deriveColor(0d, 1d, 0.5, 1d)),
                new Stop(1.0, ledColor.get()));

        ledOffGradient = new LinearGradient(0.25 * size, 0.25 * size,
                0.74 * size, 0.74 * size,
                false, CycleMethod.NO_CYCLE,
                new Stop(0.0, ledColor.get().deriveColor(0d, 1d, 0.20, 1d)),
                new Stop(0.49, ledColor.get().deriveColor(0d, 1d, 0.13, 1d)),
                new Stop(1.0, ledColor.get().deriveColor(0d, 1d, 0.2, 1d)));

        highlightGradient = new RadialGradient(0, 0,
                (0.3 * size) + offsetWidth,
                (0.3 * size) + offsetHeight,
                0.29 * size,
                false, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.WHITE),
                new Stop(1.0, Color.TRANSPARENT));
        draw();
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

        double marco, ladoX, ladoY;

        if (width < height) {
            marco = width;
            ladoX = 0;
            ladoY = (height - width) / 2;
        } else {
            marco = height;
            ladoX = (width - height) / 2;
            ladoY = 0;
        }
        double marcoShapeLed = 0.72 * marco;
        double marcoLed = 0.58 * marco;

        double ajustarMarcoShape = (marco - marcoShapeLed) / 2;
        double ajustarMarcoLed = (marco - marcoLed) / 2;

        // Limpia la región y comienza a dibujar de nuevo
        getChildren().clear();

        if (isFrameVisible()) {
            var frame = new Rectangle(ladoX,
                    ladoY,
                    marco,
                    marco);
            frame.setFill(frameGradient);
            getChildren().add(frame);
        }
        var led = new Rectangle(ajustarMarcoShape + ladoX,
                ajustarMarcoShape + ladoY,
                marcoShapeLed,
                marcoShapeLed);
        if (isOn()) {
            led.setEffect(ledOnShadow);
            led.setFill(ledOnGradient);
        } else {
            led.setEffect(ledOffShadow);
            led.setFill(ledOffGradient);
        }
        getChildren().add(led);

        var cuadrado = new Rectangle(ajustarMarcoLed + ladoX,
                ajustarMarcoLed + ladoY,
                marcoLed,
                marcoLed);

        cuadrado.setFill(highlightGradient);
        getChildren().add(cuadrado);
    }
}
