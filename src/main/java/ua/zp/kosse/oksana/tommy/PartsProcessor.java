package ua.zp.kosse.oksana.tommy;

import ua.zp.kosse.oksana.tommy.exception.PackagingException;
import ua.zp.kosse.oksana.tommy.model.packaging.Box;
import ua.zp.kosse.oksana.tommy.model.part.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class PartsProcessor {
    public void packageParts(Collection<Part> parts, Collection<Box> boxes, int boxPackagingPricePerMillimeter) {
        // Наявні коробки
        List<Box> boxesPackage = new ArrayList<>(boxes);

        // Сортуємо ящики від малих до великих, щоб спросити пошук. Будемо перебирати від малих до великих
        boxesPackage.sort(Comparator.comparing(Box::getSide));

        for (Part part : parts) {
            try {
                Box box = selectSmallestBox(part, boxesPackage);
                int boxPrice = CalculateBox.calculateBoxPrice(box, boxPackagingPricePerMillimeter);
                reportRecommendedPackaging(part, box, boxPrice);
                boxesPackage.remove(box); // Підказка: як ми можемо краще назвати змінну boxesList, щоб була зрозуміла мета цієї операції?
            } catch (PackagingException e) {
                reportUnableToPackage(part);
            }
        }
    }

    private Box selectSmallestBox(Part part, List<Box> boxesSorted) {
        // Залежно від типу деталі розраховуємо для для неї потрібний розмір коробки
        int boxSizeRequired = 0;
        if (part instanceof Cube) {
            boxSizeRequired = ((Cube) part).getSide();
        } else if (part instanceof Sphere) {
            boxSizeRequired = ((Sphere) part).getRadius() * 2;
        } else if (part instanceof Tetrahedron) {
            /*
               Розрвхунок з огляду такого розміщення https://u.osu.edu/odmp/2016/10/30/rich-math-problem-3450-21/
               Тоді піраміда чотирма ребрами буде впитарися в сторони ящика (по діагоналі кожної такої сторони).
               Знаючи діагональ шукаємо потрібну сторону куба через рівнобедрений прямокутний трикутник (ділимо на
               корінь з двох).
            */
            double cubeSide = ((Tetrahedron) part).getSide() / Math.sqrt(2);
            boxSizeRequired = (int) Math.ceil(cubeSide); // Заокруглюємо отримане значення вгору
        }

        // Шукаємо об'єм деталі для визначення ваги
        int volume = 0;
        if (part instanceof Cube) {
            int side = ((Cube) part).getSide();
            volume = side * side * side;
        } else if (part instanceof Sphere) {
            int radius = ((Sphere) part).getRadius();
            double sphereVolume = 4d / 3 * Math.PI * radius * radius * radius; // Об'єм кулі рахуємо як 4/3πr³
            volume = (int) Math.ceil(sphereVolume); // Заокруглюємо отримане значення вгору
        } else if (part instanceof Tetrahedron) {
            int side = ((Tetrahedron) part).getSide();
            double tetrahedronVolume = Math.sqrt(2) / 12 * side * side * side; // Об'єм правильного тетраедру: (√2/12)a³
            volume = (int) Math.ceil(tetrahedronVolume); // Заокруглюємо отримане значення вгору.
        }

        // Якщо використовувати функції з попереднього перегляну джави 17 (preview), то код вище можна записати так:
        /* int boxSizeRequired = switch (part) {
            case Cube c -> c.getSide();
            case Sphere s -> s.getRadius() * 2;
            case Tetrahedron t -> (int) Math.ceil(t.getSide() / Math.sqrt(2));
        };
        int volume = switch (part) {
            case Cube c -> c.getSide() * c.getSide() * c.getSide();
            case Sphere s -> (int) Math.ceil(4d / 3 * Math.PI * s.getRadius() * s.getRadius() * s.getRadius());
            case Tetrahedron t -> (int) Math.ceil(Math.sqrt(2) / 12 * t.getSide() * t.getSide() * t.getSide());
        }*/
        // Підказка: чи впливає наявність коментаря вище на зрозумілість методу? Чи потрібен він в фінальному коді?
        int weight = (int) Math.ceil(volume * part.getDensity());

        double protectiveLayerThickness = part.getProtectiveLayerThickness(weight);
        int sideProtection=2; // захисний шар потрібен з обох боків
        // Фінально необхідний розмір ящику визначаємо додаванням до попереднього значення товщину захисного шару
        boxSizeRequired += protectiveLayerThickness * sideProtection;
        // Підказка: коментаря про фінальний розмір можна уникнути, ввівши додаткову змінну із промовистою назвою

        for (Box box : boxesSorted) {
            if (box.getSide() >= boxSizeRequired) {
                return box;
            }
        }
        // Якщо в циклі ми не знайшли жодного підходящого ящика, повідомимо про це за допомогою кидання виключення
        throw new PackagingException();
    } // Підказка: це довгий метод, через що його складно сприйати. Чи ми можемо спросити його осяжність?

    private void reportRecommendedPackaging(Part part, Box box, int price) {
        String message = String.format(
                "Деталь %s варто покласти у ящик %s. Вартість пакування: %.2f₴",
                part, box, price / 100d);
        System.out.println(message);
    }

    private void reportUnableToPackage(Part part) {
        String message = String.format(
                "Деталь %s не вдалося розмістити у наявні ящики",
                part);
        System.out.println(message);
    }

}
