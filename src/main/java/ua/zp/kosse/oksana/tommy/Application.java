package ua.zp.kosse.oksana.tommy;

import ua.zp.kosse.oksana.tommy.model.packaging.Box;
import ua.zp.kosse.oksana.tommy.model.part.Cube;
import ua.zp.kosse.oksana.tommy.model.part.Part;
import ua.zp.kosse.oksana.tommy.model.part.Sphere;
import ua.zp.kosse.oksana.tommy.model.part.Tetrahedron;

import java.util.Collection;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        // Упустимо код отримання списку деталей, задамо його безпосерезньо перед викликом потрібного коду

        // Деталі
        Collection<Part> Figure = List.of(
                new Cube(450, 0.1),
                new Cube(300, 0.2),
                new Sphere( 150, 0.3),
                new Tetrahedron(250, 0.2),
                new Cube(350, 0.2),
                new Sphere(100, 0.1)
        );

        // Коробки
        Collection<Box> Package = List.of(
                new Box(700),
                new Box(500),
                new Box(500),
                new Box(500),
                new Box(400),
                new Box(400),
                new Box(400),
                new Box(300),
                new Box(300),
                new Box(200)
        );

        int boxPackagingPricePerMeter = 40; // в копійках

        new PartsProcessor().packageParts(Figure, Package, boxPackagingPricePerMeter); // Підказка: в цьому виклиці складно розібратися з сігнатурою через погані імена зммінних.
    }

}
